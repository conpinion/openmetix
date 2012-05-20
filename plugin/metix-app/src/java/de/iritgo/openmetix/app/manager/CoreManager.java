/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.manager;


import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.GagingStationRegistry;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.command.CommandTools;
import java.util.Iterator;
import java.util.Properties;


/**
 * The core manager loads and initializes the gaging stations.
 *
 * @version $Id: CoreManager.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class CoreManager
	extends BaseObject
	implements Manager
{
	/** The gaging station registry. */
	private GagingStationRegistry gagingStationRegistry;

	/**
	 * Create a new CoreManager.
	 */
	public CoreManager ()
	{
		super("CoreManager");
	}

	/**
	 * Initialize the core manager.
	 */
	public void init ()
	{
		Properties props = new Properties();

		props.put ("type", "GagingStationRegistry");
		props.put ("id", new Long(11002));
		CommandTools.performSimple ("persist.LoadObject", props);

		gagingStationRegistry =
			(GagingStationRegistry) Engine.instance ().getBaseRegistry ().get (11002);

		GagingStationManager gagingStationManager =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		if (gagingStationRegistry != null)
		{
			addChildSensorsAsMeasurementAgents ();
		}
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Retrieve the gaging station registry.
	 *
	 * @return THe gaging station registry.
	 */
	public GagingStationRegistry getGagingStationRegistry ()
	{
		return gagingStationRegistry;
	}

	/**
	 * Add all chained sensors of any sensor as measurment agents.
	 */
	private void addChildSensorsAsMeasurementAgents ()
	{
		GagingStationManager stationManager =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		for (Iterator i = gagingStationRegistry.gagingStationIterator (); i.hasNext ();)
		{
			GagingStation station = (GagingStation) i.next ();

			stationManager.addGagingStation (station);
		}
	}
}