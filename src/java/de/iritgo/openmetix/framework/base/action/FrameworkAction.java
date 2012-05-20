/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.action;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.AbstractAction;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * FrameworkAction.
 *
 * @version $Id: FrameworkAction.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class FrameworkAction
	extends AbstractAction
{
	protected long userUniqueId;

	public FrameworkAction ()
	{
		super();
	}

	public FrameworkAction (long userUniqueId)
	{
		super(Engine.instance ().getTransientIDGenerator ().createId ());

		this.userUniqueId = userUniqueId;
	}

	public long getUserUniqueId ()
	{
		return userUniqueId;
	}

	public void readObject (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
		userUniqueId = stream.readLong ();
		readObject (new FrameworkInputStream(stream));
	}

	public void writeObject (DataOutputStream stream)
		throws IOException
	{
		super.writeObject (stream);
		stream.writeLong (userUniqueId);
		writeObject (new FrameworkOutputStream(stream));
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException, ClassNotFoundException
	{
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
	}
}