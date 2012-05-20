/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.user.User;
import java.io.IOException;


/**
 * RegisterNewUserAction.
 *
 * @version $Id: RegisterNewUserAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RegisterNewUserAction
	extends FrameworkAction
{
	private String userName;
	private String email;
	private String password;

	public RegisterNewUserAction ()
	{
	}

	public RegisterNewUserAction (
		String userName, String email, long userUniqueId, String password)
	{
		super(userUniqueId);

		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public String getTypeId ()
	{
		return "action.registernewuser";
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
		password = stream.readUTF ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (email);
		stream.writeUTF (password);
	}

	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		User user = new User(userName, email, 0, password, clientTransceiver.getSender ());

		AppContext appContext = AppContext.instance ();

		appContext.setUser (user);

		Engine.instance ().getFlowControl ().ruleSuccess ("userregisted");
	}
}