package me.waltster.Fantasy;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.waltster.Fantasy.command.CommandHelp;
import me.waltster.Fantasy.command.CommandRestart;
import me.waltster.Fantasy.listener.ChatListener;
import me.waltster.Fantasy.listener.ClassAbilityListener;
import me.waltster.Fantasy.listener.PlayerListener;
import me.waltster.Fantasy.listener.SoulboundListener;
import me.waltster.Fantasy.listener.WorldListener;
import me.waltster.Fantasy.manager.ConfigManager;

public class FantasyMain extends JavaPlugin{
	private Location lobbySpawnLocation;
	private ConfigManager configManager;
	private PluginManager pluginManager;
	private ChatListener chatListener;
	private ClassAbilityListener classAbilityListener;
	private PlayerListener playerListener;
	private SoulboundListener soulboundListener;
	private WorldListener worldListener;
	private World currentMap;
	
	@Override
	public void onEnable(){
		this.getLogger().info("Fantasy is enabling");
		this.getLogger().info("This is Fantasy version 0.1 by Walt Pach (the_waltster). Please do not distribute!");
		
		this.pluginManager = this.getServer().getPluginManager();
		this.chatListener = new ChatListener();
		this.classAbilityListener = new ClassAbilityListener();
		this.playerListener = new PlayerListener(this);
		this.soulboundListener = new SoulboundListener();
		this.worldListener = new WorldListener();
		this.configManager = new ConfigManager(this);
		
		this.configManager.loadConfigurations("config.yml", "maps.yml");
		this.pluginManager.registerEvents(chatListener, this);
		this.pluginManager.registerEvents(classAbilityListener, this);
		this.pluginManager.registerEvents(playerListener, this);
		this.pluginManager.registerEvents(soulboundListener, this);
		this.pluginManager.registerEvents(worldListener, this);
		this.getLogger().info("Event listeners registered");
		
		this.lobbySpawnLocation = Util.parseLocation(this.configManager.getConfiguration("maps.yml").getConfig().getString("lobby.spawn"));
		this.currentMap = Bukkit.getWorld(this.configManager.getConfiguration("config.yml").getConfig().getString("map"));
		
		if(lobbySpawnLocation == null){
			this.getLogger().log(Level.SEVERE, "Error while reading lobby spawn from maps.yml");
		}
		if(currentMap == null){
			this.getLogger().log(Level.SEVERE, "Error while reading map name from config.yml");
		}
		
		getCommand("help").setExecutor(new CommandHelp());
		getCommand("restart").setExecutor(new CommandRestart(this));
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
