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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Kit;
import me.waltster.Fantasy.PlayerMeta;
import me.waltster.Fantasy.Race;

/**
 * The class for handling events relating to specific classes and their
 * abilities.
 * 
 * @author walt
 * @since 0.0.1
 */
public class ClassAbilityListener implements Listener{
	private FantasyMain main;
	
	/**
	 * Create a new instance of ClassAbilityListener.
	 * Only one is needed for each plugin instance.
	 * 
	 * @param main The main instance of FantasyMain; needed for configuration.
	 */
	public ClassAbilityListener(FantasyMain main){
		this.main = main;
	}
	
	/**
	 * Handler for when a player opens an inventory. Depending on their class they
	 * can/can't open certian types of inventories.
	 * 
	 * @param event The InventoryOpenEvent passed by Spigot.
	 */
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event){
		InventoryType type = event.getInventory().getType();
		Player player = (Player)event.getPlayer();

		// If the inventory opened was an anvil make sure the player was a blacksmith
		if(type == InventoryType.ANVIL){
			// If not then tell the player their race can't use that item and cancel the opening.
			if(PlayerMeta.getPlayerMeta(player).getKit() != Kit.BLACKSMITH){
				player.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.race_cant_use_that"));
				event.setCancelled(true);
			}
		}
		
		// Cancel all villager inventories
		else if(type == InventoryType.MERCHANT){
			player.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.cant_trade_npc"));
			event.setCancelled(true);
		}
		
		// If the inventory opened was an enchantement table make sure the player was an elf or a dwarf
		else if(type == InventoryType.ENCHANTING){
			// If not then tell the player their race can't use that item and cancel the opening
			if(PlayerMeta.getPlayerMeta(player).getRace() != Race.ELF && PlayerMeta.getPlayerMeta(player).getKit() != Kit.ENCHANTER){
				player.sendMessage(ChatColor.RED + main.getConfigManager().getConfiguration("messages.yml").getConfig().getString("messages.race_cant_use_that"));
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFireDamage(EntityDamageEvent event){
	    if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK){
	        if(event.getEntity() instanceof Player){
	            Player p = (Player)event.getEntity();
	            
	            if(PlayerMeta.getPlayerMeta(p).getKit() == Kit.BLACKSMITH){
	                event.setCancelled(true);
	            }
	        }
	    }
	}
}
