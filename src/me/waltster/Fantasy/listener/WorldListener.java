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
	            p.sendMessage(ChatColor.WHITE + event.getCityName() + ChatColor.RED + " has been taken");
	        }else if(PlayerMeta.getPlayerMeta(p).getRace() == newRace){
	            p.sendMessage(ChatColor.WHITE + event.getCityName() + ChatColor.GREEN + " has been taken");
	        }
	    }
	}
}
