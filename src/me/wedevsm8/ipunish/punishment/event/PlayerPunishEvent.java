package me.wedevsm8.ipunish.punishment.event;

import me.wedevsm8.ipunish.punishment.Punishment;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPunishEvent 
	extends Event implements Cancellable
{
	private final static HandlerList handlers = new HandlerList();
	
	private Punishment _punishment;
	private boolean _cancelled = false;
	
	public PlayerPunishEvent(Punishment punishment)
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
	
	public boolean isCancelled()
	{
		return this._cancelled;
	}
	
	public void setCancelled(boolean cancel)
	{
		this._cancelled = cancel;
	}
}