/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.logger;


import java.text.DateFormat;
import java.util.Date;


/**
 * A logger that prints logging messages to the system console.
 *
 * @version $Id: ConsoleLogger.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ConsoleLogger
	implements Logger
{
	/** The current time. */
	private Date currentTime;

	/** Used to format the current time. */
	private DateFormat timeFormat;

	/**
	 * Create a new console logger.
	 */
	public ConsoleLogger ()
	{
		currentTime = new Date();
		timeFormat = DateFormat.getDateTimeInstance (DateFormat.SHORT, DateFormat.SHORT);
	}

	/**
	 * Get the id of the Logger
	 *
	 * @return The logger id.
	 */
	public String getId ()
	{
		return "Console";
	}

	/**
	 * Initialize the logger.
	 *
	 * @param category The logger category.
	 */
	public void init (String category)
	{
	}

	/**
	 * Free all logger resources.
	 */
	public void dispose ()
	{
	}

	/**
	 * Log a message.
	 *
	 * @param category The logger category.
	 * @param source The logging source.
	 * @param message The log message.
	 * @param level The logging level.
	 */
	public void log (String category, String source, String message, int level)
	{
		currentTime.setTime (System.currentTimeMillis ());
		System.out.println (
			"" + timeFormat.format (currentTime) + " " + Log.logLevelName (level) + " [" +
			category + "] [" + source + "] " + message);
	}
}