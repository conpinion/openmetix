/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.network;


import de.iritgo.openmetix.core.action.AbstractAction;
import de.iritgo.openmetix.core.action.Action;
import de.iritgo.openmetix.core.action.ActionProcessor;
import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.network.NetworkService;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver;
import de.iritgo.openmetix.framework.user.User;
import java.util.Iterator;


/**
 * BlockedNetworkActionProcessor.
 *
 * @version $Id: BlockedNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class BlockedNetworkActionProcessor
	extends BaseObject
	implements ActionProcessor, ShutdownObserver
{
	private ActionProcessorRegistry actionProcessorRegistry;
	private NetworkService networkBase;
	private long blockedId;
	private boolean blocked;

	public BlockedNetworkActionProcessor (
		ActionProcessorRegistry actionProcessorRegistry, NetworkService networkBase)
	{
		this.actionProcessorRegistry = actionProcessorRegistry;
		this.networkBase = networkBase;
	}

	public String getId ()
	{
		return "syncproc";
	}

	public void perform (Action action)
	{
		blocked = true;
		blockedId = action.getUniqueId ();

		try
		{
			AbstractAction networkAction = (AbstractAction) action;
			Transceiver transceiver = action.getTransceiver ();

			ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

			networkAction.setNumObjects (
				((Channel) networkBase.getConnectedChannel (clientTransceiver.getSender ())).getNumAllObjects ());

			Iterator i = clientTransceiver.getReceiverIterator ();

			while (i.hasNext ())
			{
				double channel = ((Double) i.next ()).doubleValue ();

				networkBase.send (networkAction, channel);
				networkBase.flush (channel);
			}
		}
		catch (ClassCastException x)
		{
			Log.log (
				"network", "NetworkActionProcessor.perform",
				"ClassCastException: " + x.getMessage (), Log.WARN);
		}

		try
		{
			while (blocked)
			{
				Thread.sleep (1);
			}
		}
		catch (InterruptedException x)
		{
		}
	}

	public void perform (Action action, Transceiver transceiver)
	{
		perform (action);
	}

	public long getBlockedId ()
	{
		return blockedId;
	}

	public void onShutdown ()
	{
		blocked = false;
		blockedId = 0;
	}

	public void onUserLogoff (User user)
	{
	}

	public void resume ()
	{
		blocked = false;
		blockedId = 0;
	}

	public void close ()
	{
	}

	public Object clone ()
	{
		return null;
	}
}