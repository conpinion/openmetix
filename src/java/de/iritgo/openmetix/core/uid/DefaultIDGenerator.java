/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.uid;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A simple in memory id generator that looses it's state when the server shuts down.
 *
 * @version $Id: DefaultIDGenerator.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DefaultIDGenerator
	extends BaseObject
	implements IDGenerator
{
	/** The next unique id. */
	protected long id;

	/** The step increment. */
	protected long step;

	/**
	 * Create a new id generator.
	 *
	 * @param typeId The type id of this generator.
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public DefaultIDGenerator (String typeId, long start, long step)
	{
		super(typeId);
		this.id = start;
		this.step = step;
	}

	/**
	 * Create a new id generator.
	 *
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public DefaultIDGenerator (long start, long step)
	{
		super("DefaultIDGenerator");
		this.id = start;
		this.step = step;
	}

	/**
	 * Create a new id generator.
	 */
	public DefaultIDGenerator ()
	{
		this("DefaultIDGenerator", 1, 1);
	}

	/**
	 * Create a new unique id.
	 *
	 * @return The new unique id.
	 */
	public synchronized long createId ()
	{
		long nextId = id;

		id += step;

		return nextId;
	}

	/**
	 * Get the value of the next id that createId() will return.
	 *
	 * @return The next id value.
	 */
	public long peekNextId ()
	{
		return id;
	}

	/**
	 * Create a new instance of the id generator.
	 *
	 * @return The fresh instance.
	 */
	public IObject create ()
	{
		return new DefaultIDGenerator();
	}

	/**
	 * Load the last generator state.
	 */
	public void load ()
	{
	}

	/**
	 * Store the generator state.
	 */
	public void save ()
	{
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		id = dataStream.readLong ();
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (OutputStream stream)
		throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeLong (id);
	}
}