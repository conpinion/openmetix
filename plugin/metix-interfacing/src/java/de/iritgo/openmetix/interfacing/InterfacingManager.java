/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverThread;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import de.iritgo.openmetix.interfacing.link.Interface;
import de.iritgo.openmetix.interfacing.link.InterfaceDriver;
import de.iritgo.openmetix.interfacing.link.InterfaceDriverDescriptor;
import de.iritgo.openmetix.interfacing.link.SerialLink;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * The interfacing manager is used to start and stop the gaging system
 * threads.
 *
 * @version $Id: InterfacingManager.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class InterfacingManager
	extends BaseObject
	implements Manager, ShutdownObserver, IObjectModifiedListener
{
	/** This registry contains all gaging systems, driver configurations, etc. */
	private InterfaceRegistry interfaceRegistry;

	/** All running interface driver threads. */
	private Map driverThreads;

	/** All communication links. */
	private Map interfaceDrivers;

	/**
	 * Create a new InterfacingManager.
	 */
	public InterfacingManager ()
	{
		super("InterfacingManager");
		driverThreads = new HashMap();
		interfaceDrivers = new HashMap();
	}

	/**
	 * Called when the system shuts down.
	 * This method terminates all running interface driver threads.
	 */
	public void onShutdown ()
	{
		List tmpDriverThreads = new LinkedList(driverThreads.keySet ());

		for (Iterator i = tmpDriverThreads.iterator (); i.hasNext ();)
		{
			stopDriverInstance ((Long) i.next ());
		}
	}

	/**
	 * Called when a user logs off the system.
	 *
	 * @param user The logged of user.
	 */
	public void onUserLogoff (User user)
	{
	}

	/**
	 * Initialize the interfacing manager.
	 */
	public void init ()
	{
		Properties props = new Properties();

		props.put ("type", "InterfaceRegistry");
		props.put ("id", new Long(11004));
		CommandTools.performSimple ("persist.LoadObject", props);

		interfaceRegistry = (InterfaceRegistry) Engine.instance ().getBaseRegistry ().get (11004);
		SerialLink.searchForSerialDevices (interfaceRegistry);

		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (
			this);

		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);

		for (Iterator i = interfaceRegistry.getInterfaceIterator (); i.hasNext ();)
		{
			Interface iface = (Interface) i.next ();
			InterfaceDriver driver = InterfaceDriverDescriptor.createDriver (iface);

			interfaceDrivers.put (new Long(iface.getUniqueId ()), driver);
		}
	}

	/**
	 * Called when the manager is removed from the system.
	 */
	public void unload ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).removeObserver (
			this);
	}

	/**
	 * Retrieve the interface registry.
	 *
	 * @return The interface registry.
	 */
	public InterfaceRegistry getInterfaceRegistry ()
	{
		return interfaceRegistry;
	}

	/**
	 * Start an interface driver.
	 *
	 * @param system Gaging system to which the driver thread belongs.
	 */
	public void startDriverInstance (GagingSystem system)
	{
		long systemId = system.getUniqueId ();

		if (driverThreads.get (new Long(systemId)) == null)
		{
			GagingSystemDriverThread thread = new GagingSystemDriverThread(system);

			driverThreads.put (new Long(systemId), thread);
			thread.setDaemon (true);
			thread.start ();

			Log.logInfo (
				"server", "InterfacingManager",
				"Started driver instance '" + system.getName () + ":" + systemId + "'");
		}
	}

	/**
	 * Stop a running interface driver.
	 *
	 * @param systemId Id of the gaging system to which the driver thread belongs.
	 */
	public void stopDriverInstance (Long systemId)
	{
		GagingSystemDriverThread thread = (GagingSystemDriverThread) driverThreads.get (systemId);

		if (thread != null)
		{
			thread.terminate ();
			driverThreads.remove (systemId);

			Log.logInfo (
				"server", "InterfacingManager", "Stopped driver instance '" + systemId + "'");
		}
	}

	/**
	 * Called when an iritgo object was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		if (event.getModifiedObject () instanceof GagingSystem)
		{
			GagingSystem system = (GagingSystem) event.getModifiedObject ();

			if (system.getActive ())
			{
				startDriverInstance (system);
			}
			else
			{
				stopDriverInstance (new Long(system.getUniqueId ()));
			}
		}
		else if (event.getModifiedObject () instanceof Interface)
		{
			Interface iface = (Interface) event.getModifiedObject ();

			InterfaceDriver driver =
				(InterfaceDriver) interfaceDrivers.get (new Long(iface.getUniqueId ()));

			driver.init (iface);
		}
	}

	/**
	 * Start all gaging system that have a driver with a specific id.
	 *
	 * @param driverIds An array of driver ids.
	 */
	public void startGagingSystems (String[] driverIds)
	{
		for (Iterator i = interfaceRegistry.getGagingSystemIterator (); i.hasNext ();)
		{
			GagingSystem system = (GagingSystem) i.next ();

			if (system.getActive ())
			{
				for (int j = 0; j < driverIds.length; ++j)
				{
					if (system.getDriverId ().equals (driverIds[j]))
					{
						startDriverInstance (system);

						break;
					}
				}
			}
		}
	}

	/**
	 * Retrieve an interface driver instance.
	 *
	 * @param id The driver id.
	 * @return The interface driver.
	 */
	public InterfaceDriver getInterfaceDriver (long id)
	{
		return (InterfaceDriver) interfaceDrivers.get (new Long(id));
	}
}