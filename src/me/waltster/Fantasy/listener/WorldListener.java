package me.waltster.Fantasy.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.waltster.Fantasy.FantasyMain;
import net.md_5.bungee.api.ChatColor;

public class WorldListener implements Listener{
	private FantasyMain main;
	
	public WorldListener(FantasyMain main){
		this.main = main;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player breaker = event.getPlayer();
		
		boolean breakAllowed = breaker.hasPermission("fantasy.build.lobby") || breaker.isOp();
	
		if(event.getBlock().getLocation().getWorld().getName().toLowerCase() == main.getLobbySpawn().getWorld().getName().toLowerCase() && !breakAllowed){
			breaker.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cant_build_area"));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player placer = event.getPlayer();
		
		boolean placeAllowed = placer.hasPermission("fantasy.build.lobby") || placer.isOp();
		
		if(event.getBlock().getLocation().getWorld().getName().toLowerCase() == main.getLobbySpawn().getWorld().getName().toLowerCase() && !placeAllowed){
			placer.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cant_build_area"));
			event.setCancelled(true);
		}
	}
}
