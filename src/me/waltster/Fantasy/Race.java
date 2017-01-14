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
	HUMAN(Material.STONE_SWORD, 0){
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
	DWARF(Material.IRON_PICKAXE, 5000){
		{
			lore.add("You are dwarvish.");
			lore.add("");
			lore.add("You are the backbone of civilization");
			lore.add("from ancient kingdoms, to modern");
			lore.add("empires, your race has been in the");
			lore.add("center from the start.");
			lore.add("You may not be as strong as others,");
			lore.add("but your ingenuity will take you");
			lore.add("places.");
			
			kits.add(Kit.WARRIOR);
			kits.add(Kit.BLACKSMITH);
			kits.add(Kit.MINER);
		}
	},
	ELF(Material.BOW, 7500){
		{
			lore.add("You are elvish.");
			lore.add("");
			lore.add("You are the legendary gracefull");
			lore.add("civilization, rumoured to live up");
			lore.add("to 700 years. Your civilization");
			lore.add("it ancient and advanced in magic");
			lore.add("and weaponary.");
			lore.add("");
			lore.add("Your race's craftsmanship is");
			lore.add("unrivaled when it comes to wood.");
			
			kits.add(Kit.ARCHER);
		}
	},
	NONE(Material.BEDROCK, 0){
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
	int cost;
	
	/**
	 * 
	 * @param m
	 */
	Race(Material m, int cost){
		this.icon = new ItemStack(m);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(getName());
		icon.setItemMeta(meta);
		this.cost = cost;
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
	
	public int getCost(){
	    return this.cost;
	}
	
	public String getLetter(){
	    if(this == Race.DWARF){
	        return ChatColor.GRAY + "D";
	    }else if(this == Race.ELF){
	        return ChatColor.GREEN + "E";
	    }else if(this == Race.HUMAN){
	        return ChatColor.AQUA + "H";
	    }
	    
	    return ChatColor.WHITE + "L";
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
		return p.isOp() || p.hasPermission("fantasy.race." + getName().toLowerCase()) || this == Race.HUMAN;
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
		Inventory inv = Bukkit.createInventory(p, ((Race.values().length + 8) / 9) * 9, "Select Your Race");
		
		for(Race r : values()){
			if(r.icon.getType() == Material.BEDROCK){
				continue;
			}
			
			ItemStack item = r.icon.clone();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			lore.add(ChatColor.AQUA + "----------");
			
			if(r.doesPlayerOwnRace(p)){
				lore.add(ChatColor.GREEN + "Unlocked");
			}else{
				lore.add(ChatColor.RED + "Locked. Unlock");
				lore.add(ChatColor.RED + "with Royals");
			}
			
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		p.openInventory(inv);
	}
}
