/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.manager;



/**
 * Managers are used to provide services to the application.
 *
 * @version $Id: Manager.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface Manager
{
	/**
	 * Get the id of the manager.
	 *
	 * @return The manager id.
	 */
	public String getTypeId ();

	/**
	 * Initialize the manager.
	 */
	public void init ();

	/**
	 * Called when a manager is unloaded from the system.
	 */
	public void unload ();
}