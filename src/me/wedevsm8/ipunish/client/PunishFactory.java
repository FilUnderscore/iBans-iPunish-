package me.wedevsm8.ipunish.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.wedevsm8.ipunish.punishment.Punishment;
import me.wedevsm8.ipunish.util.UtilItem;
import me.wedevsm8.ipunish.util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class PunishFactory 
{
	private static Map<UUID, PunishClient> _clientMap = new HashMap<UUID, PunishClient>();
	
	public static PunishClient createClient(UUID uuid)
	{
		if(_clientMap == null)
			_clientMap = new HashMap<>();
		
		if(!_clientMap.containsKey(uuid))
		{
			PunishClient punishClient = new PunishClient(uuid);
			
			_clientMap.put(uuid, punishClient);
			
			return punishClient;
		}
		
		return null;
	}
	
	public static PunishClient getClient(Player player)
	{
		if(_clientMap.containsKey(player.getUniqueId()))
			return _clientMap.get(player.getUniqueId());
		
		return null;
	}
	
	public static boolean removeClient(PunishClient punishClient)
	{
		if(punishClient == null)
			return false;
		
		if(_clientMap == null)
			return false;
		
		if(punishClient.getPlayer() == null)
			return false;
		
		if(!(_clientMap.containsKey(punishClient.getPlayer().getUniqueId())))
			return false;
		
		_clientMap.remove(punishClient.getPlayer().getUniqueId());
		
		punishClient.destroy();
		
		return true;
	}
	
	@SuppressWarnings("unused")
	public static void openPunishGUI(Player issuer, final Player target, String reason, List<Punishment> punishments)
	{
		PunishClient punishClient = createClient(target.getUniqueId());
		
		final Inventory punishInventory = Bukkit.createInventory(null, 54, "Punishing " + target.getName());
		
		ItemStack muteOffense = new ItemStack(Material.BOOK_AND_QUILL, 1);
		ItemStack warning = new ItemStack(Material.PAPER, 1);
		ItemStack hackOffense = new ItemStack(Material.IRON_SWORD, 1);
		ItemStack offense = new ItemStack(Material.HOPPER, 1);
		
		ItemStack player = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		
		UtilItem.setHeadOwner(player, target.getName());
		UtilItem.setDisplayNameAndLore(player, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(target.getName()).toString(), "");
		
		UtilItem.setDisplayNameAndLore(muteOffense, ChatColor.RESET + "Chat Offense", new StringBuilder().append(ChatColor.RESET).append(ChatColor.GRAY).append("Mute category for Chat Offenses.").toString());
		UtilItem.setDisplayNameAndLore(warning, ChatColor.RESET + "Warning", new StringBuilder().append(ChatColor.RESET).append(ChatColor.GRAY).append("Issue a warning to the player.").toString());
		UtilItem.setDisplayNameAndLore(hackOffense, ChatColor.RESET + "Hacking Offense", new StringBuilder().append(ChatColor.RESET).append(ChatColor.GRAY).append("Hack category for Hacking Offenses.").toString());
		UtilItem.setDisplayNameAndLore(offense, ChatColor.RESET + "General Offense", new StringBuilder().append(ChatColor.RESET).append(ChatColor.GRAY).append("Category for any offense.").toString());
		
		punishInventory.setItem(4, player);
		
		punishInventory.setItem(11, muteOffense);
		punishInventory.setItem(13, offense);
		punishInventory.setItem(15, hackOffense);
		
		punishInventory.setItem(25, warning);
		
		//List<Punishment> punishments = UtilIterator.copyIterator(punishClient.retrievePunishments());
		
		//Iterator<Punishment> punishments = punishClient.retrievePunishments();
		
		int slot = 45;
		
		/*
		for(int i = 0; i <= punishments.; i++)
		{
			//Punishment punishment = punishments.get((punishments.size() - i));
			
			System.out.println("i = " + i + " | slot = " + slot);
			
			if(punishment != null)
			{
				ItemStack stack = getPunishmentStack(punishment);
				
				punishInventory.setItem(slot, stack);
				
				System.out.println("Placed Punishment " + punishment + " in Slot " + slot);
				
				if(slot < punishInventory.getSize())
				{
					slot++;
				}
			}
		}
		*/
		
		//System.out.println("(iPunish) DEBUG Running Thread 't'");
		
		/*
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				int slot = 45;
				
				System.out.println("(iPunish) Preparing Punishments for " + target.getUniqueId());
				
				while(punishments.hasNext())
				{
					Punishment punishment = punishments.next();
					
					punishments.remove();
					
					ItemStack stack = getPunishmentStack(punishment);
					
					punishInventory.setItem(slot, stack);
					
					System.out.println("Placed Punishment " + punishment + " in Slot " + slot);
					
					if(punishments.hasNext())
						slot++;
					else
					{
						break;
					}
				}
			}
		});
		
		t.start();
		*/
		
		/*
		for(Iterator<Punishment> iter = punishClient.retrievePunishments(); iter.hasNext();)
		{
			Punishment punishment = iter.next();
			
			ItemStack stack = getPunishmentStack(punishment);
			
			punishInventory.setItem(slot, stack);
			
			System.out.println("Placed Punishment " + punishment + " in Slot " + slot);
			
			slot++;
		}
		*/
		
		//while(punishments.size() == 0)
		//{
			if(punishments.size() != 0)
			{
				System.out.println("(iPunish) DEBUG PunishFactory.openPunishGUI: Size of Punishments List: " + punishments.size());
				
				for(Punishment punishment : punishments)
				{
					ItemStack stack = getPunishmentStack(punishment);
					
					punishInventory.setItem(slot, stack);
					
					System.out.println("Placed Punishment " + punishment + " in Slot " + slot);
					
					slot++;
				}
				
				//break;
			}
		//}
		
		issuer.openInventory(punishInventory);
	}
	
	private static ItemStack getPunishmentStack(Punishment punishment)
	{
		switch(punishment.getPunishmentType())
		{
			case BAN:
				if(punishment.getPunishmentType().getRawTime() == -1L)
				{
					if(!(punishment.hasExpired()))
					{
						ItemStack temp = new ItemStack(Material.REDSTONE_BLOCK, 1);
						
						UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
						"Permanent Ban").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Permanent Ban", ChatColor.RESET + 
						"Amount of Time: " + ChatColor.YELLOW + "Permanent", ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
						ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
						ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
						UtilTime.getTimeWhen(punishment.getCreationTime()));
					
						ItemStack stack = UtilItem.addGlowNameLore(temp, true, temp.getItemMeta().getDisplayName(), temp.getItemMeta().getLore());
						
						return stack;
					}
				}
				else if(!(punishment.hasExpired()))
				{
					ItemStack temp = new ItemStack(Material.HOPPER, 1);
					
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"General Offense").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "General Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()));
					
					ItemStack stack = UtilItem.addGlowNameLore(temp, true, temp.getItemMeta().getDisplayName(), temp.getItemMeta().getLore());
					
					return stack;
				}
				
				if(!(punishment.wasRemoved()))
				{
					ItemStack temp = new ItemStack(Material.HOPPER, 1);
				
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"General Offense").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "General Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()));
				}
				else
				{
					ItemStack temp = new ItemStack(Material.BOOK_AND_QUILL, 1);
					
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"General Offense").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "General Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()), ChatColor.RESET + "", ChatColor.RESET + "Removed by: " + 
					ChatColor.YELLOW + punishment.getRemover().getName(), 
					ChatColor.RESET + "Remove Reason: " + ChatColor.YELLOW + punishment.getRemoveReason());
					
					return temp;
				}
				
			case MUTE:
				if(punishment.getPunishmentType().getRawTime() == -1L)
				{
					if(!(punishment.hasExpired()))
					{
						ItemStack temp = new ItemStack(Material.BOOK_AND_QUILL, 1);
						
						UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
						"Permanent Mute").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Mute Offense", ChatColor.RESET + 
						"Amount of Time: " + ChatColor.YELLOW + "Permanent", ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
						ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
						ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
						UtilTime.getTimeWhen(punishment.getCreationTime()));
					
						ItemStack stack = UtilItem.addGlowNameLore(temp, true, temp.getItemMeta().getDisplayName(), temp.getItemMeta().getLore());
						
						return stack;
					}
				}
				else if(!(punishment.hasExpired()))
				{
					ItemStack temp = new ItemStack(Material.BOOK_AND_QUILL, 1);
					
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"Permanent Mute").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Mute Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()));
					
					ItemStack stack = UtilItem.addGlowNameLore(temp, true, temp.getItemMeta().getDisplayName(), temp.getItemMeta().getLore());
					
					return stack;
				}
				
				if(!(punishment.wasRemoved()))
				{
					ItemStack temp = new ItemStack(Material.BOOK_AND_QUILL, 1);
					
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"Permanent Mute").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Mute Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()));
				}
				else
				{
					ItemStack temp = new ItemStack(Material.BOOK_AND_QUILL, 1);
					
					UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
					"Permanent Mute").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Mute Offense", ChatColor.RESET + 
					"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
					ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
					ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
					UtilTime.getTimeWhen(punishment.getCreationTime()), ChatColor.RESET + "", ChatColor.RESET + "Removed by: " + ChatColor.YELLOW + punishment.getRemover().getName(), 
					ChatColor.RESET + "Remove Reason: " + ChatColor.YELLOW + punishment.getRemoveReason());
				}
				
			case WARN:
				ItemStack temp = new ItemStack(Material.PAPER, 1);
				
				UtilItem.setDisplayNameAndLore(temp, new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append(
				"Warning").toString(), ChatColor.RESET + "Punishment Type: " + ChatColor.YELLOW + "Warning", ChatColor.RESET + 
				"Amount of Time: " + ChatColor.YELLOW + UtilTime.convertString(punishment.getPunishmentType().getRawTime()), ChatColor.RESET + "", ChatColor.RESET + "Reason: " + 
				ChatColor.YELLOW + punishment.getReason(), ChatColor.RESET + "", ChatColor.RESET + "Issuer: " + 
				ChatColor.YELLOW + punishment.getIssuer().getName(), ChatColor.RESET + "Date: " + ChatColor.YELLOW + 
				UtilTime.getTimeWhen(punishment.getCreationTime()));
				
			default:
				return new ItemStack(Material.ANVIL, 1);
		}
	}
}