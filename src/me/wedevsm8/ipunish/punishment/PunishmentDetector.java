package me.wedevsm8.ipunish.punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.wedevsm8.ipunish.client.PunishClient;
import me.wedevsm8.ipunish.client.PunishFactory;
import me.wedevsm8.ipunish.util.UtilTime;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PunishmentDetector 
	implements Listener
{
	private JavaPlugin _plugin;
	
	private List<UUID> _silencedList;
	
	public PunishmentDetector(JavaPlugin plugin)
	{
		this._plugin = plugin;
		
		this._silencedList = new ArrayList<UUID>();
		
		this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void AsyncLogin(AsyncPlayerPreLoginEvent event)
	{
		PunishClient punishClient = PunishFactory.createClient(event.getUniqueId());
		
		punishClient.retrievePunishments();
		
		if(punishClient.getLatestPunishment() != null)
		{
			Punishment currentPunishment = punishClient.getLatestPunishment();
			
			if((currentPunishment.getPunishmentType() == PunishmentType.BAN) && (!(currentPunishment.hasExpired())))
			{
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You are banned for " + UtilTime.convertString(currentPunishment.getPunishmentType().getRawTime())).append("\n").append(ChatColor.RESET).append(currentPunishment.getReason()).toString());
				
				return;
			}
			else if((currentPunishment.getPunishmentType() == PunishmentType.MUTE) && (!(currentPunishment.hasExpired())))
			{
				this._silencedList.add(event.getUniqueId());
			}
			
			event.allow();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void AsyncChat(AsyncPlayerChatEvent event)
	{
		if(this._silencedList.contains(event.getPlayer().getUniqueId()))
		{
			PunishClient punishClient = PunishFactory.createClient(event.getPlayer().getUniqueId());
			
			event.getPlayer().sendMessage(ChatColor.GOLD + "[iPunish] " + ChatColor.AQUA + "Shhh. You have been muted by " + punishClient.getLatestPunishment().getIssuer().getName() + " for " + punishClient.getLatestPunishment().getReason() + ".");
			event.getPlayer().sendMessage(ChatColor.BOLD + "[iPunish] " + ChatColor.AQUA + "You are muted for " + ChatColor.GREEN + UtilTime.convertString(punishClient.getLatestPunishment().getPunishmentType().getRawTime()));
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Disconnect(PlayerQuitEvent event)
	{
		PunishClient punishClient = PunishFactory.createClient(event.getPlayer().getUniqueId());
		
		PunishFactory.removeClient(punishClient);
		
		punishClient.destroy();
	}
}