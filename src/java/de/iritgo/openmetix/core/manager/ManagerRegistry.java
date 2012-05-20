/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.manager;


import java.util.HashMap;


/**
 * This registry contains all managers knwon to the system.
 *
 * @version $Id: ManagerRegistry.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ManagerRegistry
{
	/** The managers stored by its id. */
	private HashMap managers;

	/**
	 * Create a new empty manager registry.
	 */
	public ManagerRegistry ()
	{
		managers = new HashMap();
	}

	/**
	 * Add a manager to the registry.
	 *
	 * @param manager The manager to add.
	 */
	public void addManager (Manager manager)
	{
		managers.put (manager.getTypeId (), manager);
	}

	/**
	 * Retrieve a manager from the registry.
	 *
	 * @param id The id of the manager to retrieve.
	 * @return The manager or null if none was found.
	 */
	public Manager getManager (String id)
	{
		return (Manager) managers.get (id);
	}

	/**
	 * Remove a manager from the registry.
	 *
	 * @param manager The manager to remove.
	 */
	public void remove (Manager manager)
	{
		managers.remove (manager.getTypeId ());
	}
}