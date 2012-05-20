/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.base.Transceiver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ClientTransceiver.
 *
 * @version $Id: ClientTransceiver.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ClientTransceiver
	implements Transceiver
{
	/** The sending channel number. */
	private double sender;

	/** List of all receivers. */
	private List receivers;

	/** The connected channel. */
	private Channel connectedChannel;

	/**
	 * Create a new ClientTransceiver.
	 */
	public ClientTransceiver (double sender)
	{
		receivers = new LinkedList();
		this.sender = sender;
	}

	/**
	 * Create a new ClientTransceiver.
	 */
	public ClientTransceiver (double sender, Channel connectedChannel)
	{
		this(sender);
		this.connectedChannel = connectedChannel;
	}

	/**
	 * Get the sender channel id.
	 *
	 * @return The sender channel id.
	 */
	public double getSender ()
	{
		return sender;
	}

	/**
	 * Set the sender channel id.
	 *
	 * @return The sender channel id.
	 */
	public void setSender (double sender)
	{
		this.sender = sender;
	}

	/**
	 * Return the current connected channel of this transceiver.
	 *
	 * @return The connected channel.
	 **/
	public Channel getConnectedChannel ()
	{
		return connectedChannel;
	}

	/**
	 * Get an iterator over all receivers.
	 *
	 * @return The iterator.
	 */
	public Iterator getReceiverIterator ()
	{
		return receivers.iterator ();
	}

	/**
	 * Add a receiver.
	 */
	public void addReceiver (double receiver)
	{
		receivers.add (new Double(receiver));
	}

	/**
	 * Remove all receivers
	 */
	public void removeAllReceivers ()
	{
		receivers.clear ();
	}
}