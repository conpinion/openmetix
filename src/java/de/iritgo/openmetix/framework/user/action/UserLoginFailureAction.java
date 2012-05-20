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
 * UserLoginFailureAction.
 *
 * @version $Id: UserLoginFailureAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class UserLoginFailureAction
	extends FrameworkAction
{
	/** Failure type if the username or password was wrong. */
	static public int BAD_USERNAME_OR_PASSWORD = 1;

	/** Failure type if the user is already logged in. */
	static public int USER_ALREADY_ONLINE = 2;

	/** Failure type. */
	private int failure;

	/**
	 * Create a new UserLoginFailureAction.
	 */
	public UserLoginFailureAction ()
	{
	}

	/**
	 * Create a new UserLoginFailureAction.
	 *
	 * @param failure The failure type.
	 */
	public UserLoginFailureAction (int failure)
	{
		this.failure = failure;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
		failure = stream.readInt ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeInt (failure);
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		Engine.instance ().getFlowControl ().ruleFailure ("UserLogin", new Integer(failure));
	}

	/**
	 * Get the failure type.
	 *
	 * @return The failure type.
	 */
	public int getFailure ()
	{
		return failure;
	}
}