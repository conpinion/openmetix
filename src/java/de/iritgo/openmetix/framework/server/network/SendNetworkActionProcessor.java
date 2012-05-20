/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server.network;


import de.iritgo.openmetix.core.action.AbstractAction;
import de.iritgo.openmetix.core.action.Action;
import de.iritgo.openmetix.core.action.NetworkActionProcessor;
import de.iritgo.openmetix.core.action.NetworkActionProcessorInterface;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.BroadcastTransceiver;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.network.NetworkService;
import java.util.Iterator;


/**
 * SendNetworkActionProcessor.
 *
 * @version $Id: SendNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class SendNetworkActionProcessor
	extends NetworkActionProcessor
{
	private NetworkService networkService;

	public SendNetworkActionProcessor (
		NetworkService networkService, Channel channel,
		NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super("Server.SendNetworkActionProcessor", channel, parentNetworkActionProcessor);
		this.networkService = networkService;
	}

	public String getId ()
	{
		return "Server.SendNetworkActionProcessor";
	}

	public void perform (Action action)
	{
		perform (action, action.getTransceiver ());
	}

	public void perform (Action action, Transceiver transceiver)
	{
		AbstractAction networkAction = (AbstractAction) action;

		try
		{
			ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

			Channel channel =
				(Channel) networkService.getConnectedChannel (clientTransceiver.getSender ());

			if (channel == null)
			{
				return;
			}

			networkAction.setNumObjects (channel.getNumAllObjects ());

			Iterator i = clientTransceiver.getReceiverIterator ();

			while (i.hasNext ())
			{
				double channelNumber = ((Double) i.next ()).doubleValue ();

				networkService.send (networkAction, channelNumber);
				networkService.flush (channelNumber);
			}

			return;
		}
		catch (ClassCastException x)
		{
			BroadcastTransceiver clientTransceiver = (BroadcastTransceiver) transceiver;

			networkService.sendBroadcast (networkAction);
		}
	}

	public Object clone ()
	{
		SendNetworkActionProcessor clone =
			new SendNetworkActionProcessor(networkService, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}