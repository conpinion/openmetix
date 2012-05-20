/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystem;


import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;


/**
 * This class implements the gaging system object.
 *
 * @version $Id: GagingSystem.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingSystem
	extends DataObject
{
	/** Driver properties. */
	private Properties driverProperties;

	/**
	 * Create a new GagingSystem.
	 */
	public GagingSystem ()
	{
		super("GagingSystem");

		putAttribute ("name", "");
		putAttribute ("active", new Integer(0));
		putAttribute ("storeToDatabase", new Integer(1));
		putAttribute ("interfaceId", new Long(0));
		putAttribute ("driverId", "");
		putAttribute ("driverParams", "");
		putAttribute (
			"outputs", new IObjectList("outputs", new FrameworkProxy(new GagingOutput()), this));
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
		driverProperties = null;
	}

	/**
	 * Get the system name.
	 *
	 * @return The system name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the system name.
	 *
	 * @param name The new name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Check wether this system is active.
	 *
	 * @return True for an active system.
	 */
	public boolean getActive ()
	{
		return getIntAttribute ("active") != 0;
	}

	/**
	 * Activate/deactivate the system.
	 *
	 * @param active If true the system is active.
	 */
	public void setActive (boolean active)
	{
		putAttribute ("active", new Integer(active ? 1 : 0));
	}

	/**
	 * Check wether the system should store measurements to the
	 * database.
	 *
	 * @return True if the system stores the measurements to the
	 *   database.
	 */
	public boolean isStoreToDatabase ()
	{
		return getIntAttribute ("storeToDatabase") != 0;
	}

	/**
	 * Decide wether the system should store measurements to the
	 * database or not.
	 *
	 * @param storeToDatabase If true the system stores the measurements
	 *   to the database.
	 */
	public void setStoreToDatabase (boolean storeToDatabase)
	{
		putAttribute ("storeToDatabase", new Integer(storeToDatabase ? 1 : 0));
	}

	/**
	 * Get the id of the system driver.
	 *
	 * @return The driver id.
	 */
	public String getDriverId ()
	{
		return getStringAttribute ("driverId");
	}

	/**
	 * Set the id of the system driver.
	 *
	 * @param driverId The new id of the system driver.
	 */
	public void setDriverId (String driverId)
	{
		putAttribute ("driverId", driverId);
	}

	/**
	 * Retrieve the system driver parameters.
	 *
	 * @return The system driver parameters.
	 */
	public String getDriverParams ()
	{
		return getStringAttribute ("driverParams");
	}

	/**
	 * Set the system driver parameters.
	 *
	 * @param driverParams The new system driver parameters.
	 */
	public void setDriverParams (String driverParams)
	{
		putAttribute ("driverParams", driverParams);
	}

	/**
	 * Get the interface id.
	 *
	 * @return The interface id.
	 */
	public long getInterfaceId ()
	{
		return getLongAttribute ("interfaceId");
	}

	/**
	 * Set the interface id.
	 *
	 * @param interfaceId The new interface id.
	 */
	public void setInterfaceId (long interfaceId)
	{
		putAttribute ("interfaceId", new Long(interfaceId));
	}

	/**
	 * Add a system output.
	 *
	 * @param output The output to add.
	 */
	public void addOutput (GagingOutput output)
	{
		getIObjectListAttribute ("outputs").add (output);
	}

	/**
	 * Remove a system output.
	 *
	 * @param output The output to remove.
	 */
	public void removeOutput (GagingOutput output)
	{
		getIObjectListAttribute ("outputs").remove (output);
	}

	/**
	 * Get an iterator over all outputs of a gaging system.
	 *
	 * @return An output iterator.
	 */
	public Iterator getOutputIterator ()
	{
		return getIObjectListAttribute ("outputs").iterator ();
	}

	/**
	 * Retrieve the system outputs
	 *
	 * @return The system output list.
	 */
	public IObjectList getOutputs ()
	{
		return getIObjectListAttribute ("outputs");
	}

	/**
	 * Get the number of output of the gaging system.
	 *
	 * @return The number of outputs.
	 */
	public int getOutputCount ()
	{
		return getIObjectListAttribute ("outputs").size ();
	}

	/**
	 * Get a specific system output.
	 *
	 * @param index The index of the output to retrieve.
	 * @return The system output.
	 */
	public GagingOutput getOutput (int index)
	{
		return (GagingOutput) getIObjectListAttribute ("outputs").get (index);
	}

	/**
	 * Get the driver properties.
	 *
	 * @return The driver properties.
	 */
	public Properties getDriverProperties ()
	{
		if (driverProperties == null)
		{
			driverProperties = new Properties();

			try
			{
				driverProperties.load (new ByteArrayInputStream(getDriverParams ().getBytes ()));
			}
			catch (IOException x)
			{
				Log.logError ("server", "GagingSystem.getDriverProperties", x.toString ());
			}
		}

		return driverProperties;
	}

	/**
	 * Set the driver properties.
	 *
	 * @param driverProperties The driver properties.
	 */
	public void setDriverProperties (Properties driverProperties)
	{
		this.driverProperties = driverProperties;

		try
		{
			ByteArrayOutputStream propOut = new ByteArrayOutputStream();

			driverProperties.store (propOut, null);
			setDriverParams (propOut.toString ());
		}
		catch (IOException x)
		{
			Log.logError ("client", "GagingSystem.setDriverProperties", x.toString ());
		}
	}

	/**
	 * Create a string representation of the gaging system.
	 *
	 * @return A string representation.
	 */
	public String toString ()
	{
		return getName ();
	}
}