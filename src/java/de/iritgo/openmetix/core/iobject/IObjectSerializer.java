/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * The IObjectSerializer is used to read IObjects
 * from DataInputStreams and write IObject to
 * DataOutputStreams.
 *
 * @version $Id: IObjectSerializer.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectSerializer
	extends BaseObject
{
	/**
	 * Create a new IObjectSerializer.
	 */
	public IObjectSerializer ()
	{
	}

	/**
	 * Read an IObject from a DataInputStream.
	 *
	 * @param inputStream The input stream to read the object from.
	 * @return The read IObject.
	 * @throws IOException Is thrown if an error occurred during reading.
	 * @throws NoSuchIObjectException Is thrown if an invalid type id
	 *   was transmitted.
	 * @throws ClassNotFoundException Is thrown if an invalid type id
	 *   was transmitted.
	 */
	public IObject read (DataInputStream inputStream)
		throws IOException, NoSuchIObjectException, ClassNotFoundException
	{
		IObject object = null;
		String classId = "";

		classId = inputStream.readUTF ();

		if (classId.length () == 0)
		{
			Log.log ("system", "Prototype.get", "ClassID is NULL", Log.FATAL);

			return null;
		}

		object = Engine.instance ().getIObjectFactory ().newInstance (classId);

		if (object == null)
		{
			return null;
		}

		object.readObject (inputStream);

		if (! classId.equals (object.getTypeId ()))
		{
			Log.log ("system", "Prototype.get", "Wrong objecttype!!!!!", Log.FATAL);

			return null;
		}

		return object;
	}

	/**
	 * Write an IritogObject to a DataOutputStream.
	 *
	 * @param outputStream The output stream to write to.
	 * @param object The IritogObject to write.
	 * @throws IOException Is thrown if an error occurred during writing.
	 */
	public void write (DataOutputStream outputStream, IObject object)
		throws IOException
	{
		synchronized (outputStream)
		{
			outputStream.writeUTF (object.getTypeId ());
			object.writeObject (outputStream);
		}
	}
}