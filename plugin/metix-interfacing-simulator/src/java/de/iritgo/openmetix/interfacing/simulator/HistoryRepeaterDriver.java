/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.simulator;


import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;


/**
 * This driver creates new measurements from historical data.
 *
 * @version $Id: HistoryRepeaterDriver.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class HistoryRepeaterDriver
	extends GagingSystemDriver
{
	/**
	 * Create a new instance of the driver.
	 */
	public HistoryRepeaterDriver ()
	{
		super("HistoryRepeaterDriver");
	}

	/**
	 * Driver main method.
	 */
	public void run ()
	{
		while (running)
		{
			try
			{
				Thread.sleep (
					1000 * NumberTools.toInt (
						(String) system.getDriverProperties ().get ("interval"), 1));
			}
			catch (InterruptedException x)
			{
			}
		}
	}
}