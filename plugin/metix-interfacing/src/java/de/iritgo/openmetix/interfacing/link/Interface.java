/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.framework.base.DataObject;


/**
 * This class implements a physical interface.
 *
 * @version $Id: Interface.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class Interface
	extends DataObject
{
	/**
	 * Create a new Interface.
	 */
	public Interface ()
	{
		super("Interface");

		putAttribute ("name", "");
		putAttribute ("driverId", "");
		putAttribute ("driverParams", "");
	}

	/**
	 * Get the interface name.
	 *
	 * @return The interface name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the interface name.
	 *
	 * @param name The new name.
	 */
	public void setName (String name)
	{
		putAttribute ("name", name);
	}

	/**
	 * Get the name id of the interface driver.
	 *
	 * @return The interface driver id.
	 */
	public String getDriverId ()
	{
		return getStringAttribute ("driverId");
	}

	/**
	 * Set the name id of the interface driver.
	 *
	 * @param driverId The new driver id.
	 */
	public void setDriverId (String driverId)
	{
		putAttribute ("driverId", driverId);
	}

	/**
	 * Retrieve the driver parameters.
	 *
	 * @return The driver parameters.
	 */
	public String getDriverParams ()
	{
		return getStringAttribute ("driverParams");
	}

	/**
	 * Set the driver parameters.
	 *
	 * @param driverParams The new driver parameters.
	 */
	public void setDriverParams (String driverParams)
	{
		putAttribute ("driverParams", driverParams);
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