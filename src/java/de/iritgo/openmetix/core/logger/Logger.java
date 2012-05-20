/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.logger;



/**
 * Interface which all loggers must implement.
 *
 * @version $Id: Logger.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public interface Logger
{
	/**
	 * Get the id of the Logger
	 *
	 * @return The logger id.
	 */
	public String getId ();

	/**
	 * Initialize the logger.
	 *
	 * @param category The logger category.
	 */
	public void init (String category);

	/**
	 * Free all logger resources.
	 */
	public void dispose ();

	/**
	 * Log a message.
	 *
	 * @param category The logger category.
	 * @param source The logging source.
	 * @param message The log message.
	 * @param level The logging level.
	 */
	public void log (String category, String source, String message, int level);
}