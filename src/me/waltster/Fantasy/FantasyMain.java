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
import me.waltster.Fantasy.listener.PlayerListener;
import me.waltster.Fantasy.listener.ResourceListener;
import me.waltster.Fantasy.listener.SoulboundListener;
import me.waltster.Fantasy.listener.WorldListener;
import me.waltster.Fantasy.manager.CityCaptureSignManager;
import me.waltster.Fantasy.manager.CityJoinSignManager;
import me.waltster.Fantasy.manager.ConfigManager;
import me.waltster.Fantasy.manager.StatsManager;

public class FantasyMain extends JavaPlugin{
	private Location lobbySpawnLocation;
	private ConfigManager configManager;
	private PluginManager pluginManager;
	private CityJoinSignManager citySignManager;
	private CityCaptureSignManager cityCaptureSignManager;
	private ChatListener chatListener;
	private ClassAbilityListener classAbilityListener;
	private PlayerListener playerListener;
	private SoulboundListener soulboundListener;
	private WorldListener worldListener;
	private ResourceListener resourceListener;
	private World currentMap;
	private StatsManager statsManager;
	private ItemMessage messageManager;
	
	@Override
	public void onEnable(){
		this.getLogger().info("Fantasy version 0.5 by the_waltster enabled");
		this.getLogger().info("(C) Walter Pach 2016-2017, all rights reserved");
		
		this.pluginManager = this.getServer().getPluginManager();
		this.configManager = new ConfigManager(this);
		this.citySignManager = new CityJoinSignManager(this);
		this.cityCaptureSignManager = new CityCaptureSignManager(this);
		this.statsManager = new StatsManager(this, this.configManager);
		this.messageManager = new ItemMessage(this);
		
		this.chatListener = new ChatListener();
		this.classAbilityListener = new ClassAbilityListener(this);
		this.playerListener = new PlayerListener(this);
		this.soulboundListener = new SoulboundListener();
		this.worldListener = new WorldListener(this);
		this.resourceListener = new ResourceListener(this);
		
		this.configManager.loadConfigurations("config.yml", "maps.yml", "messages.yml");
		this.pluginManager.registerEvents(chatListener, this);
		this.pluginManager.registerEvents(classAbilityListener, this);
		this.pluginManager.registerEvents(playerListener, this);
		this.pluginManager.registerEvents(soulboundListener, this);
		this.pluginManager.registerEvents(worldListener, this);
		this.pluginManager.registerEvents(resourceListener, this);
		
		this.getLogger().info("Event listeners registered");
		
		this.lobbySpawnLocation = Util.parseLocation(this.configManager.getConfiguration("maps.yml").getConfig().getString("lobby.spawn"));
		this.currentMap = Bukkit.getWorld(this.configManager.getConfiguration("config.yml").getConfig().getString("map"));
		
		if(lobbySpawnLocation == null) throw new IllegalStateException("Null result when reading lobby spawn from maps.yml");
		if(currentMap == null) throw new IllegalStateException("Null result when reading map name from config.yml");

		this.citySignManager.loadCitySigns();
		getCommand("help").setExecutor(new CommandHelp());
		getCommand("die").setExecutor(new CommandDie(this));
		getCommand("race").setExecutor(new CommandRace());
		getCommand("class").setExecutor(new CommandClass());
		getCommand("buyclass").setExecutor(new CommandClassBuy());
		getCommand("buyrace").setExecutor(new CommandRaceBuy());
		getCommand("stats").setExecutor(new CommandStats(this.statsManager));
		getCommand("fartp").setExecutor(new CommandTP());
	}
	
	@Override
	public void onDisable(){
		this.getLogger().info("Saving configurations");
		
		this.configManager.getConfiguration("config.yml").save();
		this.configManager.getConfiguration("maps.yml").save();
		this.configManager.getConfiguration("stats.yml").save();
	}
	
	public StatsManager getStatsManager(){
	    return this.statsManager;
	}
	
	public ConfigManager getConfigManager(){
		return this.configManager;
	}
	
	public CityJoinSignManager getCityJoinSignManager(){
	    return this.citySignManager;
	}
	
	public CityCaptureSignManager getCityCaptureSignManager(){
	    return this.cityCaptureSignManager;
	}
	
	public Location getLobbySpawn(){
		return this.lobbySpawnLocation;
	}
	
	public World getMap(){
		return this.currentMap;
	}
	
	public ItemMessage getItemMessageManager(){
	    return this.messageManager;
	}
}
