/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.lambrecht;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutput;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * This gaging system drivers reads measurements from Lambrecht weather stations
 * transmitted through a serial line.
 *
 * @version $Id: LambrechtSynmetDriver.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class LambrechtSynmetDriver
	extends GagingSystemDriver
{
	/** Custom output parameter names. */
	private static String[] outputParameterNames =
		new String[]
		{
			Engine.instance ().getResourceService ().getStringWithoutException ("metix.code")
		};

	/** Custom output parameter classes. */
	private static Class[] outputParameterClasses = new Class[]
		{
			String.class
		};

	/**
	 * Create a new instance of the Lambrecht synmet driver.
	 */
	public LambrechtSynmetDriver ()
	{
		super("LambrechtSynmetDriver");
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
				String res = iface.comm ("\00201\033A\015");

				if (res != null && res.length () > 17)
				{
					StringTokenizer st = new StringTokenizer(res.substring (17), "|");

					int outputNum = 0;

					while (st.hasMoreTokens ())
					{
						String measure = st.nextToken ();

						if (measure.length () < 5 || measure.charAt (0) == 23)
						{
							break;
						}

						double value = NumberTools.toDouble (measure.substring (4), 0.0);

						if (value < 99999900.0)
						{
							sendMeasurement (value, outputNum);
						}

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

	/**
	 * Get the number of custom output parameters.
	 *
	 * @return The number of output parameters.
	 */
	public int getCustomOutputParameterCount ()
	{
		return outputParameterNames.length;
	}

	/**
	 * Get the name of a custom output parameter.
	 *
	 * @param index The index of the parameter to retrieve.
	 * @return The parameter name.
	 */
	public String getCustomOutputParameterName (int index)
	{
		return outputParameterNames[index];
	}

	/**
	 * Get the class of a custom output parameter.
	 *
	 * @param index The index of the parameter to retrieve.
	 * @return The parameter class.
	 */
	public Class getCustomOutputParameterClass (int index)
	{
		return outputParameterClasses[index];
	}

	/**
	 * Get the value of a custom output parameter.
	 *
	 * @param system The gaging system.
	 * @param output The gaging system output.
	 * @param index The index of the output to retrieve.
	 * @param paramIndex The index of the parameter to retrieve.
	 * @return The parameter value.
	 */
	public Object getCustomOutputParameterValue (
		GagingSystem system, GagingOutput output, int index, int paramIndex)
	{
		Properties props = output.getCustomProperties ();

		switch (paramIndex)
		{
			case 0:
				return props.get ("code") != null ? props.get ("code").toString () : "";

			default:
				return null;
		}
	}
}