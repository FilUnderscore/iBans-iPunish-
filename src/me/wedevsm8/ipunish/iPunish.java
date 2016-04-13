package me.wedevsm8.ipunish;

import java.io.File;

import me.wedevsm8.ipunish.command.CommandHandler;
import me.wedevsm8.ipunish.mysql.AsyncDatabaseUpdate;
import me.wedevsm8.ipunish.mysql.DatabaseManager;
import me.wedevsm8.ipunish.punishment.PunishmentDetector;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class iPunish 
	extends JavaPlugin
{
	private static File _pluginFolder;
	private static File _configFile;
	
	public void onEnable() 
	{
		loadConfig();
		
		new PunishmentDetector(this);
		
		new CommandHandler(this);
		
		new AsyncDatabaseUpdate("CREATE TABLE IF NOT EXISTS iPunish (player VARCHAR(36) NOT NULL, "
				+ "issuer VARCHAR(36) NOT NULL, reason VARCHAR(256), created LONG, "
				+ "removed BOOL, type VARCHAR(32), expiresOn LONG, remover VARCHAR(36), "
				+ "removeReason VARCHAR(256), id INT, PRIMARY KEY (id));");
	}
	
	public static File getPluginFolder()
	{
		return _pluginFolder;
	}
	
	public static File getConfigFile()
	{
		return _configFile;
	}
	
	private void loadConfig()
	{
		_pluginFolder = getDataFolder();
		_configFile = new File(_pluginFolder + File.separator + "config.yml");
		
		try
		{
			if(!(_configFile.exists()))
			{
				_pluginFolder.mkdirs();
				
				_configFile.createNewFile();
				
				FileConfiguration fc = YamlConfiguration.loadConfiguration(_configFile);
				
				fc.set("database-host", "localhost");
				fc.set("database-port", 3306);
				fc.set("database-user", "root");
				fc.set("database-pass", "password");
				fc.set("database", "default");
				
				fc.save(_configFile);
				
				System.err.println("(iPunish) ERROR loadConfig: Database settings not configured.");
				
				Bukkit.shutdown();
			}
			
			FileConfiguration fc = YamlConfiguration.loadConfiguration(_configFile);
			
			new DatabaseManager(fc.getString("database-host"), fc.getString("database-user"), fc.getString("database-pass"), fc.getString("database"), fc.getInt("database-port"));
		}
		catch(Exception exception)
		{
			System.err.println("(iPunish) ERROR loadConfig: " + exception.getMessage());
			
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
}