package me.wedevsm8.ipunish.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilItem 
{
	public static void setDisplayNameAndLore(ItemStack stack, String displayName, String... lore)
	{
		ItemMeta meta = stack.getItemMeta();
		
		meta.setDisplayName(displayName);
		
		meta.setLore(Arrays.asList(lore));
		
		stack.setItemMeta(meta);
	}
	
	public static void setHeadOwner(ItemStack stack, String owner)
	{
		if(stack.getType() != Material.SKULL_ITEM)
			return;
		
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		
		meta.setOwner(owner);
		
		stack.setItemMeta(meta);
	}
	
	/*
	public static ItemStack addGlows(ItemStack item)
	{
        net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        
        NBTTagCompound tag = null;
        
        if (!nmsStack.hasTag()) 
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        
        if (tag == null) 
        	tag = nmsStack.getTag();
        
        NBTTagList ench = new NBTTagList();
        
        tag.set("ench", ench);
        
        nmsStack.setTag(tag);
        
        return CraftItemStack.asCraftMirror(nmsStack);
    }
    */
	
	/*
	public static ItemStack addGlow(ItemStack item)
	{
		try
		{
			 Object nmsStack = UtilReflection.getNMSClass("ItemStack");
		     
			 Object nmsStackClass = UtilReflection.getNMSClass("CraftItemStack");
			 
			 //Class<?> nmsClass = (Class<?>) UtilReflection.getMethod(nmsStackClass.getClass(), "asNMSCopy", new Class[0]).invoke(nmsStack, new Object[] { item });
		     
			 nmsStack = UtilReflection.getMethod(nmsStackClass.getClass(), "asNMSCopy", new Class[0]).invoke(nmsStack, new Object[] { item });
			 
			 Object compound = UtilReflection.getNMSClass("NBTTagCompound");
			 
			 if(nmsStack.getClass().getField("hasTag").getBoolean(false))
			 {
				 compound = UtilReflection.getNMSClass("NBTTagCompound");
				 
				 UtilReflection.getMethod(nmsStack.getClass(), "setTag", new Class[0]).invoke(nmsStack, new Object[] { compound });
			 }
			 
			 if(compound == null)
				 compound = nmsStack.getClass().getField("getTag");
			 
			 Object list = UtilReflection.getNMSClass("NBTTagList");
			 
			 UtilReflection.getMethod(compound.getClass(), "set", new Class[0]).invoke(compound, new Object[] { "ench", list });
			 
			 UtilReflection.getMethod(nmsStack.getClass(), "setTag", new Class[0]).invoke(nmsStack, new Object[] { compound });
			 
			 return (ItemStack) UtilReflection.getMethod(nmsStackClass.getClass(), "asCraftMirror", new Class[0]).invoke(nmsStackClass, new Object[] { nmsStack });
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		return item;
	}
	*/
	
	public static ItemStack addGlowNameLore(ItemStack item, boolean enchanted, String name, List<String> lore) 
	{
        try 
        {
            Class<?> ItemStack = UtilReflection.getCraftClass("ItemStack");
            Class<?> NBTTagCompound = UtilReflection.getCraftClass("NBTTagCompound");
            Class<?> NBTTagList = UtilReflection.getCraftClass("NBTTagList");
            Class<?> CraftItemStack = UtilReflection.getBukkitClass("inventory.CraftItemStack");
            Class<?> NBTTagString = UtilReflection.getCraftClass("NBTTagString");
            Class<?> NBTBase = UtilReflection.getCraftClass("NBTBase");
          
            Method asNMSCopy = CraftItemStack.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            Method asCraftMirror = CraftItemStack.getDeclaredMethod("asCraftMirror", ItemStack);
            Method hasTag = ItemStack.getDeclaredMethod("hasTag");
            Method setTag = ItemStack.getDeclaredMethod("setTag", NBTTagCompound);
            Method getTag = ItemStack.getDeclaredMethod("getTag");
            Method set = NBTTagCompound.getDeclaredMethod("set", String.class, NBTBase);
            Method add = NBTTagList.getDeclaredMethod("add", NBTBase);
          
            asNMSCopy.setAccessible(true);
            asCraftMirror.setAccessible(true);
            hasTag.setAccessible(true);
            setTag.setAccessible(true);
            getTag.setAccessible(true);
            set.setAccessible(true);
          
            Constructor<?> NBTTagCompoundConstructor = NBTTagCompound.getConstructor();
            Constructor<?> NBTTagListConstructor = NBTTagList.getConstructor();
            Constructor<?> NBTTagStringConstructor = NBTTagString.getConstructor(String.class);
          
            NBTTagCompoundConstructor.setAccessible(true);
            NBTTagListConstructor.setAccessible(true);
          
            Object nmsStack = asNMSCopy.invoke(null, item);
            Object tag = null;
          
            if ((Boolean) hasTag.invoke(nmsStack))
                tag = getTag.invoke(nmsStack);
            else
                tag = NBTTagCompoundConstructor.newInstance();
          
            if (enchanted) {
                Object ench = NBTTagListConstructor.newInstance();
                set.invoke(tag, "ench", ench);
            }
          
            Object display = NBTTagCompoundConstructor.newInstance();
            set.invoke(display, "Name", NBTTagStringConstructor.newInstance(name));
          
            Object loreObj = NBTTagListConstructor.newInstance();
            for (String str : lore) {
                add.invoke(loreObj, NBTTagStringConstructor.newInstance(str));
            }
            set.invoke(display, "Lore", loreObj);
          
            set.invoke(tag, "display", display);
          
            setTag.invoke(nmsStack, tag);
          
            return (org.bukkit.inventory.ItemStack) asCraftMirror.invoke(null, nmsStack);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            
            return item;
        }
    }
}