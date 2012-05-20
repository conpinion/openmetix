/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.thies;


import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;
import java.util.StringTokenizer;


/**
 * This gaging system drivers reads measurments from Thies weather stations
 * transmitted through a serial line.
 *
 * @version $Id: ThiesDriver.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ThiesDriver
	extends GagingSystemDriver
{
	/**
	 * Create a new instance of the Thies synmet driver.
	 */
	public ThiesDriver ()
	{
		super("ThiesDriver");
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
				String res = iface.comm ("\002mm\0003");

				if (res != null)
				{
					StringTokenizer st = new StringTokenizer(res, " ");

					if (st.hasMoreTokens ())
					{
						st.nextToken ();
					}

					int outputNum = 0;

					while (st.hasMoreTokens ())
					{
						String measure = st.nextToken ();

						double value = NumberTools.toDouble (measure, 0.0);

						sendMeasurement (value, outputNum);

						++outputNum;
					}
				}

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