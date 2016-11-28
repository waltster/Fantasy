package me.waltster.Fantasy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
		p.getInventory().clear();
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.updateInventory();
		
		PlayerMeta.getPlayerMeta(p).setRace(Race.NONE);
		p.setDisplayName(ChatColor.GOLD + "[" + Util.getChatColor(Race.NONE) + "Lobby" + ChatColor.GOLD + "] " + p.getName());
		p.setHealth(p.getMaxHealth());
		p.setExp(0);
		p.setSaturation(100f);
		p.teleport(lobbyLocation);
	}
}
