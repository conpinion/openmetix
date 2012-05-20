/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.network.ClientTransceiver;
import java.io.IOException;


/**
 * AliveCheckServerAction.
 *
 * @version $Id: AliveCheckServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class AliveCheckServerAction
	extends NetworkFrameworkServerAction
{
	public static final int SERVER = 1;
	public static final int CLIENT = 2;
	private int source;

	public AliveCheckServerAction ()
	{
	}

	public AliveCheckServerAction (int source)
	{
		this.source = source;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		source = stream.readInt ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeInt (source);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		if (source == CLIENT)
		{
			clientTransceiver.addReceiver (clientTransceiver.getSender ());

			return (FrameworkAction) new AliveCheckAction(source);
		}

		clientTransceiver.getConnectedChannel ().setAliveCheckSent (false);

		return null;
	}
}