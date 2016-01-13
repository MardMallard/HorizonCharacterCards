package com.gmail.Rhisereld.HorizonCharacterCards;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin 
{
	ConfigAccessor config;
	ConfigAccessor data;
	
	/**
	 * onEnable() is called when the server is started or the plugin is enabled.
	 * It should contain everything that the plugin needs for its initial setup.
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	public void onEnable()
	{
    	//Setup files for configuration and data storage.
    	config = new ConfigAccessor(this, "config.yml");
    	config.getConfig().options().copyDefaults(true);
    	data = new ConfigAccessor(this, "data.yml");
    	
    	//Write header.
    	config.getConfig().options().copyHeader(true);
    	
    	//Save configuration
    	config.saveDefaultConfig();
    	
    	//Listeners and commands.
        getServer().getPluginManager().registerEvents(new CardListener(config.getConfig(), data.getConfig()), this);
    	this.getCommand("card").setExecutor(new HorizonCommandParser(config.getConfig(), data.getConfig()));
    	
    	//Save every 30 minutes.
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run() 
			{
				data.saveConfig();
			}			
		} , 36000, 36000);
	}
	
	/**
     * onDisable() is called when the server shuts down or the plugin is disabled.
     * It should contain all the cleanup and data saving that the plugin needs to do before it is disabled.
     * 
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
	public void onDisable()
	{
		data.saveConfig();
		config = null;
		data = null;
	}
}
