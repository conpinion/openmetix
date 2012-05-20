/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.logger.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Instances of this class implement a specific interface driver.
 *
 * @version $Id: InterfaceDriver.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class InterfaceDriver
{
	/** How many times this driver is used. */
	protected int openCount;

	/** The driver configuration. */
	protected Properties config;

	/**
	 * Create a new InterfaceDriver.
	 */
	public InterfaceDriver ()
	{
	}

	/**
	 * Initialize the driver.
	 *
	 * @param iface The interface from which to configure the driver.
	 */
	public void init (Interface iface)
	{
		config = new Properties();

		try
		{
			config.load (new ByteArrayInputStream(iface.getDriverParams ().getBytes ()));
		}
		catch (IOException x)
		{
			Log.logError ("server", "InterfaceDriver.init", x.toString ());
		}
	}

	/**
	 * Open the communication link.
	 */
	public synchronized void open ()
	{
		++openCount;
	}

	/**
	 * Close the communication link.
	 */
	public void close ()
	{
		--openCount;
	}

	/**
	 * Send a string to the communication link and wait for a
	 * response.
	 *
	 * @param value The value to send.
	 * @return The response.
	 */
	public synchronized String comm (String value)
	{
		return null;
	}

	/**
	 * Send a string to the communication link and wait for a
	 * response.
	 *
	 * @param value The value to send.
	 * @param nakChar Charackter to wait for in case of a communication error.
	 * @return The response.
	 */
	public synchronized String comm (String value, char nakChar)
	{
		return comm (value);
	}
}