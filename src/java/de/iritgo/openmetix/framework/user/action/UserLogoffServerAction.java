/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.FilterActionProcessor;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.base.action.FrameworkServerAction;


/**
 * UserLogoffServerAction.
 *
 * @version $Id: UserLogoffServerAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class UserLogoffServerAction
	extends FrameworkServerAction
{
	public UserLogoffServerAction ()
	{
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		Double channel = new Double(clientTransceiver.getSender ());
		FilterActionProcessor filterActionProcessor =
			(FilterActionProcessor) Engine.instance ().getActionProcessorRegistry ().get (
				"Server.FilterActionProcessor");

		UserLogoffAction userLogoffAction = new UserLogoffAction();

		clientTransceiver.addReceiver (clientTransceiver.getSender ());
		userLogoffAction.setTransceiver (clientTransceiver);
		ActionTools.sendToClient (userLogoffAction);

		filterActionProcessor.addChannelToFilter (channel);
	}
}