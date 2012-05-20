/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.exporter.manager;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;


/**
 * ExporterManager.
 */
public class ExporterManager
	extends BaseObject
	implements Manager
{
	public ExporterManager ()
	{
		super("ExporterManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
	}

	/**
	 * Called when a manager is unloaded from the system.
	 */
	public void unload ()
	{
	}
}