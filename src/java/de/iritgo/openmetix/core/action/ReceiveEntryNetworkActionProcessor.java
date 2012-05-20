/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.network.Channel;


/**
 * ReceiveNetworkActionProcessor.
 *
 * @version $Id: ReceiveEntryNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ReceiveEntryNetworkActionProcessor
	extends NetworkActionProcessor
{
	/**
	 * Create a new ReceiveEntryNetworkActionProcessor.
	 *
	 * @param typeId
	 * @param channel
	 * @param parentNetworkActionProcessor
	 */
	public ReceiveEntryNetworkActionProcessor (
		String typeId, Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super(typeId, channel, parentNetworkActionProcessor);
	}

	/**
	 * Clone a new instance from this processor with all outputs.
	 *
	 * @return The clone.
	 */
	public Object clone ()
	{
		ReceiveEntryNetworkActionProcessor clone =
			new ReceiveEntryNetworkActionProcessor(typeId, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}