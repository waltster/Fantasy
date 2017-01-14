package me.waltster.Fantasy.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;
import me.waltster.Fantasy.api.CityCaptureEvent;
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
	
	@EventHandler
	public void onCityCapture(CityCaptureEvent event){
	    Race newRace = PlayerMeta.getPlayerMeta(event.getCapturer()).getRace();
	    
	    for(Player p : Bukkit.getOnlinePlayers()){
	        if(PlayerMeta.getPlayerMeta(p).getRace() == event.getOldOwner()){
	            main.getItemMessageManager().sendMessage(p, ChatColor.WHITE + event.getCityName() + ChatColor.RED + " has been taken");
	        }else if(PlayerMeta.getPlayerMeta(p).getRace() == newRace){
	            main.getItemMessageManager().sendMessage(p, ChatColor.WHITE + event.getCityName() + ChatColor.GREEN + " has been taken");
	        }
	    }
	}
}
