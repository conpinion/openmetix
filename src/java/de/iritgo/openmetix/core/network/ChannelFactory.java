/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.thread.Threadable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A ChannelFactory is a threadable object that waits for network connections
 * on a tcp port. If a connection is established, it creates a new connected channel
 * and adds it to the NetworkService.
 *
 * @version $Id: ChannelFactory.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ChannelFactory
	extends Threadable
{
	/** The hostname of the system on which the server runs. */
	private String hostName = "localhost";

	/** The port number to listen on. */
	private int port;

	/** The server socket. */
	private ServerSocket serverSocket;

	/** The network service. */
	private NetworkService networkService;

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory (NetworkService networkService, int port, int timeout)
		throws IOException
	{
		this(networkService, "localhost", port, timeout);
	}

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param hostName The name of the server host.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory (NetworkService networkService, String hostName, int port, int timeout)
		throws IOException
	{
		super("ServerSocket [" + (hostName == null ? "localhost" : hostName) + ":" + port + "]");

		this.networkService = networkService;
		this.port = port;
		this.hostName = hostName;

		serverSocket = new ServerSocket(port);

		serverSocket.setSoTimeout (timeout);
	}

	/**
	 * Execute the Threadable.
	 *
	 * This method waits for connections on the server socket and creates new
	 * ConnectedChannels if a connection was established.
	 */
	public void run ()
	{
		try
		{
			Log.logVerbose ("network", "ChannelFactory.run", "Waiting for Connections");

			Socket s = serverSocket.accept ();

			networkService.addConnectedChannel (new Channel(s, networkService));
			Log.logDebug ("network", "ChannelFactory.run", "Connection accepted");
		}
		catch (IOException e)
		{
		}

		setState (Threadable.FREE);
	}
}