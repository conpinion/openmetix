/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * NetworkActionProcessors are used to perform actions. Actions are not executed directly,
 * because we want some basic conditions to hold, for example we want to
 * synchronize the actions among the clients. Network action processors have outputs to deliver the
 * action to the next processor.
 *
 * @version $Id: NetworkActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public abstract class NetworkActionProcessor
	extends BaseObject
	implements ActionProcessor, NetworkActionProcessorInterface, Cloneable
{
	protected HashMap channelProcessorMapping;

	/** All channel processors. */
	protected List channelProcessors;

	/** Our network channel. */
	protected Channel channel;

	/** The parent processor. */
	protected NetworkActionProcessorInterface parentNetworkActionProcessor;

	/**
	 * Create a new NetworkActionProcessor.
	 *
	 * @param Channel The channel for this action processor
	 * @param NetworlActionProcessor The parent network action processor.
	 */
	public NetworkActionProcessor (
		String typeId, Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super(typeId);

		channelProcessorMapping = new HashMap();
		channelProcessors = new LinkedList();
		this.channel = channel;
		this.parentNetworkActionProcessor = parentNetworkActionProcessor;
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#init()
	 */
	public void init ()
	{
	}

	/**
	 * If a new channel is created this method is called.
	 *
	 * @param channel The new channel.
	 */
	public void newChannelCreated (Channel channel)
	{
		for (Iterator i = channelProcessorMapping.values ().iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).newChannelCreated (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).newChannelCreated (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * If a new channel is closed this method is called.
	 *
	 * @param channel The new channel.
	 */
	public void channelClosed (Channel channel)
	{
		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) channelProcessorMapping.get (i.next ())).channelClosed (
					channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).channelClosed (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#perform(de.iritgo.openmetix.core.action.Action)
	 */
	public void perform (Action action)
	{
		perform (action, action.getTransceiver ());
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#perform(de.iritgo.openmetix.core.action.Action, de.iritgo.openmetix.core.base.Transceiver)
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		Channel channel = ((ClientTransceiver) transceiver).getConnectedChannel ();

		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		{
			Channel channelProcessor = (Channel) i.next ();

			if (channelProcessor == channel)
			{
				((ActionProcessor) channelProcessorMapping.get (channel)).perform (
					action, transceiver);
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((ActionProcessor) i.next ()).perform (action, transceiver);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Add an output action processor to this network action processor
	 * and the specified channel.
	 *
	 * @param channel The channel.
	 * @param actionProcessor The action processor.
	 */
	public void addOutput (Channel channel, ActionProcessor actionProcessor)
	{
		channelProcessorMapping.put (channel, actionProcessor);
	}

	/**
	 * Remove an action processor from the output and the
	 * specified channel.
	 *
	 * @param channel The channel.
	 */
	public void removeOutput (Channel channel)
	{
		channelProcessorMapping.remove (channel);
	}

	/**
	 * Add an output action processor to this network action processor.
	 * This processor is called for all channels.
	 *
	 * @param actionProcessor The action processor
	 */
	public void addOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.add (actionProcessor);
	}

	/**
	 * Remove an output.
	 *
	 * @param actionProcessor The action processor.
	 */
	public void removeOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.remove (actionProcessor);
	}

	/**
	 * Clone a new instance from this processor with all outputs.
	 *
	 * @return NetworkActionProcessor
	 */
	public abstract Object clone ();

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#cloneOutputs(de.iritgo.openmetix.core.action.NetworkActionProcessorInterface)
	 */
	public void cloneOutputs (NetworkActionProcessorInterface clone)
	{
		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		{
			try
			{
				Channel channel = (Channel) i.next ();

				clone.addOutput (
					channel,
					(ActionProcessor) ((ActionProcessor) channelProcessorMapping.get (channel)).clone ());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				clone.addOutput ((ActionProcessor) ((ActionProcessor) i.next ()).clone ());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#close()
	 */
	public void close ()
	{
	}
}