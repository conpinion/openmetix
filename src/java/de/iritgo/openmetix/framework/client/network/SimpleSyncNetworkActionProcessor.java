/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.network;


import de.iritgo.openmetix.core.action.Action;
import de.iritgo.openmetix.core.action.NetworkActionProcessor;
import de.iritgo.openmetix.core.action.NetworkActionProcessorInterface;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.network.Channel;
import de.iritgo.openmetix.framework.appcontext.AppContext;


/**
 * SimpleSyncNetworkActionProcessor.
 *
 * @version $Id: SimpleSyncNetworkActionProcessor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SimpleSyncNetworkActionProcessor
	extends NetworkActionProcessor
{
	public SimpleSyncNetworkActionProcessor (
		Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super("SimpleSyncNetworkActionProcessor", channel, parentNetworkActionProcessor);
	}

	public void perform (Action action, Transceiver transceiver)
	{
		Object lockObject = AppContext.instance ().getLockObject ();

		synchronized (lockObject)
		{
			super.perform (action, transceiver);
		}
	}

	public Object clone ()
	{
		SimpleSyncNetworkActionProcessor clone =
			new SimpleSyncNetworkActionProcessor(channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}