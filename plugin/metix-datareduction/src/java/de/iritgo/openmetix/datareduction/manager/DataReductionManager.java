/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.manager;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.datareduction.DataReduction;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;


/**
 * @version $Id: DataReductionManager.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DataReductionManager
	extends BaseObject
	implements Manager, IObjectModifiedListener
{
	public DataReductionManager ()
	{
		super("DataReduction");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		System.out.println ("DataReductionManager gestartet.");

		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);
		Engine.instance ().getEventRegistry ().addListener ("objectcreated", this);
		Engine.instance ().getEventRegistry ().addListener ("objectremoved", this);
	}

	/**
	 * Unload the manager from the system.
	 */
	public void unload ()
	{
	}

	/**
	 * Called when an iobject was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		if (event.getModifiedObject () instanceof DataReduction)
		{
			startReduction ((DataReduction) event.getModifiedObject ());
		}
	}

	/**
	 * Extract the information of the dataobject and starts the data reduction.
	 *
	 * @param dataobject
	 */
	public void startReduction (DataReduction dataobject)
	{
		System.out.println ("Start der Datenverdichtung durch den Manager");
	}
}