/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserEvent;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.io.IOException;


/**
 * UserLoginAction.
 *
 * @version $Id: UserLoginAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class UserLoginAction
	extends FrameworkAction
{
	private User user;

	public UserLoginAction ()
	{
		super(-1);
		user = new User();
	}

	public UserLoginAction (User user)
	{
		super(user.getUniqueId ());
		this.user = user;
	}

	public String getTypeId ()
	{
		return "action.userlogin";
	}

	public String getUserName ()
	{
		return user.getName ();
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		user.readObject (stream);
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		user.writeObject (stream);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		UserRegistry userRegistry = Client.instance ().getUserRegistry ();

		user.setNetworkChannel (clientTransceiver.getSender ());
		user.setOnline (true);

		userRegistry.addUser (user);

		AppContext appContext = AppContext.instance ();

		appContext.setUser (user);

		FrameworkProxy userProxy = new FrameworkProxy(user);

		Engine.instance ().getProxyRegistry ().addProxy (userProxy);
		Engine.instance ().getBaseRegistry ().add (user);

		Engine.instance ().getFlowControl ().ruleSuccess ("UserLogin");

		Engine.instance ().getEventRegistry ().fire ("UserLogin", new UserEvent(user, true));
	}
}