/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.thies;

import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.interfacing.InterfacingManager;


/**
 * This manager starts the Thies drivers at boot time.
 *
 * @version $Id: InterfacingThiesManager.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class InterfacingThiesManager extends BaseObject implements Manager{
	/**
	 * Create a new InterfacingThiesManager.
	 */
	public InterfacingThiesManager(){
		super("InterfacingThiesManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init(){
		((InterfacingManager) Engine.getManager("InterfacingManager")).startGagingSystems(new String[] {
				"Thies"
			});
	}

	/**
	 * Called when the manager is removed from the system.
	 */
	public void unload ()
	{
	}
}