/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.link;



/**
 * Null communication link.
 *
 * @version $Id: NullInterfaceDriver.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class NullInterfaceDriver
	extends InterfaceDriver
{
	/**
	 * Create a new NullInterfaceDriver.
	 */
	public NullInterfaceDriver ()
	{
	}

	/**
	 * Send a string to the communication link and wait for a
	 * response.
	 *
	 * @param value The value to send.
	 * @return The response.
	 */
	public String comm (String value)
	{
		return "";
	}
}