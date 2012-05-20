/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.shutdown;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.AsyncCommandProcessor;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.action.UserLogoffServerAction;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ShutdownManager.
 *
 * @version $Id: ShutdownManager.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ShutdownManager
	extends BaseObject
	implements Manager
{
	private List observers;
	private static int numOfChecks = 10;
	private boolean userLogoffActionPerformed;

	public ShutdownManager ()
	{
		super("shutdown");
		observers = new LinkedList();
		userLogoffActionPerformed = false;
	}

	public void addObserver (ShutdownObserver shutdownObserver)
	{
		observers.add (shutdownObserver);
	}

	public void removeObserver (ShutdownObserver shutdownObserver)
	{
		observers.remove (shutdownObserver);
	}

	public void shutdown ()
	{
		LinkedList tmpObservers = new LinkedList(observers);

		for (Iterator i = tmpObservers.iterator (); i.hasNext ();)
		{
			((ShutdownObserver) i.next ()).onShutdown ();
		}

		if (AppContext.instance ().isUserLoggedIn ())
		{
			ActionTools.sendToServer (new UserLogoffServerAction());
			waitForUserLogoffAction ();
		}

		for (int i = numOfChecks; i >= 0; --i)
		{
			waitForCommands ();
			waitForActions ();
		}
	}

	public void shutdown (User user)
	{
		LinkedList tmpObservers = new LinkedList(observers);

		for (Iterator i = tmpObservers.iterator (); i.hasNext ();)
		{
			((ShutdownObserver) i.next ()).onUserLogoff (user);
		}

		for (int i = numOfChecks; i >= 0; --i)
		{
			waitForCommands ();
			waitForActions ();
		}
	}

	public void userLogoffActionPerformend ()
	{
		userLogoffActionPerformed = true;
	}

	private void waitForUserLogoffAction ()
	{
		int i = 0;

		while (! userLogoffActionPerformed && i <= 5)
		{
			try
			{
				++i;
				Thread.sleep (1000);
			}
			catch (Exception x)
			{
			}
		}
	}

	private void waitForCommands ()
	{
		AsyncCommandProcessor async = IritgoEngine.instance ().getAsyncCommandProcessor ();

		while (async.commandsInProcess ())
		{
			try
			{
				Thread.sleep (1000);
			}
			catch (Exception x)
			{
			}
		}
	}

	private void waitForActions ()
	{
		for (Iterator i = observers.iterator (); i.hasNext ();)
		{
			ShutdownObserver shutdownObserver = (ShutdownObserver) i.next ();

			while (
				Engine.instance ().getFlowControl ().ruleExists (
					"shutdown.in.progress." + shutdownObserver.getTypeId ()))
			{
				try
				{
					Thread.sleep (500);
				}
				catch (Exception x)
				{
				}
			}
		}
	}

	public void init ()
	{
	}

	public void unload ()
	{
	}
}