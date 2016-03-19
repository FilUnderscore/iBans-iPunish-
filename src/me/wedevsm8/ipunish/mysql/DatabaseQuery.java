package me.wedevsm8.ipunish.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQuery
{
	private String _query;
	
	public DatabaseQuery(final String query) 
	{
		this._query = query;
	}
	
	public ResultSet retrieveQuery()
		throws SQLException
	{
		return DatabaseManager.getConnection().createStatement().executeQuery(this._query);
	}
}