package me.wedevsm8.ipunish.punishment;

import me.wedevsm8.ipunish.util.UtilTime;

public enum PunishmentType 
{
	BAN(0L, "Ban"),
	KICK("Kick"),
	WARN(0L, "Warning"),
	MUTE(0L, "Mute"),
	REMOVED("Removed Punishment");
	
	private long _time = 0L;
	private String _name;
	
	private PunishmentType(long time, String name)
	{
		this._time = time;
		
		this._name = name;
	}
	
	private PunishmentType(String name) 
	{
		this._name = name;
	}
	
	public long getRawTime()
	{
		return this._time;
	}
	
	public boolean hasExpired()
	{
		return UtilTime.elapsed(this.getRawTime(), (System.currentTimeMillis() - this.getRawTime()));
	}
	
	public String getTime()
	{
		return UtilTime.convertString(this._time);
	}
	
	public void setTime(long time)
	{
		this._time = time;
	}
	
	public String getName()
	{
		return this._name;
	}
}