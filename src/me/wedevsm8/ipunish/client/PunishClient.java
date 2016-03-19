package me.wedevsm8.ipunish.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.wedevsm8.ipunish.mysql.AsyncDatabaseUpdate;
import me.wedevsm8.ipunish.mysql.DatabaseQuery;
import me.wedevsm8.ipunish.punishment.Punishment;
import me.wedevsm8.ipunish.punishment.PunishmentType;
import me.wedevsm8.ipunish.util.UtilPlayer;
import me.wedevsm8.ipunish.util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public final class PunishClient 
{
	private UUID _uuid;
	
	private Player _player;
	
	private List<Punishment> _punishments;
	
	private Thread _thread;
	
	private boolean _scanInProgress = false;
	private boolean _result = false;
	
	public PunishClient(UUID uuid)
	{
		this._uuid = uuid;
		
		this._player = Bukkit.getPlayer(uuid);
		
		this._punishments = new ArrayList<Punishment>();
	}
	
	public PunishClient(Player player)
	{
		this(player.getUniqueId());
	}
	
	@SuppressWarnings({ "deprecation" })
	public Iterator<Punishment> retrievePunishments()
	{
		System.out.println("(iPunish) Retrieving Punishments for Client ID: " + this._uuid);
		
		this._scanInProgress = true;
		
		try
		{
			DatabaseQuery query = new DatabaseQuery("SELECT * FROM iPunish WHERE player='" + this._uuid.toString() + "';");
			
			final ResultSet set = query.retrieveQuery();
			
			final List<Punishment> punishments = new ArrayList<Punishment>();
			
				this._thread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							while(set.next())
							{
								Punishment punishment = new Punishment(_uuid, UUID.fromString(set.getString("issuer")), set.getString("reason"), set.getLong("created"), set.getBoolean("removed"), PunishmentType.valueOf(set.getString("type").toUpperCase()), set.getLong("expiresOn"));
								
								punishments.add(punishment);
								
								System.out.println("(iPunish) Retrieving Punishment for " + _uuid.toString());
								System.out.println("(iPunish) Listed Punishment: " + punishment.toString());
								
								if(!(set.next()))
								{
									_thread.stop();
									
									break;
								}
							}
						}
						catch(SQLException exception)
						{
							exception.printStackTrace();
						}
					}
				});
				
				this._thread.start();
			
			this._punishments = punishments;
			
			return punishments.iterator();
		}
		catch(SQLException exception)
		{
			exception.printStackTrace();
		}
		
		return this._punishments.iterator();
	}
	
	public boolean resultsRetrieved()
	{
		if(this._scanInProgress)
			return false;
		
		return this._result;
	}
	
	@SuppressWarnings({ "deprecation" })
	public List<Punishment> retrievePunishmentsList()
	{
		System.out.println("(iPunish) Retrieving Punishments for Client ID: " + this._uuid);
		
		this._scanInProgress = true;
		this._result = false;
		
		try
		{
			DatabaseQuery query = new DatabaseQuery("SELECT * FROM iPunish WHERE player='" + this._uuid.toString() + "';");
			
			final ResultSet set = query.retrieveQuery();
			
			final List<Punishment> punishments = new ArrayList<Punishment>();
			
				this._thread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							while(set.next())
							{
								Punishment punishment = new Punishment(_uuid, UUID.fromString(set.getString("issuer")), set.getString("reason"), set.getLong("created"), set.getBoolean("removed"), PunishmentType.valueOf(set.getString("type").toUpperCase()), set.getLong("expiresOn"));
								
								punishments.add(punishment);
								
								System.out.println("(iPunish) Retrieving Punishment for " + _uuid.toString());
								System.out.println("(iPunish) Listed Punishment: " + punishment.toString());
								
								if(!(set.next()))
								{
									_punishments = punishments;
									_result = true;
									_scanInProgress = false;
									
									_thread.stop();
									
									break;
								}
							}
						}
						catch(SQLException exception)
						{
							exception.printStackTrace();
						}
					}
				});
				
				this._thread.start();
			
			return punishments;
		}
		catch(SQLException exception)
		{
			exception.printStackTrace();
		}
		
		return this._punishments;
	}
	
	public Punishment getLatestPunishment()
	{
		if(this._punishments.size() > 0)
		{
			return this._punishments.get(this._punishments.size() - 1);
		}
		
		return null;
	}
	
	public Player getPlayer()
	{
		return this._player;
	}
	
	public void issuePunishment(Punishment punishment)
	{
		punishment.issue();
		
		try
		{
			int id = this._punishments.size() + 1;
			
			if(punishment.getPunishmentType().getRawTime() != -1L)
			{
				new AsyncDatabaseUpdate("INSERT INTO iPunish VALUES ('" + punishment.getPlayerUUID() + "', '" 
						+ punishment.getIssuerUUID() + "', '" + punishment.getReason() + "', '" + System.currentTimeMillis() + "', '" 
						+ 0 + "', '" + punishment.getPunishmentType().toString() + "', '" 
						+ UtilTime.getRemaining(punishment.getPunishmentType().getRawTime()) 
						+ "', '" + null + "', '" + null + "', '" + id + "');");
			}
			else
			{
				new AsyncDatabaseUpdate("INSERT INTO iPunish VALUES ('" + punishment.getPlayerUUID() + "', '" 
						+ punishment.getIssuerUUID() + "', '" + punishment.getReason() + "', '" + System.currentTimeMillis() + "', '" 
						+ 0 + "', '" + punishment.getPunishmentType().toString() + "', '" 
						+ -1L
						+ "', '" + null + "', '" + null + "', '" + id + "');");
			}
			
			UtilPlayer.sendMessage(new Permission("ipunish.issue"), punishment.getPunishmentType(), punishment.getReason(), punishment.getIssuer(), punishment.getPlayer());
			
			System.out.println("(iPunish) Issued Punishment Successfully to " + punishment.getPlayer().getName() + ", Action requested by " + punishment.getIssuer().getName());
		}
		catch(Exception exception)
		{
			System.out.println("(iPunish) ERROR PunishClient.issuePunishment: " + exception.getMessage());
		}
	}
	
	public void removePunishment(Punishment punishment, UUID remover, String reason)
	{
		punishment.remove(remover, reason);
	}
	
	public void destroy()
	{
		System.out.println("(iPunish) Destroyed Client: " + this._uuid.toString());
		
		this._uuid = null;
		this._player = null;
		this._punishments = null;
		this._thread = null;
	}
}