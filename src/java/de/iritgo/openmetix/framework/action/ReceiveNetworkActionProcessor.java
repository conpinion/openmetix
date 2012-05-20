/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.action;


import de.iritgo.openmetix.core.action.Action;
import de.iritgo.openmetix.core.action.NetworkActionProcessor;
import de.iritgo.openmetix.core.action.NetworkActionProcessorInterface;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;


/**
 * ReceiveNetworkActionProcessor
 *
 * @version $Id: ReceiveNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ReceiveNetworkActionProcessor
	extends NetworkActionProcessor
{
	public ReceiveNetworkActionProcessor (
		Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super("ReceiveNetworkActionProcessor", channel, parentNetworkActionProcessor);
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		action.setTransceiver (transceiver);
		super.perform (action, transceiver);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	public Object clone ()
	{
		ReceiveNetworkActionProcessor clone =
			new ReceiveNetworkActionProcessor(channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}