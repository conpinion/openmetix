/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.action;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Absract base class for actions that provides some basic functionality.
 *
 * @version $Id: AbstractAction.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public abstract class AbstractAction
	extends Action
{
	/** Action time stamp. */
	private double timeStamp;

	/** Number of objects that are transferred by this action. */
	private int numObjects;

	/**
	 * Create a new AbstractAction.
	 */
	public AbstractAction ()
	{
	}

	/**
	 * Create a new AbstractAction.
	 *
	 * @param uniqueId The unique id of the action.
	 */
	public AbstractAction (long uniqueId)
	{
		super(uniqueId);
	}

	/**
	 * Set the action time stamp.
	 */
	public void setTimeStamp (double timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * Get the action time stamp.
	 */
	public double getTimeStamp ()
	{
		return timeStamp;
	}

	/**
	 * Set the number of transferred objects.
	 *
	 * @param numObjects The new number of transferred objects.
	 */
	public void setNumObjects (int numObjects)
	{
		this.numObjects = numObjects;
	}

	/**
	 * Get the number of transferred objects.
	 *
	 * @return The number of transferred objects.
	 */
	public int getNumObjects ()
	{
		return numObjects;
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The data input stream.
	 */
	public void readObject (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
		timeStamp = stream.readDouble ();
		numObjects = stream.readInt ();
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The data output stream.
	 */
	public void writeObject (DataOutputStream stream)
		throws IOException
	{
		super.writeObject (stream);
		stream.writeDouble (timeStamp);
		stream.writeInt (numObjects);
	}
}