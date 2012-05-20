/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.uid.IDGenerator;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.NewUserEvent;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.io.IOException;


/**
 * RegisterNewUserServerAction.
 *
 * @version $Id: RegisterNewUserServerAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RegisterNewUserServerAction
	extends NetworkFrameworkServerAction
{
	private String userName;
	private String email;

	public RegisterNewUserServerAction ()
	{
		super(-1);
	}

	public RegisterNewUserServerAction (String userName, String email)
	{
		super(-1);

		this.userName = userName;
		this.email = email;
	}

	public String getTypeId ()
	{
		return "server.action.registernewuser";
	}

	public String getUserName ()
	{
		return userName;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		email = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (email);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		IDGenerator g = Server.instance ().getApplicationIdGenerator ();

		long userUniqueId = g.getUniqueId ();

		String password = "nix";

		User user =
			new User(userName, email, userUniqueId, password, clientTransceiver.getSender ());

		clientTransceiver.addReceiver (clientTransceiver.getSender ());

		if (userRegistry.getUser (userName) != null)
		{
			return (FrameworkAction) new RegisterNewUserFailureAction(
				RegisterNewUserFailureAction.USERNAME_INUSE);
		}

		if (userRegistry.getUserByEMail (email) != null)
		{
			return (FrameworkAction) new RegisterNewUserFailureAction(
				RegisterNewUserFailureAction.EMAIL_INUSE);
		}

		userRegistry.addUser (user);

		Engine.instance ().getBaseRegistry ().add (user);

		Engine.instance ().getEventRegistry ().fire ("newuser", new NewUserEvent(user));

		return (FrameworkAction) new RegisterNewUserAction(userName, email, userUniqueId, password);
	}
}