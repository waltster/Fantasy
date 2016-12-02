package me.waltster.Fantasy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class Util {
	public static Location parseLocation(String s){
		String[] split = s.split(",");
		World w;
		Double x, y, z;
		
		if(split.length == 3){
			w = Bukkit.getWorld("Lobby");
			x = Double.parseDouble(split[0]);
			y = Double.parseDouble(split[1]);
			z = Double.parseDouble(split[2]);
		}else if(split.length == 4){
			w = Bukkit.getWorld(split[0]);
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
}
