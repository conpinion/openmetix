/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystem;


import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.base.DataObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * This class implements a gaging system output object.
 *
 * @version $Id: GagingOutput.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingOutput
	extends DataObject
{
	/** Custom properties. */
	private Properties customProperties;

	/**
	 * Create a new GagingOutput.
	 */
	public GagingOutput ()
	{
		super("GagingOutput");

		putAttribute ("name", "");
		putAttribute ("dimension", "");
		putAttribute ("unit", "");
		putAttribute ("customParams", "");
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
		customProperties = null;
	}

	/**
	 * Get the output's name.
	 *
	 * @return The output name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the output name.
	 *
	 * @param name The new name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Get the dimension of the output.
	 *
	 * @return The output's dimension.
	 */
	public String getDimension ()
	{
		return getStringAttribute ("dimension");
	}

	/**
	 * Set the output dimension.
	 *
	 * @param dimension The new dimension.
	 */
	public void setDimension (String dimension)
	{
		putAttribute ("dimension", dimension);
	}

	/**
	 * Get the unit of the output.
	 *
	 * @return The output's unit.
	 */
	public String getUnit ()
	{
		return getStringAttribute ("unit");
	}

	/**
	 * Set the output unit.
	 *
	 * @param unit The new unit.,
	 */
	public void setUnit (String unit)
	{
		putAttribute ("unit", unit);
	}

	/**
	 * Retrieve the output's Unit.
	 *
	 * @return The output Unit.
	 */
	public Dimension.Unit getDimensionUnit ()
	{
		return Dimension.findDimension (getDimension ()).findUnit (getUnit ());
	}

	/**
	 * Set the output's Unit.
	 *
	 * @param unit The new Unit.
	 */
	public void setDimensionUnit (Dimension.Unit unit)
	{
		setDimension (unit.getDimension ().getDimensionKey ());
		setUnit (unit.getUnitKey ());
	}

	/**
	 * Get the custom parameters.
	 *
	 * @return The custom parameters.
	 */
	public String getCustomParams ()
	{
		return getStringAttribute ("customParams");
	}

	/**
	 * Set the custom parameters.
	 *
	 * @param customParams The new custom parameters.
	 */
	public void setCustomParams (String customParams)
	{
		putAttribute ("customParams", customParams);
	}

	/**
	 * Get the custom properties.
	 *
	 * @return The custom properties.
	 */
	public Properties getCustomProperties ()
	{
		if (customProperties == null)
		{
			customProperties = new Properties();

			try
			{
				customProperties.load (new ByteArrayInputStream(getCustomParams ().getBytes ()));
			}
			catch (IOException x)
			{
				Log.logError("server", "GagingOutput.getCustomProperties",
					x.toString());
			}
		}

		return customProperties;
	}

	/**
	 * Set the custom properties.
	 *
	 * @param customProperties The custom properties.
	 */
	public void setCustomProperties(Properties customProperties){
		this.customProperties = customProperties;

		try{
			ByteArrayOutputStream propOut = new ByteArrayOutputStream();

			customProperties.store(propOut, null);
			setCustomParams(propOut.toString());
		} catch (IOException x){
			Log.logError("client", "GagingOutputEditor.setCustomProperties",
				x.toString());
		}
	}

	/**
	 * Create a string representation of the output.
	 *
	 * @return A string representation.
	 */
	public String toString(){
		return getName();
	}
}