package me.wedevsm8.ipunish.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilIterator 
{
	public static <T> List<T> copyIterator(Iterator<T> iter) 
	{
	    List<T> copy = new ArrayList<T>();
	    
	    while (iter.hasNext())
	        copy.add(iter.next());
	    
	    System.out.println("(iPunish) UtilIterator.copyIterator: Copied Iterator Successfully!");
	    
	    return copy;
	}
}
