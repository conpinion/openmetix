/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.manager;


import de.iritgo.openmetix.framework.client.gui.ClientGUI;


/**
 * ClientManager.
 *
 * @version $Id: ClientManager.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface ClientManager
{
	/**
	 * Return the client gui.
	 *
	 * @return The client gui.
	 */
	public ClientGUI getClientGUI ();
}