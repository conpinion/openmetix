/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;


import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 * NetworkSystemListener.
 *
 * @version $Id: NetworkSystemListener.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public interface NetworkSystemListener
{
	/**
	 * @param networkBase
	 * @param connectedChannel
	 */
	public void connectionEstablished (NetworkService networkBase, Channel connectedChannel);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 */
	public void connectionTerminated (NetworkService networkBase, Channel connectedChannel);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (
		NetworkService networkBase, Channel connectedChannel, NoSuchIObjectException x);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (
		NetworkService networkBase, Channel connectedChannel, SocketTimeoutException x);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (
		NetworkService networkBase, Channel connectedChannel, ClassNotFoundException x);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (NetworkService networkBase, Channel connectedChannel, EOFException x);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (NetworkService networkBase, Channel connectedChannel, SocketException x);

	/**
	 * @param networkBase
	 * @param connectedChannel
	 * @param x
	 */
	public void error (NetworkService networkBase, Channel connectedChannel, IOException x);
}