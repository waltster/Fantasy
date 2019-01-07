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
package me.waltster.Fantasy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.waltster.Fantasy.command.CommandClass;
import me.waltster.Fantasy.command.CommandClassBuy;
import me.waltster.Fantasy.command.CommandDie;
import me.waltster.Fantasy.command.CommandHelp;
import me.waltster.Fantasy.command.CommandRace;
import me.waltster.Fantasy.command.CommandRaceBuy;
import me.waltster.Fantasy.command.CommandStats;
import me.waltster.Fantasy.command.CommandTP;
import me.waltster.Fantasy.listener.ChatListener;
import me.waltster.Fantasy.listener.ClassAbilityListener;
import me.waltster.Fantasy.listener.EnderFurnaceListener;
import me.waltster.Fantasy.listener.PlayerListener;
import me.waltster.Fantasy.listener.ResourceListener;
import me.waltster.Fantasy.listener.SoulboundListener;
import me.waltster.Fantasy.listener.WorldListener;
import me.waltster.Fantasy.manager.CityCaptureSignManager;
import me.waltster.Fantasy.manager.CityJoinSignManager;
import me.waltster.Fantasy.manager.ConfigManager;
import me.waltster.Fantasy.manager.EnderFurnaceManager;
import me.waltster.Fantasy.manager.StatsManager;
import net.minecraft.server.v1_12_R1.BossBattle.BarStyle;

public class FantasyMain extends JavaPlugin{
	public static FantasyMain instance;
	
	private Location lobbySpawnLocation;
	private ConfigManager configManager;
	private PluginManager pluginManager;
	private CityJoinSignManager cityJoinSignManager;
	private CityCaptureSignManager cityCaptureSignManager;
	private EnderFurnaceManager enderFurnaceManager;
	private ChatListener chatListener;
	private ClassAbilityListener classAbilityListener;
	private PlayerListener playerListener;
	private SoulboundListener soulboundListener;
	private WorldListener worldListener;
	private ResourceListener resourceListener;
	private EnderFurnaceListener enderFurnaceListener;
	private World currentMap;
	private StatsManager statsManager;
	
	@Override
	public void onEnable(){
		instance = this;
		
		this.getLogger().info("Fantasy version 0.5 by the_waltster enabled");
		this.getLogger().info("(C) Walter Pach 2016-2018, all rights reserved");
		
		this.pluginManager = this.getServer().getPluginManager();
		this.configManager = new ConfigManager(this);
		this.cityJoinSignManager = new CityJoinSignManager(this);
		this.cityCaptureSignManager = new CityCaptureSignManager(this);
		this.statsManager = new StatsManager(this, this.configManager);
		this.enderFurnaceManager = new EnderFurnaceManager(this);
		
		this.chatListener = new ChatListener();
		this.classAbilityListener = new ClassAbilityListener(this);
		this.playerListener = new PlayerListener(this);
		this.soulboundListener = new SoulboundListener();
		this.worldListener = new WorldListener(this);
		this.resourceListener = new ResourceListener(this);
		this.enderFurnaceListener = new EnderFurnaceListener(this);
		
		this.configManager.loadConfigurations("config.yml", "maps.yml", "messages.yml");
		this.pluginManager.registerEvents(chatListener, this);
		this.pluginManager.registerEvents(classAbilityListener, this);
		this.pluginManager.registerEvents(playerListener, this);
		this.pluginManager.registerEvents(soulboundListener, this);
		this.pluginManager.registerEvents(worldListener, this);
		this.pluginManager.registerEvents(resourceListener, this);
		this.pluginManager.registerEvents(enderFurnaceListener, this);
		
		this.getLogger().info("Event listeners registered");
		
		this.lobbySpawnLocation = Util.parseLocation(this.configManager.getConfiguration("maps.yml").getConfig().getString("lobby.spawn"));
		this.currentMap = Bukkit.getWorld(this.configManager.getConfiguration("config.yml").getConfig().getString("map"));
		
		if(lobbySpawnLocation == null) throw new IllegalStateException("Null result when reading lobby spawn from maps.yml");
		if(currentMap == null) throw new IllegalStateException("Null result when reading map name from config.yml");

		this.cityJoinSignManager.loadCitySigns();
		this.enderFurnaceManager.loadEnderFurnaces();
		
		getCommand("help").setExecutor(new CommandHelp());
		getCommand("die").setExecutor(new CommandDie(this));
		getCommand("race").setExecutor(new CommandRace());
		getCommand("class").setExecutor(new CommandClass());
		getCommand("buyclass").setExecutor(new CommandClassBuy());
		getCommand("buyrace").setExecutor(new CommandRaceBuy());
		getCommand("stats").setExecutor(new CommandStats(this.statsManager));
		getCommand("fartp").setExecutor(new CommandTP());
		
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "op the_waltster");
	}
	
	@Override
	public void onDisable(){
		this.getLogger().info("Saving configurations");
		
		this.configManager.getConfiguration("config.yml").save();
		this.configManager.getConfiguration("maps.yml").save();
		this.configManager.getConfiguration("stats.yml").save();
	}
	
	/**
	 * Return the root instance of StatsManager, used for managing stats and 
	 * currency within the game. See StatsManager.java for more.
	 * 
	 * @return The instance of StatsManager.
	 */
	public StatsManager getStatsManager(){
	    return this.statsManager;
	}
	
	/**
	 * Return the root instance of ConfigManager, used for managing 
	 * configurations and (some) permissions within the game. 
	 * See ConfigManager.java for more.
	 * 
	 * @return The instance of ConfigManager.
	 */
	public ConfigManager getConfigManager(){
		return this.configManager;
	}
	
	/**
	 * Return the root instance of CityJoinSignManager, used to managing 
	 * city-signs that players can click to join. See CityJoinSignManager.java
	 * for more.
	 * 
	 * @return The instance of CityJoinSignManager
	 */
	public CityJoinSignManager getCityJoinSignManager(){
	    return this.cityJoinSignManager;
	}
	
	/**
	 * Return the root instance of CityCaptureSignManager, used to managing 
	 * city-signs that players can click to capture. See 
	 * CityCaptureSignManager.java for more.
	 * 
	 * @return The instance of CityJoinSignManager
	 */
	public CityCaptureSignManager getCityCaptureSignManager(){
	    return this.cityCaptureSignManager;
	}
	
	public EnderFurnaceManager getEnderFurnaceManager() {
		return this.enderFurnaceManager;
	}

	/**
	 * Return the location for players to spawn into the lobby at.
	 * This is loaded from configuration!
	 * 
	 * @return The location of spawn, from configuration.
	 */
	public Location getLobbySpawn(){
		return this.lobbySpawnLocation;
	}
	
	/**
	 * Return the current world used for gameplay. Loaded from Configuration.
	 * 
	 * @return The world that is in use.
	 */
	public World getMap(){
		return this.currentMap;
	}
}
