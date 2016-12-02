package me.waltster.Fantasy.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.Util;

public class PlayerListener implements Listener{
	private FantasyMain main;
	
	/**
	 * 
	 * @param main
	 */
	public PlayerListener(FantasyMain main){
		this.main = main;
	}
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		
		event.setJoinMessage("");
		p.sendMessage(ChatColor.GOLD + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.player_join"));
		
		/*if(p.hasPlayedBefore() || PlayerMeta.getPlayerMeta(p).isAlive()){
			p.sendMessage(ChatColor.GOLD + "Sending you to your previous position");
			p.setDisplayName(ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p).getRace()) + PlayerMeta.getPlayerMeta(p).getRace().getName() + ChatColor.GOLD + "] " + p.getName());

			return;
		}else{*/
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().clear();
			
			ItemStack selector = new ItemStack(Material.EYE_OF_ENDER);
			ItemMeta meta = selector.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			meta.setDisplayName(ChatColor.GOLD + "Select Race and Class");
			lore.add(ChatColor.GOLD + "Click to open the selector.");
			meta.setLore(lore);
			selector.setItemMeta(meta);
			
			p.getInventory().addItem(selector);
			p.updateInventory();
			p.setHealth(p.getMaxHealth());
			p.setSaturation(100f);
			p.setLevel(0);
			p.setExp(0);
			PlayerMeta.getPlayerMeta(p).setNeedsRevived(false);
			for(PotionEffect effect : p.getActivePotionEffects()){
				p.removePotionEffect(effect.getType());
			}
			
			p.teleport(main.getLobbySpawn());
	//	}
	}
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerHurt(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player)event.getEntity();
			
			if(p.getLocation().getWorld().getName() == main.getLobbySpawn().getWorld().getName()){
				event.setCancelled(true);
			}
			
			if(event.getDamager() instanceof Player){
				Player p1 = (Player)event.getDamager();
				
				if(PlayerMeta.getPlayerMeta(p).getRace() == PlayerMeta.getPlayerMeta(p1).getRace()){
					event.setCancelled(true);
					p1.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cannot_harm_race"));
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		final Player p = event.getEntity();
		
		event.setKeepInventory(true);

		if(!PlayerMeta.getPlayerMeta(p).isAlive()){
			p.teleport(main.getLobbySpawn());
		}
		
		if(p.getKiller() != null && !p.getKiller().equals(p)){
			String deadName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p).getRace()) + PlayerMeta.getPlayerMeta(p).getRace() + ChatColor.GOLD + "] " + p.getName();
			String killerName = ChatColor.GOLD + "[" + Util.getChatColor(PlayerMeta.getPlayerMeta(p.getKiller()).getRace()) + PlayerMeta.getPlayerMeta(p.getKiller()).getRace() + ChatColor.GOLD + "] " + p.getKiller().getName();
		
			event.setDeathMessage(deadName + ChatColor.RED + " was killed by " + killerName);
		}
		
		PlayerMeta.getPlayerMeta(p).setNeedsRevived(true);
		p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("maps.yml").getConfig().getString("messages.revive_timer_started"));
		
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
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
				Util.sendPlayerToLobby(p, main.getLobbySpawn());
			}
		}, 20*30);
	}
	
	@EventHandler
	public void onDeadMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		
		if(PlayerMeta.getPlayerMeta(p).needsRevived()){
			event.setCancelled(true);
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
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerUseSelector(PlayerInteractEvent event){
		Player p = event.getPlayer();
		
		if(event.getItem() != null){
			if(event.getItem().getType() == Material.EYE_OF_ENDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Select Race and Class")){
				event.setCancelled(true);
				Race.showRaceSelector(p);
			}else if(event.getItem().getType() == Material.BLAZE_POWDER && event.getItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Revival Powder")){
				event.setCancelled(true);
	
				Collection<Entity> entities = Bukkit.getWorld("").getNearbyEntities(p.getEyeLocation(), 0.5, 0.5, 0.5);
				
				if(entities.size() > 1){
					p.sendMessage("Please focus on one entity.");
				}else{
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
						}
					}else{
						p.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cannot_revive_npc"));
					}
				}
			}
		}
	}
}
