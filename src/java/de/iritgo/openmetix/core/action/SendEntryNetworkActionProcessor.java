/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.network.Channel;


/**
 * SendNetworkActionProcessor.
 *
 * @version $Id: SendEntryNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class SendEntryNetworkActionProcessor
	extends NetworkActionProcessor
{
	/**
	 * Create a new SendEntryNetworkActionProcessor.
	 *
	 * @param typeId
	 * @param channel
	 * @param parentNetworkActionProcessor
	 */
	public SendEntryNetworkActionProcessor (
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
		SendEntryNetworkActionProcessor clone =
			new SendEntryNetworkActionProcessor(typeId, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}