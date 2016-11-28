package me.waltster.Fantasy.manager;

import java.io.File;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import me.waltster.Fantasy.FantasyMain;

public class ConfigManager {
	/**
	 * 
	 * @author walt
	 *
	 */
	public static class Configuration{
		private File configFile;
		private YamlConfiguration config;
		
		/**
		 * 
		 * @param configFile
		 */
		public Configuration(File configFile){
			this.configFile = configFile;
			config = new YamlConfiguration();
			load();
		}
		
		/**
		 * 
		 */
		public void load(){
			try{
				config.load(configFile);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/**
		 * 
		 */
		public void save(){
			try{
				config.save(configFile);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public YamlConfiguration getConfig(){
			return this.config;
		}
	}
	
	private final FantasyMain main;
	private final File configurationFolder;
	private final TreeMap<String, Configuration> configurations;
	
	/**
	 * 
	 * @param main
	 */
	public ConfigManager(FantasyMain main){
		this.main = main;
		this.configurationFolder = main.getDataFolder();
		this.configurationFolder.mkdir();
		this.configurations = new TreeMap<String, Configuration>(String.CASE_INSENSITIVE_ORDER);
	}

	/**
	 * 
	 * @param name
	 */
	public void loadConfiguration(String name){
		File file = new File(this.configurationFolder, name);
		
		if(!file.exists()){
			main.getLogger().log(Level.WARNING, "Missing configuration for: " + name + ", while trying to load.");
			return;
		}
		
		Configuration config = new Configuration(file);
		config.load();
		configurations.put(name, config);
		main.getLogger().log(Level.INFO, "Loaded configuration: " + name);
	}
	
	/**
	 * 
	 * @param names
	 */
	public void loadConfigurations(String... names){
		for(String name : names){
			File file = new File(this.configurationFolder, name);
			
			if(!file.exists()){
				main.getLogger().log(Level.WARNING, "Missing configuration for: " + name + ", while trying to load.");
				continue;
			}
			
			Configuration config = new Configuration(file);
			config.load();
			configurations.put(name, config);
			main.getLogger().log(Level.INFO, "Loaded configuration: " + name);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Configuration getConfiguration(String name){
		Configuration config = configurations.get(name);
		
		if(config == null){
			loadConfiguration(name);
		}
		
		return configurations.get(name);
	}
}
