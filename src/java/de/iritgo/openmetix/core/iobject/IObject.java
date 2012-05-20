/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * All objects that should be transferable between the client and the
 * server, or serializeable to a backend store must implement the
 * <code>IObject</code> interface.
 *
 * @version $Id: IObject.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public interface IObject
{
	/**
	 * Get the id of the iritgo object.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId ();

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param uniqueId The new unique id.
	 */
	public void setUniqueId (long uniqueId);

	/**
	 * Get the type id of the iritgo object.
	 *
	 * @return The type id.
	 */
	public String getTypeId ();

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param typeId The new type
	 */
	public void setTypeId (String typeId);

	/**
	 * Create a new instance of the iritgo object.
	 *
	 * @return The fresh instance.
	 */
	public IObject create ();

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException;

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (OutputStream stream)
		throws IOException;
}