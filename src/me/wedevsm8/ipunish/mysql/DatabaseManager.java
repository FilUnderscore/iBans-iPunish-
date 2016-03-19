package me.wedevsm8.ipunish.mysql;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager 
{
	private static String _ip;
	private static String _user;
	private static String _pass;
	private static String _database;
	private static int _port; // Normally 3306
	private static Connection _connection;
	
	public DatabaseManager(String ip, String user, String pass, String database, int port) 
	{
		_ip = ip;
		_user = user;
		_pass = pass;
		_database = database;
		_port = port;
	}
	
	public static Connection getConnection() 
	{
		try 
		{
			_connection = DriverManager.getConnection("jdbc:mysql://" + _ip + ":" + _port + "/" + _database, _user, _pass);
		} 
		catch (Exception e)
		{
			System.out.println("(iPunish) ERROR DatabaseManager.getConnection: Could not connect to database!");
		}
		
		return _connection;
	}
}