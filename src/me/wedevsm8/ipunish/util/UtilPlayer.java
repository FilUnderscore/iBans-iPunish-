package me.wedevsm8.ipunish.util;

import me.wedevsm8.ipunish.punishment.PunishmentType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class UtilPlayer 
{
	public static void sendMessage(Permission permission, PunishmentType type, String reason, Player issuer, Player player)
	{
		for(Player players : Bukkit.getOnlinePlayers())
		{
			if(players.hasPermission(permission))
			{
				switch(type)
				{
					case KICK:
						players.sendMessage(ChatColor.GOLD + "(iPunish) " + ChatColor.AQUA + issuer.getName() + " issued a kick to " + player.getName() + ".");
					case WARN:
						players.sendMessage(ChatColor.GOLD + "(iPunish) " + ChatColor.AQUA + issuer.getName() + " issued a warning to " + player.getName() + ".");
					case REMOVED:
						
					default:
						players.sendMessage(ChatColor.GOLD + "(iPunish) " + ChatColor.AQUA + issuer.getName() + " issued a punishment to " + player.getName() + ".");
						players.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("(iPunish) ")
											.append(ChatColor.AQUA).append(ChatColor.BOLD).append("Reason: ").append(ChatColor.RESET)
											.append(ChatColor.AQUA).append(reason).toString());
				}
			}
		}
	}
}