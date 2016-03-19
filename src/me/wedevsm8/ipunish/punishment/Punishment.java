package me.wedevsm8.ipunish.punishment;

import java.util.UUID;

import me.wedevsm8.ipunish.punishment.event.PlayerPunishEvent;
import me.wedevsm8.ipunish.punishment.event.PlayerPunishmentExpiredEvent;
import me.wedevsm8.ipunish.punishment.event.PlayerPunishmentIssuedEvent;
import me.wedevsm8.ipunish.punishment.event.PlayerPunishmentRemovedEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Punishment 
{
	private UUID _player;
	private UUID _issuer;
	private String _reason;
	private long _created;
	private boolean _removed;
	private PunishmentType _type;
	
	private UUID _remover;
	private String _removeReason;
	
	public Punishment(UUID player, UUID issuer, String reason, long created, boolean removed, PunishmentType type) 
	{
		this._player = player;
		this._issuer = issuer;
		this._reason = reason;
		this._created = created;
		this._removed = removed;
		this._type = type;
	}
	
	public Punishment(UUID player, UUID issuer, String reason, long created, boolean removed, PunishmentType type, long time) 
	{
		this._player = player;
		this._issuer = issuer;
		this._reason = reason;
		this._created = created;
		this._removed = removed;
		this._type = type;
		
		this._type.setTime(time);
	}
	
	public void issue()
	{
		PlayerPunishEvent punishEvent = new PlayerPunishEvent(this);
		
		Bukkit.getPluginManager().callEvent(punishEvent);
		
		if(punishEvent.isCancelled())
			return;
		
		Bukkit.getPluginManager().callEvent(new PlayerPunishmentIssuedEvent(this));
	}
	
	public void remove(UUID remover, String reason)
	{
		if((this.hasExpired()) || (this.wasRemoved()))
		{
			return;
		}
		
		Bukkit.getPluginManager().callEvent(new PlayerPunishmentRemovedEvent(this));
		
		this._removed = true;
		
		this._remover = remover;
		this._removeReason = reason;
	}
	
	public UUID getPlayerUUID()
	{
		return this._player;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(this._player);
	}
	
	public UUID getIssuerUUID()
	{
		return this._issuer;
	}
	
	public Player getIssuer()
	{
		return Bukkit.getPlayer(this._issuer);
	}
	
	public String getReason()
	{
		return this._reason;
	}
	
	public long getCreationTime()
	{
		return this._created;
	}
	
	public boolean wasRemoved()
	{
		return this._removed;
	}
	
	public PunishmentType getPunishmentType()
	{
		return this._type;
	}
	
	public boolean hasExpired()
	{
		if(this._type.hasExpired())
		{
			Bukkit.getPluginManager().callEvent(new PlayerPunishmentExpiredEvent(this));
			
			return true;
		}
		
		return false;
	}
	
	public UUID getRemoverUUID()
	{
		return this._remover;
	}
	
	public Player getRemover()
	{
		return Bukkit.getPlayer(this._remover);
	}
	
	public String getRemoveReason()
	{
		return this._removeReason;
	}
}