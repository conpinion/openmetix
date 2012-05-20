/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.friedrichs;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutput;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriver;
import java.text.DecimalFormat;
import java.util.Properties;


/**
 * This gaging system drivers reads measurments from Lambrecht weather stations
 * transmitted through a serial line.
 *
 * @version $Id: FriedrichsCombilogDriver.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class FriedrichsCombilogDriver
	extends GagingSystemDriver
{
	/** Custom output parameter names. */
	private static String[] outputParameterNames =
		new String[]
		{
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix-interfacing-friedrichs.unit"),
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix-interfacing-friedrichs.channel")
		};

	/** Custom output parameter classes. */
	private static Class[] outputParameterClasses = new Class[]
		{
			String.class,
			String.class
		};

	/**
	 * Create a new instance of the Friedrichs Combilog driver.
	 */
	public FriedrichsCombilogDriver ()
	{
		super("FriedrichsCombilogDriver");
	}

	/**
	 * The main driver method reads synmet messages from a serial
	 * port and feeds the sensor values into the measurement processing system.
	 */
	public void run ()
	{
		while (running)
		{
			int numOutputs = getOutputCount ();

			int unit =
				NumberTools.toInt (String.valueOf (system.getDriverProperties ().get ("unit")), 0);

			try
			{
				for (int i = 0; i < numOutputs; ++i)
				{
					int channel =
						NumberTools.toInt (
							String.valueOf (
								system.getOutput (i).getCustomProperties ().get ("channel")), i);

					int outputUnit =
						NumberTools.toInt (
							String.valueOf (
								system.getOutput (i).getCustomProperties ().get ("unit")), unit);

					String cmd =
						"\044" + NumberTools.toHex2 (outputUnit) + "R" +
						NumberTools.toHex2 (channel) + "\015";

					String res = iface.comm (cmd, '\025');

					if (res != null && res.startsWith ("="))
					{
						double value = NumberTools.toDouble (res.substring (1), 0.0);

						sendMeasurement (value, i);
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
		Properties systemProps = system.getDriverProperties ();
		Properties props = output.getCustomProperties ();

		switch (paramIndex)
		{
			case 0:
				return props.get ("unit") != null ? props.get ("unit").toString ()
												  : String.valueOf (
					NumberTools.toInt ((String) systemProps.get ("unit"), 0));

			case 1:
				return props.get ("channel") != null ? props.get ("channel").toString ()
													 : String.valueOf (index);

			default:
				return null;
		}
	}
}