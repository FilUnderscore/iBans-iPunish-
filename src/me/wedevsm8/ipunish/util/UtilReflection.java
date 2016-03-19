package me.wedevsm8.ipunish.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class UtilReflection 
{
	public static void setValue(Object instance, String fieldName, Object value)
		throws Exception
	{
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}
	
	public static Object getValue(Object instance, String fieldName)
		throws Exception
	{
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		
		return field.get(instance);
	}
	
	public static String getVersion()
	{
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		
		return version;
	}
	
	public static Class<?> getNMSClass(String className)
	{
		String fullName = "net.minecraft.server." + getVersion() + className;
		
		Class<?> clazz = null;
		
		try
		{
			clazz = Class.forName(fullName);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		return clazz;
	}
	
	public static Class<?> getOBCClass(String className)
	{
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		
		Class<?> clazz = null;
		
		try
		{
			clazz = Class.forName(fullName);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		return clazz;
	}
	
	/*
	public static Object getHandle(Object obj)
	{
		try
		{
			return(getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]));
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public static Field getField(Class<?> clazz, String name)
	{
		try
		{
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			
			return field;
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... args)
	{
		for(Method m : clazz.getMethods())
		{
			if((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes()))))
			{
				m.setAccessible(true);
				
				return m;
			}
		}
		
		return null;
	}
	*/
	
	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2)
	{
		boolean equal = true;
		
		if(l1.length != l2.length)
		{
			return false;
		}
		
		for(int i = 0; i < l1.length; i++)
		{
			if(l1[i] != l2[i])
			{
				equal = false;
				
				break;
			}
		}
		
		return equal;
	}
	
	 public static int getMinecraftClientVersion(Player player) throws Exception
	    {
	        Object handle = getHandle(player);
	      
	        Field playerConnection_field = handle.getClass().getDeclaredField("playerConnection");
	        Object playerConnection = playerConnection_field.get(handle);
	      
	        Field networkManager_field = playerConnection.getClass().getDeclaredField("networkManager");
	        Object networkManager = networkManager_field.get(playerConnection);
	      
	        Method getVersion = getMethod(networkManager.getClass(), "getVersion", 0);
	      
	        if (getVersion == null)
	            return 47;
	      
	        int version = (int) (Integer) getVersion.invoke(networkManager);
	      
	        return version;
	    }
	  
	    public static boolean isMinecraft1_8OrHigher(Player player)
	    {
	        try {
	            int version = getMinecraftClientVersion(player);
	          
	            return version >= 47;
	        } catch (Exception e) {
	            e.printStackTrace();
	          
	            return false;
	        }
	    }
	  
	    public static boolean isMinecraft1_7OrLower(Player player)
	    {
	        try {
	            int version = getMinecraftClientVersion(player);
	          
	            return version < 47;
	        } catch (Exception e) {
	            e.printStackTrace();
	          
	            return true;
	        }
	    }
	  
	    public static void sendPacketRadius(Location loc, int radius, Object packet) {
	        for (Player p : loc.getWorld().getPlayers()) {
	            if (loc.distanceSquared(p.getLocation()) < (radius * radius)) {
	                sendPacket(p, packet);
	            }
	        }
	    }
	  
	    public static void sendPacket(List<Player> players, Object packet) {
	        for (Player p : players) {
	            sendPacket(p, packet);
	        }
	    }
	  
	    public static void sendPacket(Player p, Object packet) {
	        try {
	            Object nmsPlayer = getHandle(p);
	            Field con_field = nmsPlayer.getClass().getDeclaredField("playerConnection");
	            Object con = con_field.get(nmsPlayer);
	            Method packet_method = getMethod(con.getClass(), "sendPacket");
	            packet_method.invoke(con, packet);
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        }
	    }
	  
	    public static Class<?> getCraftClass(String ClassName) {
	        String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
	        String className = "net.minecraft.server." + version + ClassName;
	        Class<?> c = null;
	        try {
	            c = Class.forName(className);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return c;
	    }
	  
	    public static Class<?> getBukkitClass(String ClassPackageName) {
	        String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
	        String className = "org.bukkit.craftbukkit." + version + ClassPackageName;
	        Class<?> c = null;
	        try {
	            c = Class.forName(className);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return c;
	    }
	  
	    public static Object getHandle(Entity entity) {
	        Object nms_entity = null;
	        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
	        try {
	            nms_entity = entity_getHandle.invoke(entity);
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
	        return nms_entity;
	    }
	  
	    public static Object getHandle(Object entity) {
	        Object nms_entity = null;
	        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
	        try {
	            nms_entity = entity_getHandle.invoke(entity);
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
	        return nms_entity;
	    }
	  
	    public static Field getField(Class<?> cl, String field_name) {
	        try {
	            Field field = cl.getDeclaredField(field_name);
	            return field;
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	  
	    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
	        for (Method m : cl.getMethods()) {
	            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
	                return m;
	            }
	        }
	        return null;
	    }
	  
	    public static Method getMethod(Class<?> cl, String method, Integer args) {
	        for (Method m : cl.getMethods()) {
	            if (m.getName().equals(method) && args.equals(Integer.valueOf(m.getParameterTypes().length))) {
	                return m;
	            }
	        }
	        return null;
	    }
	  
	    public static Method getMethod(Class<?> cl, String method) {
	        for (Method m : cl.getMethods()) {
	            if (m.getName().equals(method)) {
	                return m;
	            }
	        }
	        return null;
	    }
	  
	    public static Method getMethod(Class<?> cl, Class<?> returnType, Class<?>[] parameters) {
	        Method method = null;
	        for (Method m : cl.getMethods()) {
	            if (m.getReturnType().getCanonicalName().equals(returnType.getCanonicalName()) && m.getParameterTypes().length == parameters.length) {
	                for (int i = 0; i < parameters.length; i++) {
	                    if (!m.getParameterTypes()[i].getCanonicalName().equals(parameters[i].getCanonicalName()))
	                        continue;
	                }
	              
	                method = m;
	                break;
	            }
	        }
	      
	        return method;
	    }
}