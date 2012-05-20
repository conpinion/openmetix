/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.Transceiver;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Actions are primarily used to trigger some remote functionality.
 * A client can for example execute an action on the server, or the
 * server can execute actions on the clients.
 *
 * @version $Id: Action.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class Action
	extends BaseObject
	implements IObject
{
	/** The action transceiver. */
	protected Transceiver transceiver;

	/**
	 * Create a new action.
	 */
	public Action ()
	{
	}

	/**
	 * Create a new action.
	 *
	 * @param uniqueId The unique id of the action.
	 */
	public Action (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Create a new action instance.
	 */
	public IObject create ()
	{
		try
		{
			IObject newAction = (IObject) getClass ().newInstance ();

			newAction.setTypeId (getTypeId ());

			return newAction;
		}
		catch (Exception x)
		{
			Log.logError (
				"system", "Action",
				"Cannot create instance for action '" + getTypeId () + "': " + x.toString ());

			return null;
		}
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		readObject (new DataInputStream(stream));
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (OutputStream stream)
		throws IOException
	{
		writeObject (new DataOutputStream(stream));
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
		uniqueId = stream.readLong ();
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (DataOutputStream stream)
		throws IOException
	{
		stream.writeLong (uniqueId);
	}

	/**
	 * Perform the action.
	 * Subclasses should override this method to provide their custom
	 * action code.
	 */
	public void perform ()
	{
	}

	/**
	 * Check wether the action can be performed or not.
	 * By default actions are executable. Subclasses should provide a
	 * reasonable implementation of this method.
	 *
	 * @return True if the action can be performed.
	 */
	public boolean canPerform ()
	{
		return true;
	}

	/**
	 * Set the transceiver for this action.
	 *
	 * @param transceiver The new transceiver.
	 */
	public void setTransceiver (Transceiver transceiver)
	{
		this.transceiver = transceiver;
	}

	/**
	 * Get the transceiver of this action.
	 *
	 * @return The transceiver.
	 */
	public Transceiver getTransceiver ()
	{
		return transceiver;
	}
}