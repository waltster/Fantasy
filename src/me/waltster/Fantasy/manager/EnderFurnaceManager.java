package me.waltster.Fantasy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryFurnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Util;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.TileEntityFurnace;

public class EnderFurnaceManager {
	/**
	 * 
	 * @author walt
	 *
	 */
	private class VirtualFurnace extends TileEntityFurnace {
		public VirtualFurnace(EntityPlayer entity) {
			world = entity.world;
		}
		
		public InventoryHolder getOwner() {
			return new InventoryHolder() {
				public Inventory getInventory() {
					return new CraftInventoryFurnace(VirtualFurnace.this);
				}
			};
		}
	}
	
	private ArrayList<String> locations;
	private HashMap<String, VirtualFurnace> furnaces;
	private FantasyMain instance;
	
	public EnderFurnaceManager(FantasyMain main) {
		this.instance = main;
		
		locations = new ArrayList<String>();
		furnaces = new HashMap<String, VirtualFurnace>();
		
		Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
			public void run() {
				for(VirtualFurnace f : furnaces.values()) {
					try {
						f.c(0, 0);
					}catch(Exception e) {
						
					}
				}
			}
		}, 0L, 1L);
	}
	
	/**
	 * 
	 * @param location
	 */
	public void addEnderFurnace(Location location) {
		locations.add(location.toString());
	}
	
	/**
	 * 
	 */
	public void loadEnderFurnaces() {
		List<String> furnaces = instance.getConfigManager().getConfiguration("maps.yml").getConfig().getStringList("enderFurnaces");
		
		if(furnaces == null) {
			instance.getLogger().warning("Launching without Ender Furnaces configured.");
			return;
		}
		
		for(String furnace : furnaces) {
			Location location = Util.parseLocation(furnace);
			
			if(location != null) {
				this.addEnderFurnace(location);
			}
		}
	}
	
	public ArrayList<String> getFurnaceLocations(){
		return this.locations;
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	public VirtualFurnace getFurnace(Player player) {
		if(!furnaces.containsKey(player.getName())) {
			EntityPlayer handle = ((CraftPlayer)player).getHandle();
			furnaces.put(player.getName(), new VirtualFurnace(handle));
		}
		
		return furnaces.get(player.getName());
	}
}
