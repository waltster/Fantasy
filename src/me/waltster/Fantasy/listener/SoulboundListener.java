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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoulboundListener implements Listener{
	
	@EventHandler
	public void onPlayerDropSoulbound(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		Item i = event.getItemDrop();
		
		if(i != null){
		    if(isSoulbound(i.getItemStack())){
	            p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1F, 0.5F);
	            event.getItemDrop().remove();
		    }
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Iterator<ItemStack> items = event.getDrops().iterator();
		
		while(items.hasNext()){
			if(isSoulbound(items.next())){
				items.remove();
			}
		}
	}
	
	public static boolean isSoulbound(ItemStack item){
		if(item.getType() == Material.AIR || item.getType() == null){
			return false;
		}
		
	    if(!item.hasItemMeta()){
	        return false;
	    }
	    
		ItemMeta meta = item.getItemMeta();

		if(meta.getDisplayName().contains(ChatColor.GOLD + "Soulbound")){
			return true;
		}
		
		return false;
	}
	
	public static void soulbind(ItemStack item){
		if(item.getType() != Material.AIR && item.hasItemMeta()){
		    ItemMeta meta = item.getItemMeta();
		    
			if(!meta.hasLore()){
				meta.setLore(Arrays.asList(ChatColor.GOLD + "Soulbound"));
			}else{
				List<String> lore = meta.getLore();
				lore.add(ChatColor.GOLD + "Soulbound");
				meta.setLore(lore);
			}
			
			item.setItemMeta(meta);
		}
	}
}
