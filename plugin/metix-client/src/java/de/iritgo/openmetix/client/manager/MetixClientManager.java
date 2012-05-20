/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.manager;


import de.iritgo.openmetix.app.userprofile.ActiveDisplay;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.client.gui.MetixClientGUI;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.gui.IDisplayClosedEvent;
import de.iritgo.openmetix.core.gui.IDisplayClosedListener;
import de.iritgo.openmetix.core.gui.IDisplayOpenedEvent;
import de.iritgo.openmetix.core.gui.IDisplayOpenedListener;
import de.iritgo.openmetix.core.gui.IWindow;
import de.iritgo.openmetix.core.gui.swing.SwingDesktopManager;
import de.iritgo.openmetix.core.gui.swing.SwingWindowFrame;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.core.iobject.IObjectProxyListener;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.client.gui.ClientGUI;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.manager.ClientManager;
import de.iritgo.openmetix.framework.user.UserEvent;
import de.iritgo.openmetix.framework.user.UserListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * The client managers main responsibility is to save and restore the desktop pages
 * and the measurement instruments.
 *
 * @version $Id: MetixClientManager.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class MetixClientManager
	extends BaseObject
	implements Manager, ClientManager, UserListener, IDisplayOpenedListener, IDisplayClosedListener,
		IObjectProxyListener
{
	/** The client gui. */
	private ClientGUI clientGUI;

	/** The user profile. */
	private UserProfile userProfile;

	/** Number of already restored displays. */
	private int numRestoredDisplays;

	/** All displays to restore. */
	private List activeDisplays;

	/** Desktop id to index map. */
	private Map desktopIndexById;

	/** Number of active displays already received by the client. */
	private int activeDisplaysReceived;

	/**
	 * Create a new MetixClientManager.
	 *
	 */
	public MetixClientManager ()
	{
		super("client");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		Engine.instance ().getEventRegistry ().addListener ("UserLogin", this);
		Engine.instance ().getEventRegistry ().addListener ("iwindowframe.opened", this);
		Engine.instance ().getEventRegistry ().addListener ("iwindowframe.closed", this);
		activeDisplays = new LinkedList();
		desktopIndexById = new HashMap();
		activeDisplaysReceived = 0;
		initNetworkSystem ();
	}

	private void initNetworkSystem ()
	{
		Client.instance ().createDefaultNetworkProcessingSystem ();
	}

	/**
	 * Unload the manager from the system.
	 */
	public void unload ()
	{
	}

	/**
	 * This method is called when user has logged in.
	 *
	 * @param event The user event.
	 */
	public void userEvent (UserEvent event)
	{
		try
		{
			if ((event != null) && (event.isLoggedIn ()))
			{
				activeDisplaysReceived = 0;
				activeDisplays.clear ();
				desktopIndexById.clear ();

				userProfile =
					new UserProfile(event.getUser ().getNamedIritgoObjectId ("UserProfile"));
				Engine.instance ().getProxyEventRegistry ().addEventListener (userProfile, this);
				Engine.instance ().getBaseRegistry ().add ((BaseObject) userProfile);

				FrameworkProxy userProfileProxy = new FrameworkProxy(userProfile);

				Engine.instance ().getProxyRegistry ().addProxy (userProfileProxy);
				AppContext.instance ().setApplicationObject (userProfile);
				userProfileProxy.reset ();
			}
		}
		catch (NoSuchIObjectException x)
		{
			Log.logError ("client", "MetixClientManager", "Unable to retrieve UserProfile: " + x);
		}
	}

	/**
	 * This method is called after a change to a data object.
	 *
	 * @param event The proxy event.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject ())
		{
			return;
		}

		final MetixClientGUI clientGUI = (MetixClientGUI) Client.instance ().getClientGUI ();

		if (event.getObject () instanceof UserProfile)
		{
			clientGUI.setStatusProgressRange (0, userProfile.getActiveDisplayCount () + 1);

			String desktopList = ((UserProfile) event.getObject ()).getDesktopList ();

			if ("empty".equals (desktopList))
			{
				clientGUI.createDesktop (
					Engine.instance ().getResourceService ().getStringWithoutException (
						"metix.standardDesktop"));
			}
			else
			{
				StringTokenizer st = new StringTokenizer(desktopList, "\n");
				int desktopIndex = 0;

				while (st.hasMoreTokens ())
				{
					String desktopId = st.nextToken ();

					if (st.hasMoreTokens ())
					{
						String desktopName = st.nextToken ();

						clientGUI.createDesktop (desktopId, desktopName);
						desktopIndexById.put (desktopId, new Integer(desktopIndex++));
					}
				}
			}

			Engine.instance ().getProxyEventRegistry ().removeEventListener (userProfile, this);

			if (! "unknown".equals (userProfile.getActiveDesktopId ()))
			{
				clientGUI.activateDesktop (userProfile.getActiveDesktopId ());
			}

			for (Iterator i = userProfile.activeDisplayIterator (); i.hasNext ();)
			{
				ActiveDisplay activeDisplay = (ActiveDisplay) i.next ();

				Engine.instance ().getProxyEventRegistry ().addEventListener (activeDisplay, this);
			}
		}

		if (event.getObject () instanceof ActiveDisplay)
		{
			ActiveDisplay activeDisplay = (ActiveDisplay) event.getObject ();

			++activeDisplaysReceived;

			if (! activeDisplay.isDeleted ())
			{
				activeDisplays.add (activeDisplay);
			}

			if (activeDisplaysReceived == userProfile.getActiveDisplayCount ())
			{
				clientGUI.setStatusProgressStep ();

				Engine.instance ().getProxyEventRegistry ().removeEventListener (this);

				Collections.sort (
					activeDisplays,
					new Comparator()
					{
						public int compare (Object o1, Object o2)
						{
							ActiveDisplay ad1 = (ActiveDisplay) o1;
							ActiveDisplay ad2 = (ActiveDisplay) o2;

							int comp =
								((Integer) desktopIndexById.get (ad1.getDesktopId ())).intValue () -
								((Integer) desktopIndexById.get (ad2.getDesktopId ())).intValue ();

							if (comp == 0)
							{
								comp = ad1.getBounds ().y - ad2.getBounds ().y;

								if (comp == 0)
								{
									comp = ad1.getBounds ().x - ad2.getBounds ().x;
								}
							}

							return comp;
						}

						public boolean equals (Object o1, Object o2)
						{
							return o1 == o2;
						}
					});

				for (Iterator i = activeDisplays.iterator (); i.hasNext ();)
				{
					activeDisplay = (ActiveDisplay) i.next ();

					Properties properties = new Properties();

					properties.put ("metixReload", new Boolean(true));
					properties.put ("bounds", activeDisplay.getBounds ());
					properties.put ("maximized", new Boolean(activeDisplay.isMaximized ()));

					final ShowWindow showWindow =
						new ShowWindow(
							activeDisplay.getDisplayGUIPaneId (),
							(IObject) Engine.instance ().getBaseRegistry ().get (
								activeDisplay.getDisplayUniqueId ()), null,
							activeDisplay.getDesktopId ());

					showWindow.setProperties (properties);

					CommandTools.performAsync (showWindow);
				}
			}
		}
	}

	/**
	 * Transfer the current desktop layout, i.e the desktop pages and the
	 * instruments to the server.
	 */
	public void saveAllActiveDisplaysOnServer ()
	{
		Engine.instance ().getProxyEventRegistry ().removeEventListener (this);

		CommandTools.performAsync (
			new Command("saveAllActiveDisplaysOnServer")
			{
				public void perform ()
				{
					MetixClientGUI clientGUI = (MetixClientGUI) Client.instance ().getClientGUI ();
					SwingDesktopManager desktopManager =
						(SwingDesktopManager) clientGUI.getDesktopManager ();

					StringBuffer desktopList = new StringBuffer();

					for (Iterator i = clientGUI.desktopIdIterator (); i.hasNext ();)
					{
						String id = (String) i.next ();

						desktopList.append (id);
						desktopList.append ("\n");
						desktopList.append (desktopManager.getTitle (id));
						desktopList.append ("\n");
					}

					UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();

					userProfile.setDesktopList (desktopList.toString ());
					userProfile.setActiveDesktopId (desktopManager.getActiveDesktopPaneId ());
					userProfile.update ();

					for (Iterator i = userProfile.activeDisplayIterator (); i.hasNext ();)
					{
						ActiveDisplay activeDisplay = (ActiveDisplay) i.next ();

						activeDisplay.setDeletedFlag ();
						activeDisplay.update ();
					}

					userProfile.removeAllActiveDisplays ();

					for (Iterator i = desktopManager.getDisplayIterator (); i.hasNext ();)
					{
						IDisplay display = (IDisplay) i.next ();
						ActiveDisplay activeDisplay = new ActiveDisplay();

						activeDisplay.setDisplayGUIPaneId (display.getGUIPane ().getTypeId ());

						try
						{
							SwingWindowFrame windowFrame =
								(SwingWindowFrame) ((IWindow) display).getWindowFrame ();

							activeDisplay.setBounds (windowFrame.getNormalBounds ());
							activeDisplay.setMaximized (windowFrame.isMaximum ());
						}
						catch (ClassCastException x)
						{
						}

						if (display.getGUIPane ().getObject () == null)
						{
							continue;
						}

						activeDisplay.setDisplayTypeId (
							display.getGUIPane ().getObject ().getTypeId ());
						activeDisplay.setDisplayUniqueId (
							display.getGUIPane ().getObject ().getUniqueId ());
						activeDisplay.setDesktopId (display.getDesktopId ());

						userProfile.addActiveDisplay (activeDisplay);
					}
				}
			});
	}

	/**
	 * Return the client gui.
	 *
	 * @return The client gui.
	 */
	public ClientGUI getClientGUI ()
	{
		if (clientGUI == null)
		{
			clientGUI = new MetixClientGUI();
		}

		return clientGUI;
	}

	/**
	 * Called when a new display was opened on the desktop.
	 *
	 * @param event The display event.
	 */
	public void displayOpenedEvent (IDisplayOpenedEvent event)
	{
	}

	/**
	 * Called when a display was closed.
	 *
	 * @param event The display event.
	 */
	public void displayClosedEvent (IDisplayClosedEvent event)
	{
	}
}