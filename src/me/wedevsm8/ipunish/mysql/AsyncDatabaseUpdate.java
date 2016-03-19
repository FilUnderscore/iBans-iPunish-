package me.wedevsm8.ipunish.mysql;

import java.sql.SQLException;

public class AsyncDatabaseUpdate
{	
	public AsyncDatabaseUpdate(final String query) 
	{
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					DatabaseManager.getConnection().createStatement().executeUpdate(query);
				} 
				catch (SQLException e) 
				{
					System.out.println("(iPunish) ERROR AsyncDatabaseUpdate: " + e.getMessage());
				}
			}
		});
		
		t.start();
	}
}