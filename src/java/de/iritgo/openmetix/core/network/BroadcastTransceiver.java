/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.base.Transceiver;


/**
 * BroadcastTransceiver.
 *
 * @version $Id: BroadcastTransceiver.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class BroadcastTransceiver
	implements Transceiver
{
	/**
	 * Clone the transceiver.
	 *
	 * @return The clone.
	 */
	public Transceiver cloneTransceiver ()
	{
		return new BroadcastTransceiver();
	}
}