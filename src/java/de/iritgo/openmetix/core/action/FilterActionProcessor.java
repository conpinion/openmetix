/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import java.util.LinkedList;
import java.util.List;


/**
 * FilterActionProcessor.
 *
 * With this filter you can disable the processing on a channel.
 *
 * @version $Id: FilterActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class FilterActionProcessor
	extends NetworkActionProcessor
{
	/** All filter channels. */
	private List filterChannels;

	/**
	 * Create a new FilterActionProcessor.
	 *
	 * @param typeId
	 * @param channel
	 * @param parentNetworkActionProcessor
	 */
	public FilterActionProcessor (
		String typeId, Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super(typeId, channel, parentNetworkActionProcessor);
		filterChannels = new LinkedList();
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#perform(de.iritgo.openmetix.core.action.Action, de.iritgo.openmetix.core.base.Transceiver)
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		Double channel = new Double(((ClientTransceiver) transceiver).getSender ());

		if (filterChannels.contains (channel))
		{
			return;
		}

		super.perform (action, transceiver);
	}

	/**
	 * Add a channel. All actions on this channel will be ignored.
	 *
	 * @param Channel channel
	 */
	public void addChannelToFilter (Double channel)
	{
		filterChannels.add (channel);
	}

	/**
	 * If a new channel is closed this method is called.
	 *
	 * @param channel The new channel.
	 */
	public void channelClosed (Channel channel)
	{
		filterChannels.remove (channel);
		super.channelClosed (channel);
	}

	/**
	 * Clone a new instance from this processor with all outputs.
	 *
	 * @return NetworkActionProcessor
	 */
	public Object clone ()
	{
		FilterActionProcessor clone =
			new FilterActionProcessor(typeId, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}