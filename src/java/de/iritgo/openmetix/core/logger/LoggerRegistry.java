/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.logger;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * This registry contains all system loggers.
 *
 * @version $Id: LoggerRegistry.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class LoggerRegistry
{
	/** Logger catgeories. */
	static Hashtable CategoryRegistry = new Hashtable();

	/** Loggers of one category. */
	static Hashtable LoggerRegistry = new Hashtable();

	/**
	 * Create a new LoggerRegistry.
	 */
	public LoggerRegistry ()
	{
		Log.setloggerRegistries (CategoryRegistry, LoggerRegistry);
	}

	/**
	 * Initialize the base loggers.
	 */
	public void initBaseLogger ()
	{
		addLogger (new ConsoleLogger());
	}

	/**
	 * Add a logger.
	 *
	 * @param category The logger category.
	 * @param loggerId The id
	 */
	public void addLogger (String category, String loggerId)
	{
		if (! CategoryRegistry.containsKey (category))
		{
			CategoryRegistry.put (category, new ArrayList());
		}

		ArrayList loggerList = (ArrayList) CategoryRegistry.get (category);

		if (loggerList.contains (loggerId))
		{
			return;
		}

		loggerList.add (loggerId);

		((Logger) LoggerRegistry.get (loggerId)).init (category);
	}

	/**
	 * Add a logger.
	 *
	 * @param logger The logger to add.
	 */
	public void addLogger (Logger logger)
	{
		if (! LoggerRegistry.contains (logger.getId ()))
		{
			LoggerRegistry.put (logger.getId (), logger);
		}
	}

	/**
	 * Remove a logger from a category.
	 *
	 * @param category The category from which to remove the logger.
	 * @param loggerId The id of the logger to remove.
	 */
	public void removeLogger (String category, String loggerId)
	{
		if (! CategoryRegistry.containsKey (category))
		{
			return;
		}

		ArrayList loggerList = (ArrayList) CategoryRegistry.get (category);

		for (int i = 0; i < loggerList.size (); ++i)
		{
			if (loggerList.get (i).equals (loggerId))
			{
				loggerList.remove (i);
			}
		}
	}

	/**
	 * Remove a logger from all categories.
	 *
	 * @param loggerId The id of the logger to remove.
	 */
	public void removeLogger (String loggerId)
	{
		Enumeration e = CategoryRegistry.elements ();

		while (e.hasMoreElements ())
		{
			ArrayList loggerList = (ArrayList) e.nextElement ();

			for (int i = 0; i < loggerList.size (); ++i)
			{
				if (loggerList.get (i).equals (loggerId))
				{
					loggerList.remove (i);
				}
			}
		}

		LoggerRegistry.remove (loggerId);
	}
}