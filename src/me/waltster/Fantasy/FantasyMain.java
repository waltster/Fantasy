package me.waltster.Fantasy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.waltster.Fantasy.command.CommandClass;
import me.waltster.Fantasy.command.CommandDie;
import me.waltster.Fantasy.command.CommandHelp;
import me.waltster.Fantasy.command.CommandRace;
import me.waltster.Fantasy.listener.ChatListener;
import me.waltster.Fantasy.listener.ClassAbilityListener;
import me.waltster.Fantasy.listener.PlayerListener;
import me.waltster.Fantasy.listener.SoulboundListener;
import me.waltster.Fantasy.listener.WorldListener;
import me.waltster.Fantasy.manager.CitySignManager;
import me.waltster.Fantasy.manager.ConfigManager;

public class FantasyMain extends JavaPlugin{
	private Location lobbySpawnLocation;
	private ConfigManager configManager;
	private PluginManager pluginManager;
	private CitySignManager citySignManager;
	private ChatListener chatListener;
	private ClassAbilityListener classAbilityListener;
	private PlayerListener playerListener;
	private SoulboundListener soulboundListener;
	private WorldListener worldListener;
	private World currentMap;
	
	@Override
	public void onEnable(){
		this.getLogger().info("Fantasy version 0.1 by the_waltster enabled");
		this.getLogger().info("(C) Walter Pach 2016, all rights reserved");
		
		this.pluginManager = this.getServer().getPluginManager();
		this.configManager = new ConfigManager(this);
		this.citySignManager = new CitySignManager(this);
		
		this.chatListener = new ChatListener();
		this.classAbilityListener = new ClassAbilityListener(this);
		this.playerListener = new PlayerListener(this);
		this.soulboundListener = new SoulboundListener();
		this.worldListener = new WorldListener(this);
		
		this.configManager.loadConfigurations("config.yml", "maps.yml", "messages.yml");
		this.pluginManager.registerEvents(chatListener, this);
		this.pluginManager.registerEvents(classAbilityListener, this);
		this.pluginManager.registerEvents(playerListener, this);
		this.pluginManager.registerEvents(soulboundListener, this);
		this.pluginManager.registerEvents(worldListener, this);
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
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public ConfigManager getConfigManager(){
		return this.configManager;
	}
	
	public Location getLobbySpawn(){
		return this.lobbySpawnLocation;
	}
	
	public World getMap(){
		return this.currentMap;
	}
}
