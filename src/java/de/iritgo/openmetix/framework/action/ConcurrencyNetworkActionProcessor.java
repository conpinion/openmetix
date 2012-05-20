/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.action;


import de.iritgo.openmetix.core.action.ActionProcessor;
import de.iritgo.openmetix.core.action.NetworkActionProcessor;
import de.iritgo.openmetix.core.action.NetworkActionProcessorInterface;
import de.iritgo.openmetix.core.action.ThreadNetworkActionProcessor;
import de.iritgo.openmetix.core.network.Channel;


/**
 * ConcurrencyNetworkActionProcessor
 *
 * @version $Id: ConcurrencyNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ConcurrencyNetworkActionProcessor
	extends NetworkActionProcessor
{
	private ThreadNetworkActionProcessor threadNetworkActionProcessor;

	/**
	 * Default Constructor
	 *
	 * @param Channel The channel for this processor
	 * @param NetworkActionProcessor The network action processor
	 * @param ThreadNetworkActionProcessor The thread network action processor
	 */
	public ConcurrencyNetworkActionProcessor (
		Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super("Server.ConcurrencyNetworkActionProcessor", channel, parentNetworkActionProcessor);
	}

	/**
	 * Default Constructor
	 *
	 * @param Channel The channel for this processor
	 * @param NetworkActionProcessor The network action processor
	 * @param ThreadNetworkActionProcessor The thread network action processor
	 */
	public ConcurrencyNetworkActionProcessor (
		Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor,
		ThreadNetworkActionProcessor threadNetworkActionProcessor)
	{
		super("Server.ConcurrencyNetworkActionProcessor", channel, parentNetworkActionProcessor);
		this.threadNetworkActionProcessor = threadNetworkActionProcessor;
	}

	/**
	 * Helper method for inital create.
	 *
	 * @param ThreadNetworkActionProcessor
	 */
	public void setThreadNetworkActionProcessor (
		ThreadNetworkActionProcessor threadNetworkActionProcessor)
	{
		this.threadNetworkActionProcessor = threadNetworkActionProcessor;
	}

	/**
	 * If a new channel is created this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void newChannelCreated (Channel channel)
	{
		addOutput (channel, (ActionProcessor) threadNetworkActionProcessor.clone ());
		super.newChannelCreated (channel);
	}

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void channelClosed (Channel channel)
	{
		((NetworkActionProcessorInterface) channelProcessorMapping.get (channel)).channelClosed (
			channel);
		((NetworkActionProcessorInterface) channelProcessorMapping.get (channel)).close ();
		removeOutput (channel);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	public Object clone ()
	{
		ConcurrencyNetworkActionProcessor clone =
			new ConcurrencyNetworkActionProcessor(
				channel, parentNetworkActionProcessor, threadNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}