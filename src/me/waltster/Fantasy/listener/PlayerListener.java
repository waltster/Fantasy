/*
 * Copyright 2016-2017 Walter Pach, all rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.waltster.Fantasy.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.StatType;
import me.waltster.Fantasy.Util;
import me.waltster.Fantasy.api.CityCaptureEvent;

/**
 * The Listener for all Player events (excluding class/race specific ones).
 * @author walt
 * @since 0.0.1
 */
public class PlayerListener implements Listener{
	private FantasyMain main;
	private Random rand = new Random();
	
	/**
	 * Create a new instance of PlayerListener. Only one is needed per plugin though.
	 * @param main The instance of this plugin.
	 */
	public PlayerListener(FantasyMain main){
		this.main = main;
	}

	/**
	 * Handle a player join event
	 *
	 * When a player joins we need to send them a welcome message then give them their items, then send them where
	 * they need to go.
	 *
	 * @param event The PlayerJoinEvent passed by Spigot
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();

		event.setJoinMessage("");

		p.sendMessage(ChatColor.GOLD + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.player_join"));


		if(p.hasPlayedBefore() || PlayerMeta.getPlayerMeta(p).isAlive()){
			p.sendMessage(ChatColor.GOLD + "Sending you to your previous position. Type /die to restart from the beginning");
			return;
		}else{
			Util.sendPlayerToLobby(p, main.getLobbySpawn());
		}
	}

	/**
	 * Handle an EntityDamageByEntityEvent
	 *
	 * We use this to cancel any in-lobby violence, as well as any special
	 * damage that may happen between entities.
	 *
	 * @param event The EntityDamgeByEntityEvent passed by Spigot
	 */
	@EventHandler
	public void onPlayerHurt(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player)event.getEntity();

			if(p.getLocation().getWorld().getName() == main.getLobbySpawn().getWorld().getName()){
				if(event.getCause() == DamageCause.VOID) {
					event.setCancelled(true);
					Util.sendPlayerToLobby(p, main.getLobbySpawn());
				}
				
				event.setCancelled(true);
			}
			

			if(event.getDamager() instanceof Player){
				Player p1 = (Player)event.getDamager();

				if(PlayerMeta.getPlayerMeta(p).getRace() == PlayerMeta.getPlayerMeta(p1).getRace()){
					event.setCancelled(true);
					// Set this to whatever you want. Right now it just uses a pre-configured message from 'messages.yml'
					p1.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cannot_harm_race"));
				}else if(PlayerMeta.getPlayerMeta(p1).getKit() == Kit.WARRIOR){
				    event.setDamage(event.getDamage() + 1);
				}else if(PlayerMeta.getPlayerMeta(p1).getKit() == Kit.ARCHER && event.getDamager().getType() == EntityType.ARROW) {
					event.setDamage(event.getDamage() * 2);
				}
			}
		}
	}

	/**
	 * Handle a PlayerDeathEvent
	 *
	 * We use this to capture when a player dies and handle XP drop, sending them to the lobby,
	 * broadcasting messages, and setting up a timer to allow for revival.
	 *
	 * @param event The PlayerDeathEvent passed by Spigot
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		final Player p = event.getEntity();

		if(!PlayerMeta.getPlayerMeta(p).isAlive()){
			p.teleport(main.getLobbySpawn());
		}

		if(p.getKiller() != null && !p.getKiller().equals(p)){
			String deadName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p).getRace()) + PlayerMeta.getPlayerMeta(p).getRace() + ChatColor.GOLD + "] " + p.getName();
			String killerName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p.getKiller()).getRace()) + PlayerMeta.getPlayerMeta(p.getKiller()).getRace() + ChatColor.GOLD + "] " + p.getKiller().getName();

			event.setDeathMessage(deadName + ChatColor.RED + " was killed by " + killerName);
			main.getStatsManager().incrementStat(StatType.KILLS, p.getKiller());

			p.getKiller().sendMessage(ChatColor.GREEN + "+3 Shotbow XP");
			main.getStatsManager().incrementStat(StatType.SHOTBOW_XP, p.getKiller(), 3);
		}

		PlayerMeta.getPlayerMeta(p).setNeedsRevived(true);

		p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revive_timer_started"));

		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				if(PlayerMeta.getPlayerMeta(p).needsRevived() == false){
					return;
				}

				PlayerMeta.getPlayerMeta(p).setAlive(false);

				for(int i = 0; i < p.getExp() / 2; i++){
					main.getMap().spawnEntity(p.getLocation(), EntityType.EXPERIENCE_ORB);
				}

				p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.you_died"));
				Util.updatePlayerSkin(p, Race.NONE);
				Util.sendPlayerToLobby(p, main.getLobbySpawn());
			}
		}, 20*15); // <-- number is the number of ticks. Each tick is 1/20th of a second, so 30*20 is 30s.
	}

	/**
	 * Handle a PlayerMoveEvent, specifically if the player needs revived.
	 *
	 * @param event The PlayerMoveEvent passed by Spigot
	 */
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player p = event.getPlayer();

		if(PlayerMeta.getPlayerMeta(p).needsRevived()){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cant_move_while_dead"));
		}else if(PlayerMeta.getPlayerMeta(p).getRace() == Race.GHOST && PlayerMeta.getPlayerMeta(p).abilityEnabled()){
			if(p.getLocation().getBlock().getType() == Material.WATER) {
				p.damage(4);
			}
			
			if(PlayerMeta.getPlayerMeta(p).getKit() == Kit.SHADE){
				p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
			}else if(PlayerMeta.getPlayerMeta(p).getKit() == Kit.SPARKY){
				p.getWorld().playEffect(p.getLocation(), Effect.LAVA_POP, 0);
				
				if(rand.nextInt(10) < 8){
					p.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
					//p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 0);
				}
			}
		}
	}

	/**
	 *
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerSelectRaceOrKit(InventoryClickEvent event){
		Player p = (Player)event.getWhoClicked();

		if(event.getInventory().getTitle().startsWith("Select Your Race")){
			if(event.getCurrentItem().getType() == Material.AIR){
				return;
			}

			p.closeInventory();
			event.setCancelled(true);
			String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();

			if(!Race.valueOf(name).doesPlayerOwnRace(p)){
				p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.race_not_owned"));
				return;
			}

			p.sendMessage(ChatColor.GREEN + "Race " + ChatColor.WHITE + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()) + ChatColor.GREEN + " selected.");
			PlayerMeta.getPlayerMeta(p).setRace(Race.valueOf(name));
			p.setDisplayName(ChatColor.GOLD + "[" + Util.getChatColor(Race.valueOf(name)) + Race.valueOf(name).getName() + ChatColor.GOLD + "] " + p.getName());
			Kit.showKitSelector(p, PlayerMeta.getPlayerMeta(p).getRace());

		}else if(event.getInventory().getTitle().startsWith("Select Your Class")){
			if(event.getCurrentItem().getType() == Material.AIR){
				return;
			}

			p.closeInventory();
			event.setCancelled(true);
			String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();

			if(!Kit.valueOf(name).doesPlayerOwnClass(p)){
				p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.class_not_owned"));
				return;
			}

			p.sendMessage(ChatColor.GREEN + "Class " + ChatColor.WHITE + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()) + ChatColor.GREEN + " selected.");
			PlayerMeta.getPlayerMeta(p).setKit(Kit.valueOf(name));
		}else if(event.getInventory().getTitle().startsWith("Purchase Stuff")){
		    if(event.getCurrentItem().getType() == Material.AIR){
		        return;
		    }

		    p.closeInventory();
		    event.setCancelled(true);

		    String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();

		    if(name == "RACE"){
		        Util.showRaceBuySelector(p);
		        p.sendMessage(ChatColor.GREEN + "Opening race purchasing selector");
		    }else if(name == "CLASS"){
                Util.showClassBuySelector(p);
                p.sendMessage(ChatColor.GREEN + "Opening class purchasing selector");
		    }
		}else if(event.getInventory().getTitle().startsWith("Buy a Race")){
		    if(event.getCurrentItem().getType() == Material.AIR){
                return;
            }

            p.closeInventory();
            event.setCancelled(true);

            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();

            if(!Race.valueOf(name).doesPlayerOwnRace(p)){
                int amount = main.getStatsManager().getStat(StatType.SHOTBOW_XP, p);

                if(amount >= Race.valueOf(name).getCost()){
                  //  PermissionUser user = PermissionsEx.getUser(p);

                    main.getStatsManager().setValue(StatType.SHOTBOW_XP, p, amount - Race.valueOf(name).getCost());
                   // user.addPermission("fantasy.race." + name.toLowerCase());
                   // user.save();
                    p.sendMessage(ChatColor.GREEN + "Purchased race " + ChatColor.WHITE + Race.valueOf(name).getName());
                }else{
                    p.sendMessage(ChatColor.RED + "You only have " + ChatColor.WHITE + amount + ChatColor.RED + " Royals");
                }
            }
		}else if(event.getInventory().getTitle().startsWith("Buy a Class")){
            if(event.getCurrentItem().getType() == Material.AIR){
                return;
            }

            p.closeInventory();
            event.setCancelled(true);

            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase();

            if(!Kit.valueOf(name).doesPlayerOwnClass(p)){
                int amount = main.getStatsManager().getStat(StatType.SHOTBOW_XP, p);

                if(amount >= Kit.valueOf(name).getCost()){
                  //  PermissionUser user = PermissionsEx.getUser(p);

                    main.getStatsManager().setValue(StatType.SHOTBOW_XP, p, amount - Race.valueOf(name).getCost());
                   // user.addPermission("fantasy.kit." + name.toLowerCase());
                    //user.save();
                    p.sendMessage(ChatColor.GREEN + "Purchased class " + ChatColor.WHITE + Race.valueOf(name).getName());
                }else{
                    p.sendMessage(ChatColor.RED + "You only have " + ChatColor.WHITE + amount + ChatColor.RED + " ShotbowXP");
                }
            }
        }
	}

	@EventHandler
	public void onSoulboundInteract(InventoryClickEvent event){
		if(event.getCurrentItem().hasItemMeta()) {
			if(event.getCurrentItem().getItemMeta().getLore().contains(ChatColor.GOLD + "Soulbound")) {
				event.setCancelled(true);
			}
		}
	    
	}

	/**
	 * Handle a PlayerInteractEvent (essentially any clicking event)
	 *
	 * @param event The PlayerInteractEvent passed by Spigot
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player p = event.getPlayer();

		if(event.getItem() != null){
			if(event.getItem().getType() == Material.EYE_OF_ENDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Select Race and Class")){
				event.setCancelled(true);
				Race.showRaceSelector(p);
			}
			else if(event.getItem().getType() == Material.BLAZE_POWDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Revival Powder")){
				event.setCancelled(true);

				Collection<Entity> entities = Bukkit.getWorld(event.getPlayer().getLocation().getWorld().getName()).getNearbyEntities(p.getEyeLocation(), 0.5, 0.5, 0.5);

				if(entities.size() > 1){
					p.sendMessage("Please focus on one entity.");
				}
				else{
					Entity e = entities.iterator().next();

					if(e instanceof Player){
						Player p1 = (Player)e;

						if(PlayerMeta.getPlayerMeta(p1).needsRevived()){
							PlayerMeta.getPlayerMeta(p1).setNeedsRevived(false);
							p1.sendMessage(ChatColor.GREEN + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revived_message") + p.getDisplayName());
							p.sendMessage(ChatColor.GREEN + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revived_sucessfully"));


							ItemStack toRemove = event.getItem().clone();
							toRemove.setAmount(1);
							p.getInventory().remove(toRemove);
							p.updateInventory();
						}
					}
				}
			}
			else if(event.getItem().getType() == Material.APPLE){
				event.setCancelled(true);
				PlayerMeta.getPlayerMeta(p).setAbilityEnabled(!PlayerMeta.getPlayerMeta(p).abilityEnabled());
				
				if(!PlayerMeta.getPlayerMeta(p).abilityEnabled() && PlayerMeta.getPlayerMeta(p).getRace() == Race.GHOST) {
					main.getLogger().info("Trigger!");
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
				}else if(PlayerMeta.getPlayerMeta(p).abilityEnabled() && PlayerMeta.getPlayerMeta(p).getRace() == Race.GHOST) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true), true);
				}
			}else if (event.getItem().getType() == Material.COMPASS) {
				boolean setToNextCity = false;
				boolean compassSet = false;
				
				while(!compassSet) {
					for(String cityName : main.getCityJoinSignManager().getCitySpawnLocations().keySet()) {
						if(setToNextCity) {
							ItemMeta meta = event.getItem().getItemMeta();
							meta.setDisplayName(ChatColor.RED + "Pointing to " + ChatColor.AQUA + cityName);
							event.getItem().setItemMeta(meta);
							p.setCompassTarget(Util.parseLocation(main.getCityJoinSignManager().getCitySpawnLocations().get(cityName)));
							compassSet = true;
							break;
						}
						
						if(event.getItem().getItemMeta().getDisplayName().contains(cityName) || event.getItem().getItemMeta().getDisplayName().contains("Click to point")) {
							setToNextCity = true;
						}
					}
				}
			}
			// TODO: See if this needs removed
			return;
		}

		if(event.getClickedBlock() != null){
			Material mat = event.getClickedBlock().getType();

			if(mat == Material.SIGN_POST || mat == Material.WALL_SIGN || mat == Material.SIGN){
				Sign sign = (Sign)event.getClickedBlock().getState();

				if(sign.getLine(0).contains(ChatColor.WHITE + "[" + ChatColor.GOLD + "City" + ChatColor.WHITE + "]")){
				    if(sign.getLine(3).contains("Join")){
				        event.setCancelled(true);
	                    String cityName = ChatColor.stripColor(sign.getLine(1));
	                    YamlConfiguration config = main.getConfigManager().getConfiguration("maps.yml").getConfig();

	                    if(config.getString("cities." + cityName + ".spawn") != null){
	                        Location citySpawn = Util.parseLocation(main.getConfigManager().getConfiguration("maps.yml").getConfig().getString("cities." + cityName + ".spawn"));

	                        if(Race.valueOf(ChatColor.stripColor(sign.getLine(2)).toUpperCase()) != PlayerMeta.getPlayerMeta(p).getRace()){
	                            p.sendMessage(ChatColor.RED + "That city isn't owned by your race!");
	                            return;
	                        }
	                        else{
	                            Util.sendPlayerToGame(p, citySpawn);
	                        }
	                    }else{
	                        p.sendMessage(ChatColor.RED + "City sign not working");
	                        main.getLogger().warning(ChatColor.RED + "City " + cityName + " was missing spawn location in maps.yml.");
	                    }
				    }else if(sign.getLine(3).contains("Capture")){
				        event.setCancelled(true);
                        String cityName = ChatColor.stripColor(sign.getLine(1));
                      
                        if(Race.valueOf(ChatColor.stripColor(sign.getLine(2)).toUpperCase()) == PlayerMeta.getPlayerMeta(event.getPlayer()).getRace()){
                            event.getPlayer().sendMessage(ChatColor.RED + "City already captured by your race!");
                        }else{
                            Race r = Race.valueOf(ChatColor.stripColor(sign.getLine(2)).toUpperCase());

                            CityCaptureEvent event1 = new CityCaptureEvent(cityName, r, event.getPlayer());
                            main.getServer().getPluginManager().callEvent(event1);

                            if(event1.isCancelled()){
                                return;
                            }

                            main.getConfigManager().getConfiguration("maps.yml").getConfig().set("cities." + cityName + ".race", r.name());
                            main.getConfigManager().getConfiguration("maps.yml").save();
                            sign.setLine(2, ChatColor.GOLD + r.getName());

                            // Reload signs
                            main.getCityJoinSignManager().loadCitySigns();
                            main.getCityJoinSignManager().updateSigns(cityName);
                            main.getCityCaptureSignManager().loadCitySigns();
                            main.getCityCaptureSignManager().updateSigns(cityName);

                            Bukkit.broadcastMessage(ChatColor.WHITE + event.getPlayer().getDisplayName() + ChatColor.GOLD + " has captured " + ChatColor.WHITE + cityName);

                            Race pRace = PlayerMeta.getPlayerMeta(event.getPlayer()).getRace();

                            for(Player p1 : Bukkit.getOnlinePlayers()){
                                if(PlayerMeta.getPlayerMeta(p).getRace() == pRace){
                                    p1.sendMessage(ChatColor.BLUE + "+15 ShotbowXP");
                                    main.getStatsManager().incrementStat(StatType.SHOTBOW_XP, p1, 15);
                                }
                            }
                        }
				    }
				}
			}
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event){
	    EntityType type = event.getEntity().getType();

	    if(type != EntityType.PLAYER){
	    	if(event.getEntity().getKiller() == null) {
	    		return;
	    	}else{
	    		event.getEntity().getKiller().sendMessage(ChatColor.GREEN + "+1 Shotbow XP");
	    		main.getStatsManager().incrementStat(StatType.SHOTBOW_XP, event.getEntity().getKiller());
	    	}

	        // TODO: +1 Shotbow XP
	    }
	}
	

	

}
