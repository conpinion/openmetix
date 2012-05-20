/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectSerializer;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * ObjectStream.
 *
 * @version $Id: ObjectStream.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ObjectStream
	extends StreamOrganizer
{
	/** The object protocol. */
	private IObjectSerializer objectSerializer;

	/** Data input stream wrapped around the socket stream. */
	private DataInputStream in;

	/** Data output stream wrapped around the socket stream. */
	private DataOutputStream out;

	/**
	 * Create a new ObjectStream.
	 *
	 * @param socket The communication socket.
	 */
	public ObjectStream (Socket socket)
		throws IOException
	{
		super(socket);

		objectSerializer = new IObjectSerializer();

		in = new DataInputStream(streamIn);
		out = new DataOutputStream(streamOut);
	}

	/**
	 * Send an object over the socket.
	 *
	 * @param object The object to send.
	 */
	public void send (Object object)
		throws IOException
	{
		IObject sendObject = (IObject) object;

		synchronized (out)
		{
			objectSerializer.write (out, sendObject);
		}
	}

	/**
	 * Receive an object from the socket.
	 *
	 * @return the received object.
	 */
	public Object receive ()
		throws IOException, ClassNotFoundException, NoSuchIObjectException
	{
		IObject object;

		object = objectSerializer.read (in);
		Log.logDebug ("system", "ObjectStream.receive", "Object created and received: " + object);

		return object;
	}

	/**
	 * Close the connection.
	 */
	public void close ()
		throws IOException
	{
		if (! socket.isClosed ())
		{
			socket.shutdownInput ();
			socket.shutdownOutput ();
			socket.close ();
		}

		out.close ();
		in.close ();
	}

	/**
	 * Flush all data.
	 */
	public void flush ()
		throws IOException
	{
		out.flush ();
	}

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 * @return The new StreamOrganizer.
	 */
	public StreamOrganizer create (Socket socket)
		throws IOException
	{
		return new ObjectStream(socket);
	}
}