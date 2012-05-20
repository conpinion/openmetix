/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.network;


import de.iritgo.openmetix.core.action.Action;
import de.iritgo.openmetix.core.action.ActionProcessor;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.Transceiver;


/**
 * BlockedActionProcessor.
 *
 * @version $Id: BlockedActionProcessor.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class BlockedActionProcessor
	extends BaseObject
	implements ActionProcessor
{
	BlockedNetworkActionProcessor processor;

	public BlockedActionProcessor (BlockedNetworkActionProcessor processor)
	{
		this.processor = processor;
	}

	public void perform (Action action)
	{
		if (action.getUniqueId () == processor.getBlockedId ())
		{
			processor.resume ();
		}
	}

	public void perform (Action action, Transceiver transceiver)
	{
		perform (action);
	}

	public void close ()
	{
	}

	public Object clone ()
	{
		return null;
	}
}