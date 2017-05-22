/**
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
package me.waltster.Fantasy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Provides many static utilities that come into play at different parts of
 * the gamemode.
 * 
 * @author Walt Pach (walt@waltster.me)
 */
public class Util {
	/**
	 * Parse a location from a string.
	 * <p>This can include a world name, but must be in the following format:</p>
	 * <code>"WORLD,X,Y,Z"</code> or just the coordinates.
	 * 
	 * @param s The string to parse
	 * @return A location object or null.
	 */
	public static Location parseLocation(String s){
		String[] split = s.split(",");
		World w;
		Double x, y, z;
		
		if(split.length == 3){
			w = Bukkit.getWorld("lobby");
			x = Double.parseDouble(split[0]);
			y = Double.parseDouble(split[1]);
			z = Double.parseDouble(split[2]);
		}else if(split.length == 4){
			w = Bukkit.getWorld(split[0]);
			
			if(w == null){
			    w = Bukkit.createWorld(new WorldCreator(split[0]));
			}

			x = Double.parseDouble(split[1]);
			y = Double.parseDouble(split[2]);
			z = Double.parseDouble(split[3]);
		}else{
			System.out.println("Misformatted location");
			return null;
		}
		
		return new Location(w, x, y, z);
	}
	
	/**
	 * Get the color of each race.
	 * @param r The race
	 * @return The color for that race, by default {@link}ChatColor.WHITE
	 */
	public static ChatColor getChatColor(Race r){
		switch(r){
			case HUMAN:
				return ChatColor.RED;
			case DWARF:
				return ChatColor.GRAY;
			case ELF:
				return ChatColor.GOLD;
			default:
				return ChatColor.WHITE;
		}
	}
	
	/**
	 * Send a player to the lobby from the game.
	 * <p>
	 *   This resets the player's inventory effects, and gives them a
	 *   class/race selector. It also updates player meta to "alive" = false,
	 *   and sets their class/race to NONE.
	 * </p>
	 * @param p The player to send to the lobby
	 * @param lobbyLocation The location of the lobby "spawn".
	 */
	@SuppressWarnings("deprecation")
	public static void sendPlayerToLobby(Player p, Location lobbyLocation){
		// Clear out the player's armor and inventory
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().clear();
		
		// Create a new item that will be the class selector.
		ItemStack selector = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta meta = selector.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		// Set the meta-data for the item.
		meta.setDisplayName(ChatColor.GOLD + "Select Race and Class");
		lore.add(ChatColor.GOLD + "Click to open the selector.");
		meta.setLore(lore);
		selector.setItemMeta(meta);
		
		// Add the item to the player's inventory
		p.getInventory().addItem(selector);
		p.updateInventory();
		
		// Set the player to max food and health, then reset XP (in-game not Shotbow).
		p.setHealth(p.getMaxHealth());
		p.setSaturation(100f);
		p.setLevel(0);
		p.setExp(0);
		
		// Update the player's status internally to "newly spawned"
		PlayerMeta.getPlayerMeta(p).setNeedsRevived(false);
		PlayerMeta.getPlayerMeta(p).setKit(Kit.NONE);
		PlayerMeta.getPlayerMeta(p).setRace(Race.NONE);
		PlayerMeta.getPlayerMeta(p).setAlive(false);
		
		// Remove any effects from the player. If we don't do this then the player may re-join with class effects.
		for(PotionEffect effect : p.getActivePotionEffects()){
			p.removePotionEffect(effect.getType());
		}
		
		p.teleport(lobbyLocation);
	}
	
	/**
	 * Send the player to the game, from the lobby.
	 * <p>Also updates their PlayerMeta and gives them their kit.</p>
	 * @param p The player to send
	 * @param location The location to send them to
	 */
	public static void sendPlayerToGame(Player p, Location location){
		PlayerMeta meta = PlayerMeta.getPlayerMeta(p);
		
		meta.setNeedsRevived(false);
		meta.setAlive(true);
		meta.getKit().giveKitToPlayer(p);
		Util.updatePlayerSkin(p, meta.getRace());
		p.sendMessage(ChatColor.GOLD + "Sending you to game");
		p.teleport(location);
	}
	
	/**
	 * Clear the chat area for a player.
	 * @param p The player to clear their chat.
	 */
	public static void clearChat(Player p){
		for(int i = 0; i < 25; i++){
			p.sendMessage("                         ");
		}
	}
	
	/**
     * Show the player the buy selector. Not operational.
     * @deprecated Doesn't work.
     * @param p The player to show the selector to.
     */
    public static void showBuySelector(Player p){
        Inventory inv = Bukkit.createInventory(p, ((Race.values().length + 8) / 9) * 9, "Purchase Stuff");
        
        ItemStack item1 = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName(ChatColor.GOLD + "Race");
        List<String> lore1 = new ArrayList<String>();
        lore1.add("");
        lore1.add(ChatColor.GOLD + "Click this to purchase a race");
        meta1.setLore(lore1);
        item1.setItemMeta(meta1);
        inv.addItem(item1);
        
        ItemStack item2 = new ItemStack(Material.REDSTONE);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(ChatColor.GOLD + "Class");
        List<String> lore2 = new ArrayList<String>();
        lore2.add("");
        lore2.add(ChatColor.GOLD + "Click this to purchase a class");
        meta2.setLore(lore2);
        item2.setItemMeta(meta2);
        inv.addItem(item2);
        
        p.openInventory(inv);
    }
    
    /**
     * Show the class purchaser to a player.
     * @deprecated Not sure if Shotbow will use this, deprecated for now in case we need to remove it.
     * @param p The player to show the selector to.
     */
    public static void showClassBuySelector(Player p){
        Inventory inv = Bukkit.createInventory(p, ((Kit.values().length + 8) / 9) * 9, "Buy a Class");
        
        for(Race r : Race.values()){
            for(Kit k : r.kits){
                if(k.icon.getType() == Material.BEDROCK){
                    continue;
                }
                
                ItemStack item = k.icon.clone();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                
                lore.add(ChatColor.AQUA + "----------");
               
                if(k.doesPlayerOwnClass(p)){
                    lore.add(ChatColor.GREEN + "Unlocked");
                }else{
                    lore.add(ChatColor.RED + "Locked: " + ChatColor.WHITE + k.getCost() + ChatColor.RED + " Royals to unlock");
                }
                
                meta.setLore(lore);
                item.setItemMeta(meta);
                inv.addItem(item);
            }
        }
        
        p.openInventory(inv);
    }
    
    /**
     * Show the race purchaser to a player.
     * @deprecated Not sure if Shotbow will use this, deprecated for now in case we need to remove it.
     * @param p The player to show the selector to.
     */
    public static void showRaceBuySelector(Player p){
        Inventory inv = Bukkit.createInventory(p, ((Race.values().length + 8) / 9) * 9, "Buy a Race");
        
        for(Race r : Race.values()){
            if(r.icon.getType() == Material.BEDROCK){
                continue;
            }
            
            ItemStack item = r.icon.clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            
            lore.add(ChatColor.AQUA + "----------");
            
            if(r.doesPlayerOwnRace(p)){
                lore.add(ChatColor.GREEN + "Unlocked");
            }else{
                lore.add(ChatColor.RED + "Locked: " + ChatColor.WHITE + r.getCost() + ChatColor.RED + " Royals to unlock");
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        
        p.closeInventory();
        p.openInventory(inv);
    }
    
    /**
     * Update a player's skin to match their race.
     * @deprecated
     * @param p The player to update their skin.
     * @param r The race the player is.
     */
    public static void updatePlayerSkin(Player p, Race r){}
}
