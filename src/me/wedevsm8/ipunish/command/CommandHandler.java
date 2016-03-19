package me.wedevsm8.ipunish.command;

import java.util.List;

import me.wedevsm8.ipunish.client.PunishClient;
import me.wedevsm8.ipunish.client.PunishFactory;
import me.wedevsm8.ipunish.punishment.Punishment;
import me.wedevsm8.ipunish.punishment.PunishmentType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler 
	implements Listener
{
	private JavaPlugin _plugin;
	
	public CommandHandler(JavaPlugin plugin)
	{
		this._plugin = plugin;
		
		this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void CommandPreprocess(final PlayerCommandPreprocessEvent event)
	{
		String command = event.getMessage().substring(1);
		
		String[] commandArgs = null;
		
		if(command.contains(" "))
		{
			command = command.split(" ")[0];
			commandArgs = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
		}
		
		if((command.equalsIgnoreCase("p")) || (command.equalsIgnoreCase("punish")))
		{
			if(event.getPlayer().hasPermission("ipunish.use"))
			{
				if(commandArgs != null)
				{
					if(commandArgs.length < 2)
					{
						event.getPlayer().sendMessage(ChatColor.GOLD + "[Punish] " + ChatColor.RED + "No permission to use this command.");
						
						return;
					}
					
					String reason = "";
					
					for(int i = 1; i < commandArgs.length; i++)
					{
						reason = commandArgs[i] + " ";
					}

					final Punishment punishment = new Punishment(event.getPlayer().getUniqueId(), event.getPlayer().getUniqueId(), "Test Punishment", System.currentTimeMillis(), false, PunishmentType.MUTE, -1L);
					
					final PunishClient punishClient = PunishFactory.createClient(event.getPlayer().getUniqueId());
					
					final List<Punishment> punishments = punishClient.retrievePunishmentsList();
					
					final String player = commandArgs[0];

					System.out.println("(iPunish) DEBUG CommandHandler.CommandPreprocess: Initializing While Loop");
					
					if(punishClient.resultsRetrieved())
					{
						System.out.println("(iPunish) DEBUG CommandHandler.CommandPreprocess: Retrieved Punishments.");
							
						punishClient.issuePunishment(punishment);
					}
					
					punishments.add(punishment);
					
					PunishFactory.openPunishGUI(event.getPlayer(), Bukkit.getPlayer(player), "Test", punishments);
				}
			}
			
			event.setCancelled(true);
		}
	}
}
