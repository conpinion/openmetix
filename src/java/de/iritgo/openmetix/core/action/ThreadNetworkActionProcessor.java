/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.thread.Threadable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ThreadNetworkActionProcessor.
 *
 * @version $Id: ThreadNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ThreadNetworkActionProcessor
	extends Threadable
	implements ActionProcessor, NetworkActionProcessorInterface, Cloneable
{
	/** All channel processors. */
	protected HashMap channelProcessorMapping;

	/** All channel processors. */
	protected List channelProcessors;

	/** Our channel. */
	protected Channel channel;

	/** Parent processor. */
	protected NetworkActionProcessorInterface parentNetworkActionProcessor;

	/** All actions. */
	protected List actions;

	/** Lock object. */
	protected Object listLock;

	/**
	 * Create a new ThreadNetworkActionProcessor.
	 *
	 * @param Channel The channel for this action processor.
	 * @param NetworlActionProcessor The parent network action processor.
	 */
	public ThreadNetworkActionProcessor (
		String typeId, Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super(typeId);

		channelProcessorMapping = new HashMap();
		channelProcessors = new LinkedList();
		this.channel = channel;
		this.parentNetworkActionProcessor = parentNetworkActionProcessor;
		actions = new LinkedList();
		listLock = new Object();
		Engine.instance ().getThreadService ().add (this);
	}

	/**
	 * Set the channel for this thread action processor.
	 *
	 * @param channel The network channel.
	 */
	public void setChannel (Channel channel)
	{
		this.channel = channel;
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#init()
	 */
	public void init ()
	{
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#newChannelCreated(de.iritgo.openmetix.core.network.Channel)
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
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#channelClosed(de.iritgo.openmetix.core.network.Channel)
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
	 * @see de.iritgo.openmetix.core.thread.Threadable#run()
	 */
	public void run ()
	{
		Action action = null;

		synchronized (listLock)
		{
			if (actions.size () > 0)
			{
				action = (Action) actions.get (0);
				actions.remove (action);
			}
		}

		if (action != null)
		{
			performAction (action, action.getTransceiver ());
		}

		synchronized (listLock)
		{
			if ((actions.size () == 0))
			{
				try
				{
					listLock.wait ();
				}
				catch (InterruptedException x)
				{
				}
			}
		}
	}

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform (Action action)
	{
		perform (action, action.getTransceiver ());
	}

	/**
	 * Add an action with a transceiver to the thread network action processor.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		synchronized (listLock)
		{
			actions.add (action);
			listLock.notify ();
		}
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void performAction (Action action, Transceiver transceiver)
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
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#addOutput(de.iritgo.openmetix.core.network.Channel, de.iritgo.openmetix.core.action.ActionProcessor)
	 */
	public void addOutput (Channel channel, ActionProcessor actionProcessor)
	{
		channelProcessorMapping.put (channel, actionProcessor);
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#removeOutput(de.iritgo.openmetix.core.network.Channel)
	 */
	public void removeOutput (Channel channel)
	{
		channelProcessorMapping.remove (channel);
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#addOutput(de.iritgo.openmetix.core.action.ActionProcessor)
	 */
	public void addOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.add (actionProcessor);
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#removeOutput(de.iritgo.openmetix.core.action.ActionProcessor)
	 */
	public void removeOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.remove (actionProcessor);
	}

	/**
	 * Check if an action is currently processed.
	 *
	 * @return True if an action is currently processed.
	 */
	public boolean actionsInProcess ()
	{
		return actions.size () > 0;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone ()
	{
		ThreadNetworkActionProcessor clone =
			new ThreadNetworkActionProcessor(typeId, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}

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
	 * @see de.iritgo.openmetix.core.thread.Threadable#dispose()
	 */
	public void dispose ()
	{
		synchronized (listLock)
		{
			setState (Threadable.CLOSING);
			listLock.notify ();
		}
	}

	/**
	 * @see de.iritgo.openmetix.core.action.NetworkActionProcessorInterface#close()
	 */
	public void close ()
	{
		dispose ();
	}
}