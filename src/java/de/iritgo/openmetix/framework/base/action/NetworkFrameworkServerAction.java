/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.user.User;
import java.io.IOException;


/**
 * NetworkFrameworkServerAction.
 *
 * @version $Id: NetworkFrameworkServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class NetworkFrameworkServerAction
	extends FrameworkServerAction
{
	public NetworkFrameworkServerAction ()
	{
		super();
	}

	public NetworkFrameworkServerAction (long userUniqueId)
	{
		super(userUniqueId);
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		FrameworkAction action = getAction (clientTransceiver);

		if (action == null)
		{
			return;
		}

		action.setTransceiver (clientTransceiver);

		action.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (action);
	}

	public User getUser ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		return (User) clientTransceiver.getConnectedChannel ().getCustomContextObject ();
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		return null;
	}
}