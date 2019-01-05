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
package me.waltster.Fantasy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import me.waltster.Fantasy.FantasyMain;
import me.waltster.Fantasy.Util;
import net.md_5.bungee.api.ChatColor;

public class CityJoinSignManager {
	private FantasyMain main;
	/**
	 * Key: City name
	 * Value: Arraylist of locations for join signs. One city can have multiple signs.
	 */
	private HashMap<String, ArrayList<Location>> signs;
	
	/**
	 * 
	 * @param main
	 */
	public CityJoinSignManager(FantasyMain main){
		this.main = main;
		this.signs = new HashMap<String, ArrayList<Location>>();
	}
	
	/**
	 * 
	 */
	public void loadCitySigns(){
		List<String> activeCities = main.getConfigManager().getConfiguration("maps.yml").getConfig().getStringList("cities.activeCities");
		
		if(activeCities == null){
			throw new IllegalStateException("Cannot load cities list");
		}
		
		for(String city : activeCities){
			ConfigurationSection section = main.getConfigManager().getConfiguration("maps.yml").getConfig().getConfigurationSection("cities." + city);
			
			signs.put(city, new ArrayList<Location>());


			for(String signLoc : section.getStringList("signs")){
				Location loc = Util.parseLocation(signLoc);
				
				if(loc != null){
					addSign(city, loc);
				}else{
					main.getLogger().warning("Missing location for sign");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param cityName
	 * @param location
	 */
	public void addSign(String cityName, Location location){
		Block block = location.getBlock();
		
		if(block.getType() == null || block.getType() == Material.AIR){
			main.getLogger().warning("Unable to set city join sign because the block was not found.");
			return;
		}else if (block.getType() == Material.SIGN|| block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN){
			this.signs.get(cityName).add(location);
			updateSigns(cityName);
		}
	}
	
	/**
	 * 
	 * @param cityName
	 */
	public void updateSigns(String cityName){
		for(Location location : signs.get(cityName)){
			Block block = location.getBlock();
			
			if(block == null){
				continue;
			}
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
				Sign sign = (Sign)block.getState();
				
				sign.setLine(0, ChatColor.WHITE + "[" + ChatColor.GOLD + "City" + ChatColor.WHITE + "]");
                sign.setLine(1, ChatColor.GOLD + cityName);
                
				String race = main.getConfigManager().getConfiguration("maps.yml").getConfig().getString("cities." + cityName + ".race").toUpperCase();
				sign.setLine(2, ChatColor.GOLD + race);
				sign.setLine(3, ChatColor.BOLD + "" + ChatColor.GOLD + "Click to Join!");
				sign.update(true);
			}
		}
	}
	
	/**
	 * 
	 */
	public void updateAllSigns(){
		for(String s : signs.keySet()){
			updateSigns(s);
		}
	}
}
