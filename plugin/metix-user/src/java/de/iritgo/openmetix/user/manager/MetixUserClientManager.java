/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.manager;


import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.core.iobject.IObjectProxyListener;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.user.UserEvent;
import de.iritgo.openmetix.framework.user.UserListener;
import de.iritgo.openmetix.user.MetixUser;
import java.util.Properties;


/**
 * Client manager functionality.
 *
 * @version $Id: MetixUserClientManager.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class MetixUserClientManager
	extends BaseObject
	implements Manager, UserListener
{
	/** The Metix user. */
	private MetixUser metixUser;

	/** The user preferences. */
	private Preferences preferences;

	/**
	 * Create a new MetixUserClientManager.
	 */
	public MetixUserClientManager ()
	{
		super("MetixUserClientManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		Engine.instance ().getEventRegistry ().addListener ("UserLogin", this);
	}

	/**
	 * Unload the manager from the system.
	 */
	public void unload ()
	{
	}

	/**
	 * This will called if the user is loggedin
	 *
	 * This method triggers the loading of the current metix user.
	 *
	 * @param event The userEvent.
	 */
	public void userEvent (UserEvent event)
	{
		try
		{
			if ((event != null) && (event.isLoggedIn ()))
			{
				MetixUser metixUser =
					new MetixUser(event.getUser ().getNamedIritgoObjectId ("MetixUser"));

				Engine.instance ().getProxyEventRegistry ().addEventListener (
					metixUser,
					new IObjectProxyListener()
					{
						public void proxyEvent (IObjectProxyEvent event)
						{
							if (! event.isWaitingForNewObject ())
							{
								MetixUser metixUser = (MetixUser) event.getObject ();
								Properties props = new Properties();

								props.put (
									"enabled",
									new Boolean(metixUser.getRole () == MetixUser.ROLE_ADMIN));
								CommandTools.performAsync ("EnableAdminFunctions", props);
							}
						}
					});
				Engine.instance ().getBaseRegistry ().add ((BaseObject) metixUser);

				FrameworkProxy metixUserProxy = new FrameworkProxy(metixUser);

				Engine.instance ().getProxyRegistry ().addProxy (metixUserProxy);
				metixUserProxy.reset ();

				Preferences preferences =
					new Preferences(event.getUser ().getNamedIritgoObjectId ("Preferences"));

				Engine.instance ().getProxyEventRegistry ().addEventListener (
					preferences,
					new IObjectProxyListener()
					{
						public void proxyEvent (IObjectProxyEvent event)
						{
							if (! event.isWaitingForNewObject ())
							{
								Preferences preferences = (Preferences) event.getObject ();
								Properties props = new Properties();

								props.put ("preferences", preferences);
								CommandTools.performAsync ("PopulatePreferences", props);
							}
						}
					});
				Engine.instance ().getBaseRegistry ().add ((BaseObject) preferences);

				FrameworkProxy preferencesProxy = new FrameworkProxy(preferences);

				Engine.instance ().getProxyRegistry ().addProxy (preferencesProxy);
				preferencesProxy.reset ();
			}
		}
		catch (NoSuchIObjectException x)
		{
			Log.logError (
				"client", "MetixUserClientManager", "Unable to install user settings: " + x);
		}
	}
}