/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import javax.comm.CommPortIdentifier;
import java.util.Enumeration;


/**
 * This class implements a physical serial interface.
 *
 * @version $Id: SerialLink.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SerialLink
	extends DataObject
{
	/**
	 * Create a new SerialLink.
	 */
	public SerialLink ()
	{
		super("SerialLink");

		putAttribute ("device", "");
	}

	/**
	 * Get the device.
	 *
	 * @return The device.
	 */
	public String getDevice ()
	{
		return getStringAttribute ("device");
	}

	/**
	 * Set the device.
	 *
	 * @param device The new device.
	 */
	public void setDevice (String device)
	{
		putAttribute ("device", device);
	}

	/**
	 * Create a string representation of the serial link.
	 *
	 * @return A string representation.
	 */
	public String toString ()
	{
		return getDevice ();
	}

	/**
	 * Search for serial devices and add them to the registry.
	 *
	 * @param interfaceRegistry The interface registry.
	 */
	public static void searchForSerialDevices (InterfaceRegistry interfaceRegistry)
	{
		try
		{
			Enumeration portList = CommPortIdentifier.getPortIdentifiers ();

			while (portList.hasMoreElements ())
			{
				CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement ();

				if (portId.getPortType () == CommPortIdentifier.PORT_SERIAL)
				{
					SerialLink link =
						(SerialLink) Engine.instance ().getIObjectFactory ().newInstance (
							"SerialLink");

					link.setUniqueId (Engine.instance ().getTransientIDGenerator ().createId ());
					link.setDevice (portId.getName ());
					Engine.instance ().getBaseRegistry ().add ((BaseObject) link);
					interfaceRegistry.addSerialLink (link);
				}
			}
		}
		catch (NoSuchIObjectException x)
		{
		}
	}
}