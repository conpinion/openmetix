/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;


/**
 * NetworkActionProcessors are used to perform actions. Actions are not executed directly,
 * because we want some basic conditions to hold, for example we want to
 * synchronize the actions among the clients. Network action processors have outputs to deliver the
 * action to the next processor.
 *
 * @version $Id: NetworkActionProcessorInterface.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public interface NetworkActionProcessorInterface
{
	/**
	 * Init a network action processor.
	 */
	public abstract void init ();

	/**
	 * If a new channel is created this method is called.
	 *
	 * @param channel The new channel.
	 */
	public abstract void newChannelCreated (Channel channel);

	/**
	 * If a new channel is closed this method is called.
	 *
	 * @param channel The new channel.
	 */
	public abstract void channelClosed (Channel channel);

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public abstract void perform (Action action);

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public abstract void perform (Action action, Transceiver transceiver);

	/**
	 * Add a output action processor to this network action processor
	 * and the specified channel.
	 *
	 * @param channel The channel.
	 * @param actionProcessor The action processor.
	 */
	public abstract void addOutput (Channel channel, ActionProcessor actionProcessor);

	/**
	 * Remove an action processor from the output and the specified channel.
	 *
	 * @param channel The channel.
	 */
	public abstract void removeOutput (Channel channel);

	/**
	 * Add a output action processor to this network action processor.
	 * This processor is called for all channels.
	 *
	 * @param actionProcessor The action processor
	 */
	public abstract void addOutput (ActionProcessor actionProcessor);

	/**
	 * Remove an action processor from the output.
	 *
	 * @param actionProcessor The action processor.
	 */
	public abstract void removeOutput (ActionProcessor actionProcessor);

	/**
	 * Close a network action processor.
	 */
	public abstract void close ();

	/**
	 * Clone all outputs.
	 *
	 * @param NetworkActionProcessorInterface
	 */
	public void cloneOutputs (NetworkActionProcessorInterface clone);
}