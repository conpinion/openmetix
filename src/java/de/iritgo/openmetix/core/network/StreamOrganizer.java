/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.io.IBufferedInputStream;
import de.iritgo.openmetix.core.io.IBufferedOutputStream;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import org.apache.avalon.framework.configuration.Configuration;
import java.io.IOException;
import java.net.Socket;


/**
 * StreamOrganizer.
 *
 * @version $Id: StreamOrganizer.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public abstract class StreamOrganizer
{
	/** Communication socket. */
	protected Socket socket;

	/** Input stream. */
	protected IBufferedInputStream streamIn;

	/** Output stream. */
	protected IBufferedOutputStream streamOut;

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 */
	public StreamOrganizer (Socket socket)
		throws IOException
	{
		Configuration config = Engine.instance ().getConfiguration ();
		Configuration socketConfig = config.getChild ("network").getChild ("socket");

		int bufSize = socketConfig.getAttributeAsInteger ("buffer", 1024 * 64);
		int timeout = socketConfig.getAttributeAsInteger ("readTimeout", 1000 * 60);

		this.socket = socket;
		this.socket.setSoTimeout (timeout);
		this.socket.setKeepAlive (true);

		if (checkOrder (socket))
		{
			streamIn = new IBufferedInputStream(socket.getInputStream (), bufSize);
			streamOut = new IBufferedOutputStream(socket.getOutputStream (), bufSize);
		}
		else
		{
			streamOut = new IBufferedOutputStream(socket.getOutputStream (), bufSize);
			streamIn = new IBufferedInputStream(socket.getInputStream (), bufSize);
		}
	}

	/**
	 * @param socket The Com. socket.
	 */
	protected boolean checkOrder (Socket socket)
		throws IOException
	{
		int port1 = socket.getLocalPort ();
		int port2 = socket.getPort ();

		if (port1 < port2)
		{
			return true;
		}
		else if (port1 > port2)
		{
			return false;
		}

		int address1 = socket.getLocalAddress ().hashCode ();
		int address2 = socket.getInetAddress ().hashCode ();

		if (address1 < address2)
		{
			return true;
		}
		else if (address1 > address2)
		{
			return false;
		}

		throw new IOException();
	}

	/**
	 * Send an object over the socket.
	 *
	 * @param object The object to send.
	 */
	public abstract void send (Object object)
		throws IOException;

	/**
	 * Receive an object from the socket.
	 *
	 * @return the received object.
	 */
	public abstract Object receive ()
		throws IOException, ClassNotFoundException, NoSuchIObjectException;

	/**
	 * Close the connection.
	 */
	public abstract void close ()
		throws IOException;

	/**
	 * Flush all data.
	 */
	public abstract void flush ()
		throws IOException;

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 * @return The new StreamOrganizer.
	 */
	public abstract StreamOrganizer create (Socket socket)
		throws IOException;
}