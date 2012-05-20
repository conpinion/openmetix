/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystemdriver;



/**
 * This is a 'do nothing' driver.
 *
 * @version $Id: NullDriver.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class NullDriver
	extends GagingSystemDriver
{
	/**
	 * Create a new instance of driver.
	 */
	public NullDriver ()
	{
		super("NullDriver");
	}

	/**
	 * The main driver method reads synmet messages from a serial
	 * port and feeds the sensor values into the measurement processing system.
	 */
	public void run ()
	{
		while (running)
		{
			try
			{
				Thread.sleep (1000);
			}
			catch (InterruptedException x)
			{
			}
		}
	}
}