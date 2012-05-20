/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.manager;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensorManager;
import de.iritgo.openmetix.app.instrument.Instrument;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObjectListEvent;
import de.iritgo.openmetix.core.iobject.IObjectListListener;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.IObjectModifiedEvent;
import de.iritgo.openmetix.framework.base.IObjectModifiedListener;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import de.iritgo.openmetix.user.MetixUser;
import de.iritgo.openmetix.user.MetixUserRegistry;
import java.util.Iterator;
import java.util.Properties;


/**
 * Server manager functionality.
 *
 * @version $Id: MetixUserManager.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class MetixUserManager
	extends BaseObject
	implements Manager, IObjectListListener, IObjectModifiedListener
{
	/** Number of maximum concurrent logins. */
	public static int LICENCE_COUNT = 1;

	/** The user registry. */
	private MetixUserRegistry metixUserRegistry;

	/**
	 * Create a new MetixUserManager.
	 */
	public MetixUserManager ()
	{
		super("MetixUser");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		Properties props = new Properties();

		CommandTools.performSimple ("persist.LoadAllUsers", new Object[] {  });

		props.put ("type", "MetixUserRegistry");
		props.put ("id", new Long(11000));
		CommandTools.performSimple ("persist.LoadObject", props);

		metixUserRegistry = (MetixUserRegistry) Engine.instance ().getBaseRegistry ().get (11000);

		setupTheSensorListener ();

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
	 * Get the user registry.
	 *
	 * @return The user registry.
	 */
	public MetixUserRegistry getMetixUserRegistry ()
	{
		return metixUserRegistry;
	}

	/**
	 * Initialize the sensor listeners.
	 */
	public void setupTheSensorListener ()
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		ConfigurationSensorManager configurationSensorManager =
			(ConfigurationSensorManager) Engine.instance ().getManagerRegistry ().getManager (
				"ConfigurationSensorManager");

		for (Iterator i = metixUserRegistry.getUserIterator (); i.hasNext ();)
		{
			MetixUser metixUser = (MetixUser) i.next ();
			User iUser = userRegistry.getUser (metixUser.getName ());

			UserProfile userProfile = null;

			try
			{
				userProfile = (UserProfile) iUser.getNamedIritgoObject ("UserProfile", null);
			}
			catch (Exception x)
			{
			}

			for (Iterator j = userProfile.instrumentIterator (); j.hasNext ();)
			{
				Instrument instrument = (Instrument) j.next ();

				for (Iterator l = instrument.sensorConfigIterator (); l.hasNext ();)
				{
					ConfigurationSensor configurationSensor = (ConfigurationSensor) l.next ();

					configurationSensorManager.addListenerToSensor (configurationSensor, iUser);
				}
			}
		}
	}

	/**
	 * Called when an iobject was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		if (event.getModifiedObject () instanceof MetixUser)
		{
			syncWithIritgoUser ((MetixUser) event.getModifiedObject ());
		}
	}

	/**
	 * Called when an iobject list was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectListEvent (IObjectListEvent event)
	{
		if (event.getObject () instanceof MetixUser && event.getType () == IObjectListEvent.REMOVE)
		{
			MetixUser metixUser = (MetixUser) event.getObject ();

			UserRegistry userRegistry = Server.instance ().getUserRegistry ();

			if ((metixUser.getName () == null) || metixUser.getName ().equals (""))
			{
				metixUser.setName ("no_login_name_for_this_account");
			}

			User user = userRegistry.getUser (metixUser.getName ());

			if (user == null)
			{
				return;
			}

			Properties props = new Properties();

			props.put ("id", new Long(user.getUniqueId ()));
			CommandTools.performSimple ("persist.DeleteUser", props);

			userRegistry.removeUser (user);
		}
	}

	/**
	 * Synchronize a Metix application user with the Iritgo
	 * system user.
	 *
	 * @param metixUser The user to synchronize.
	 */
	public void syncWithIritgoUser (MetixUser metixUser)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		if ((metixUser.getName () == null) || metixUser.getName ().equals (""))
		{
			metixUser.setName ("no_login_name_for_this_account");
		}

		User user = userRegistry.getUser (metixUser.getName ());

		if (user == null)
		{
			user =
				new User(
					metixUser.getName (), metixUser.getEmail (),
					Engine.instance ().getPersistentIDGenerator ().createId (),
					metixUser.getPassword (), 0);

			userRegistry.addUser (user);
			Engine.instance ().getBaseRegistry ().add (user);

			Properties props = new Properties();

			props.put ("user", user);
			CommandTools.performSimple ("AddMetixUserRegistryToUser", props);
			CommandTools.performSimple ("AddInterfaceRegistryToUser", props);
			CommandTools.performSimple ("AddGagingStationRegistryToUser", props);
			CommandTools.performSimple ("AddUserProfileToUser", props);

			props.put ("metixUser", metixUser);
			CommandTools.performSimple ("AddMetixUserToUser", props);

			props = new Properties();
			props.put ("id", new Long(user.getUniqueId ()));
			CommandTools.performSimple ("persist.StoreUser", props);
		}
		else
		{
			user.setName (metixUser.getName ());
			user.setPassword (metixUser.getPassword ());
			user.setEmail (metixUser.getEmail ());
		}
	}
}