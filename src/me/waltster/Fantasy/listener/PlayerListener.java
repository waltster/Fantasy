package me.waltster.Fantasy.listener;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.StatType;
import me.waltster.Fantasy.Util;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * The Listener for all Player events (excluding class/race specific ones).
 * @author walt
 * @since 0.0.1
 */
public class PlayerListener implements Listener{
	private FantasyMain main;
	
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
		
		// Change this to whatever you want. Right now it just loads the configurable message from 'messages.yml'.
		p.sendMessage(ChatColor.GOLD + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.player_join"));
		
		 
		if(p.hasPlayedBefore() || PlayerMeta.getPlayerMeta(p).isAlive()){
			p.sendMessage(ChatColor.GOLD + "Sending you to your previous position");
			Util.updatePlayerName(p, ChatColor.GOLD + "[" + ChatColor.WHITE + PlayerMeta.getPlayerMeta(p).getRace().getName() + ChatColor.GOLD + "] " + p.getName());
			return;
		}else{
		    Util.updatePlayerName(p, ChatColor.GOLD + "[" + "Lobby" + ChatColor.GOLD + "] " + p.getName());

			// I wrote a method in the utilities class to clean up this area. We have to pass the
			// location because there's no static instance of FantasyMain to use with Util.
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
		// If a player was hurt we need to check if it was in the lobby
		if(event.getEntity() instanceof Player){
			Player p = (Player)event.getEntity();
			
			// If the player's location is in the same world as the lobby cancel the action.
			if(p.getLocation().getWorld().getName() == main.getLobbySpawn().getWorld().getName()){
				event.setCancelled(true);
			}
			
			// If the entity who damaged the player was a player as well then handle inter-racial conflict
			if(event.getDamager() instanceof Player){
				Player p1 = (Player)event.getDamager();
				
				// If the two players are the same Race then cancel the damage and send a message to the damager.
				if(PlayerMeta.getPlayerMeta(p).getRace() == PlayerMeta.getPlayerMeta(p1).getRace()){
					event.setCancelled(true);
					// Set this to whatever you want. Right now it just uses a pre-configured message from 'messages.yml'
					p1.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cannot_harm_race"));
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
		// This needs to be final so that we can use it in the BukkitRunnable
		final Player p = event.getEntity();
		
		// If the player died but isn't in the game just send them back to the lobby spawn.
		// An example of this happening would be if someone fell off the edge of the lobby world.
		if(!PlayerMeta.getPlayerMeta(p).isAlive()){
			p.teleport(main.getLobbySpawn());
		}
		
		// If the player was killed by a player and it wasn't himself then lets broadcast a message to everyone
		if(p.getKiller() != null && !p.getKiller().equals(p)){
			// Format the killed and killer player's name with their race and name
			String deadName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p).getRace()) + PlayerMeta.getPlayerMeta(p).getRace() + ChatColor.GOLD + "] " + p.getName();
			String killerName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p.getKiller()).getRace()) + PlayerMeta.getPlayerMeta(p.getKiller()).getRace() + ChatColor.GOLD + "] " + p.getKiller().getName();
		
			// Send the death message to everyone.
			event.setDeathMessage(deadName + ChatColor.RED + " was killed by " + killerName);
			main.getStatsManager().incrementStat(StatType.KILLS, p.getKiller());
			
			p.getKiller().sendMessage(ChatColor.GREEN + "+3 Royals");
			main.getStatsManager().incrementStat(StatType.ROYALS, p.getKiller(), 3);
		}
		
		// Store that the player needs revived before he can move again.
		PlayerMeta.getPlayerMeta(p).setNeedsRevived(true);
		
		// Tell the player they've got 30s to be revived.
		p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revive_timer_started"));
		
		// Setup a new task for 30s from now to either remove the player (if they weren't revived,
		// or continue gameplay (if they were).
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
			@Override
			public void run(){
				// If the player was revived exit early
				if(PlayerMeta.getPlayerMeta(p).needsRevived() == false){
					return;
				}
				
				// Update the player's state to "not in game"
				PlayerMeta.getPlayerMeta(p).setAlive(false);
			
				// Drop half the player's XP on the ground
				for(int i = 0; i < p.getExp() / 2; i++){
					main.getMap().spawnEntity(p.getLocation(), EntityType.EXPERIENCE_ORB);
				}
				
				// Send the player a message that he died
				p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.you_died"));
				// Send the player to the lobby
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
	public void onDeadMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		
		// If the movement was just looking around then permit it. We are okay with the player looking around,
		// just not moving.
		if(event.getFrom().getBlock().getLocation() != p.getLocation().getBlock().getLocation() && PlayerMeta.getPlayerMeta(p).needsRevived()){
			event.setCancelled(true);
			// Send the player a pre-configured message that they can't move.
			p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cant_move_while_dead"));
		}
	}
	
	/**
	 * 
	 * @param event
	 */
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
                int amount = main.getStatsManager().getStat(StatType.ROYALS, p);
                
                if(amount >= Race.valueOf(name).getCost()){
                    PermissionUser user = PermissionsEx.getUser(p);

                    main.getStatsManager().setValue(StatType.ROYALS, p, amount - Race.valueOf(name).getCost());
                    user.addPermission("fantasy.race." + name.toLowerCase());
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
                int amount = main.getStatsManager().getStat(StatType.ROYALS, p);
                
                if(amount >= Kit.valueOf(name).getCost()){
                    PermissionUser user = PermissionsEx.getUser(p);

                    main.getStatsManager().setValue(StatType.ROYALS, p, amount - Race.valueOf(name).getCost());
                    user.addPermission("fantasy.kit." + name.toLowerCase());
                    p.sendMessage(ChatColor.GREEN + "Purchased class " + ChatColor.WHITE + Race.valueOf(name).getName());
                }else{
                    p.sendMessage(ChatColor.RED + "You only have " + ChatColor.WHITE + amount + ChatColor.RED + " Royals");
                }
            }
        }
	}
	
	@EventHandler
	public void onSoulboundInteract(InventoryClickEvent event){
	    if(event.getInventory().getType() == InventoryType.CHEST || event.getInventory().getType() == InventoryType.DISPENSER || event.getInventory().getType() == InventoryType.DROPPER){
	        if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()){
    	        if(event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "Soulbound")){
    	            event.setCancelled(true);
    	        }
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
		
		// If the player clicked with an item in their hand then we need to check what it was.
		if(event.getItem() != null){
			// If the item was the Race selector then open it
			if(event.getItem().getType() == Material.EYE_OF_ENDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Select Race and Class")){
				event.setCancelled(true);
				Race.showRaceSelector(p);
			}
			// If the item was revival powder then use it
			else if(event.getItem().getType() == Material.BLAZE_POWDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Revival Powder")){
				event.setCancelled(true);
	
				// Get all entities within half a block of the player's eyeline.
				Collection<Entity> entities = Bukkit.getWorld("").getNearbyEntities(p.getEyeLocation(), 0.5, 0.5, 0.5);
				
				// If there is more than one entity inside that eye-line then tell the player to focus on one.
				if(entities.size() > 1){
					p.sendMessage("Please focus on one entity.");
				}
				else{
					// Fetch the entity that the player is focused on
					Entity e = entities.iterator().next();
					
					// We only use revival powder on players, so filter non-player entities out
					if(e instanceof Player){
						Player p1 = (Player)e;
					
						// If the player needs revived then revive them, but otherwise don't use the powder.
						if(PlayerMeta.getPlayerMeta(p1).needsRevived()){
							// Update the player's state and tell both players it worked
							PlayerMeta.getPlayerMeta(p1).setNeedsRevived(false);
							p1.sendMessage(ChatColor.GREEN + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revived_message") + p.getDisplayName());
							p.sendMessage(ChatColor.GREEN + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.revived_sucessfully"));
							
							
							// Remove the item from the player's inventory
							ItemStack toRemove = event.getItem().clone();
							toRemove.setAmount(1);
							p.getInventory().remove(toRemove);
							p.updateInventory();
						}
					}
				}
			}
			
			// TODO: See if this needs removed
			return;
		}
		
		// If the player clicked a block then we need to check what it was
		if(event.getClickedBlock() != null){
			Material mat = event.getClickedBlock().getType();
			
			// If the player clicked a sign then check if it was a city-join sign.
			if(mat == Material.SIGN_POST || mat == Material.WALL_SIGN || mat == Material.SIGN){
				Sign sign = (Sign)event.getClickedBlock().getState();
				
				// If the sign is a city sign then we need to send the player to it
				if(sign.getLine(0).contains(ChatColor.GOLD + "[City]")){
					event.setCancelled(true);
					// Fetch the city name from the sign and get the configuration for that city.
					String cityName = ChatColor.stripColor(sign.getLine(1)).toLowerCase();
					YamlConfiguration config = main.getConfigManager().getConfiguration("maps.yml").getConfig();
					
					// If the city is in the configuration then let's send the player
					if(config.getString("cities." + cityName + ".spawn") != null){
						// Get the spawn location
					    Location citySpawn = Util.parseLocation(main.getConfigManager().getConfiguration("maps.yml").getConfig().getString("cities." + cityName + ".spawn"));
						// Make sure the city is owned by the Player's race and if not then send them a message saying so
						if(Race.valueOf(ChatColor.stripColor(sign.getLine(2)).toUpperCase()) != PlayerMeta.getPlayerMeta(p).getRace()){
							p.sendMessage(ChatColor.RED + "That city isn't owned by your race!");
							return;
						}
						// If the city is owned by the player's race then send them there
						else{
							Util.sendPlayerToGame(p, citySpawn);
						}
					}
					// Oops! There wasn't a spawn in the configuration for the city. Tell the player and log it.
					else{
						p.sendMessage(ChatColor.RED + "City sign not working");
						main.getLogger().warning(ChatColor.RED + "City " + cityName + " was missing spawn location in maps.yml.");
					}
				}
			}
		}
	}
}
