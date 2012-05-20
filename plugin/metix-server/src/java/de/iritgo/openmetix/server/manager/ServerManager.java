/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.server.manager;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.server.Server;


/**
 * @version $Id: ServerManager.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ServerManager
	extends BaseObject
	implements Manager
{
	/**
	 * Create a new ServerManager.
	 */
	public ServerManager ()
	{
		super("server");
	}

	/**
	 * Initialize the server manager.
	 */
	public void init ()
	{
		initNetworkSystem ();
	}

	private void initNetworkSystem ()
	{
		Server.instance ().createDefaultNetworkProcessingSystem ();
	}

	/**
	 * Unload the server manager from the system.
	 */
	public void unload ()
	{
	}
}