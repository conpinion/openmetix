/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.AbstractAction;
import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.user.User;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * FrameworkServerAction.
 *
 * @version $Id: FrameworkServerAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class FrameworkServerAction
	extends AbstractAction
{
	/** User id. */
	protected long userUniqueId;

	/** Action processor registry. */
	protected ActionProcessorRegistry actionProcessorRegistry;

	/**
	 * Create a new FrameworkServerAction.
	 */
	public FrameworkServerAction ()
	{
		super(Engine.instance ().getTransientIDGenerator ().createId ());

		actionProcessorRegistry = Engine.instance ().getActionProcessorRegistry ();

		AppContext appContext = AppContext.instance ();

		if (appContext != null)
		{
			User user = appContext.getUser ();

			if (user != null)
			{
				this.userUniqueId = user.getUniqueId ();
			}
		}
	}

	/**
	 * Create a new FrameworkServerAction.
	 */
	public FrameworkServerAction (long userUniqueId)
	{
		super(Engine.instance ().getTransientIDGenerator ().createId ());

		this.userUniqueId = userUniqueId;
		actionProcessorRegistry = Engine.instance ().getActionProcessorRegistry ();
	}

	/**
	 * Get the user id.
	 */
	public long getUserUniqueId ()
	{
		return userUniqueId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
		userUniqueId = stream.readLong ();
		readObject (new FrameworkInputStream(stream));
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject (DataOutputStream stream)
		throws IOException
	{
		super.writeObject (stream);
		stream.writeLong (userUniqueId);
		writeObject (new FrameworkOutputStream(stream));
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
	}
}