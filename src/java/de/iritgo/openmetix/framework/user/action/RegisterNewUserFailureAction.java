/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 * RegisterNewUserFailureAction.
 *
 * @version $Id: RegisterNewUserFailureAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class RegisterNewUserFailureAction
	extends FrameworkAction
{
	static public int USERNAME_INUSE;
	static public int EMAIL_INUSE;
	private int failure;

	public RegisterNewUserFailureAction ()
	{
	}

	public RegisterNewUserFailureAction (int failure)
	{
		this.failure = failure;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		failure = stream.readInt ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeInt (failure);
	}

	public void perform ()
	{
		Engine.instance ().getFlowControl ().ruleFailure ("userregisted");
	}
}