package me.waltster.Fantasy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;

public class Util {
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
	 * 
	 * @param p
	 * @param lobbyLocation
	 */
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
	 * 
	 * @param p
	 * @param location
	 */
	public static void sendPlayerToGame(Player p, Location location){
		PlayerMeta meta = PlayerMeta.getPlayerMeta(p);
		
		meta.setNeedsRevived(false);
		meta.setAlive(true);
		meta.getKit().giveKitToPlayer(p);
		p.sendMessage(ChatColor.GOLD + "Sending you to game");
		p.teleport(location);
	}
	
	public static void clearChat(Player p){
		for(int i = 0; i < 30; i++){
			p.sendMessage("                         ");
		}
	}
	
	/**
     * 
     * @param p
     * @param r
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
    
    public static void updatePlayerName(Player p, String newName){
        EntityHuman eh = ((CraftPlayer)p).getHandle();
        PacketPlayOutEntityDestroy p29 = new PacketPlayOutEntityDestroy(new int[]{p.getEntityId()});
        PacketPlayOutNamedEntitySpawn p20 = new PacketPlayOutNamedEntitySpawn(eh);
        try {
            Field profileField = p20.getClass().getDeclaredField("b");
            profileField.setAccessible(true);
            profileField.set(p20, new net.minecraft.server.v1_10_R1.GameProfile(""+p.getEntityId(), newName));
        } catch (Exception e) {
            Bukkit.broadcastMessage("Not Work!");
        }
        for(Player o : Bukkit.getOnlinePlayers()){
            if(!o.getName().equals(p.getName())){
                ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p29);
                ((CraftPlayer)o).getHandle().playerConnection.sendPacket(p20);
            }
        }
    }
}
