package me.waltster.Fantasy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public enum Race {
	HUMAN(Material.STONE_SWORD){
		{
			lore.add("You are human.");
			lore.add("");
			lore.add("You are the backbone of civilization");
			lore.add("from ancient kingdoms, to modern");
			lore.add("empires, your race has been in the");
			lore.add("center from the start.");
			lore.add("You may not be as strong as others,");
			lore.add("but your ingenuity will take you");
			lore.add("places.");
			
			kits.add(Kit.CIVILIAN);
			kits.add(Kit.BUILDER);
			kits.add(Kit.FARMER);
		}
	},
	DWARF(Material.IRON_PICKAXE){
		{
			lore.add("You are human.");
			lore.add("");
			lore.add("You are the backbone of civilization");
			lore.add("from ancient kingdoms, to modern");
			lore.add("empires, your race has been in the");
			lore.add("center from the start.");
			lore.add("You may not be as strong as others,");
			lore.add("but your ingenuity will take you");
			lore.add("places.");
			
			kits.add(Kit.CIVILIAN);
			kits.add(Kit.BUILDER);
			kits.add(Kit.FARMER);
		}
	},
	ELF(Material.BOW){
		{
			lore.add("You are human.");
			lore.add("");
			lore.add("You are the backbone of civilization");
			lore.add("from ancient kingdoms, to modern");
			lore.add("empires, your race has been in the");
			lore.add("center from the start.");
			lore.add("You may not be as strong as others,");
			lore.add("but your ingenuity will take you");
			lore.add("places.");
			
			kits.add(Kit.CIVILIAN);
			kits.add(Kit.BUILDER);
			kits.add(Kit.FARMER);
		}
	},
	NONE(Material.BEDROCK){
		{
			
		}
	};

	static{
		for(Race r : Race.values()){
			r.init();
		}
	}
	
	ItemStack icon;
	List<String> lore = new ArrayList<String>();
	List<Kit> kits = new ArrayList<Kit>();
	
	/**
	 * 
	 * @param m
	 */
	Race(Material m){
		this.icon = new ItemStack(m);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getName());
		icon.setItemMeta(meta);
	}
	
	/**
	 * 
	 */
	private void init(){
		for(int i = 0; i < lore.size(); i++){
			String s = lore.get(i);
			s += ChatColor.AQUA;
			lore.set(i, s);
		}
		
		ItemMeta meta = icon.getItemMeta();
		meta.setLore(lore);
		icon.setItemMeta(meta);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName(){
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean doesPlayerOwnRace(Player p){
		return p.isOp() || p.hasPermission("fantasy.race." + getName().toLowerCase());
	}
	
	public List<Kit> getKits(){
		return this.kits;
	}
	
	/**
	 * 
	 * @param p
	 * @param r
	 */
	public static void showRaceSelector(Player p){
		Inventory inv = Bukkit.createInventory(p, ((Race.values().length + 8) / 9) * 9, "Choose Your Race");
		
		for(Race r : values()){
			if(r.getName() == "None"){
				continue;
			}
			
			ItemStack item = r.icon.clone();
			ItemMeta meta = item.getItemMeta();
			
			meta.getLore().add(ChatColor.AQUA + "----------");
			
			if(r.doesPlayerOwnRace(p)){
				meta.getLore().add(ChatColor.GREEN + "Unlocked");
			}else{
				meta.getLore().add(ChatColor.RED + "Locked. Visit the store at");
				meta.getLore().add(ChatColor.RED + "Shotbow.net to unlock.");
			}
			
			inv.addItem(item);
		}
		
		p.openInventory(inv);
	}
}
