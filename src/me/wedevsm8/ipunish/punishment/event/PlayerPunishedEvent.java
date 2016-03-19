package me.wedevsm8.ipunish.punishment.event;

import me.wedevsm8.ipunish.punishment.Punishment;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPunishedEvent 
	extends Event
{
	private final static HandlerList handlers = new HandlerList();
	
	private Punishment _punishment;
	
	public PlayerPunishedEvent(Punishment punishment)
	{
		this._punishment = punishment;
	}
	
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	public Punishment getPunishment()
	{
		return this._punishment;
	}
}