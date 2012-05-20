/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.GUIPaneRegistry;
import de.iritgo.openmetix.core.gui.IWindow;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.client.Client;


/**
 * ShowWindow.
 *
 * @version $Id: ShowWindow.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ShowWindow
	extends Command
{
	/** The id of the gui pane to show. */
	private String guiPaneId;

	/** The desktop name. */
	private String desktopName;

	/** The object to display. */
	private IObject prototypeable;

	/** The current session context. */
	private SessionContext sessionContext;

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (String guiPaneId)
	{
		init (guiPaneId, null, null, null);
	}

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (String guiPaneId, IObject prototypeable)
	{
		init (guiPaneId, null, prototypeable, null);
	}

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (String guiPaneId, long unqiueId, String desktopId)
	{
		GUIPane guiPane = GUIPaneRegistry.instance ().create (guiPaneId);

		IObject prototypeable = (IObject) Engine.instance ().getBaseRegistry ().get (unqiueId);

		if (prototypeable == null)
		{
			prototypeable = guiPane.getSampleObject ();
			prototypeable.setUniqueId (unqiueId);
			Engine.instance ().getBaseRegistry ().add ((BaseObject) prototypeable);

			FrameworkProxy proxy = new FrameworkProxy(prototypeable);

			proxy.setSampleRealObject ((IObject) prototypeable);
			Engine.instance ().getProxyRegistry ().addProxy (proxy);
		}

		init (guiPaneId, desktopId, prototypeable, null);
	}

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (String guiPaneId, long unqiueId)
	{
		this(guiPaneId, unqiueId, null);
	}

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (String guiPaneId, SessionContext sessionContext)
	{
		init (guiPaneId, null, null, sessionContext);
	}

	/**
	 * Create a new ShowWindow command.
	 */
	public ShowWindow (
		String guiPaneId, IObject prototypeable, SessionContext sessionContext, String desktopName)
	{
		init (guiPaneId, desktopName, prototypeable, sessionContext);
	}

	/**
	 * Display the window.
	 */
	public void perform ()
	{
		final IWindow window = new IWindow(guiPaneId);

		window.setProperties (properties);
		window.setDesktopManager (Client.instance ().getClientGUI ().getDesktopManager ());
		window.setDesktopId (desktopName);

		if (prototypeable == null)
		{
			window.initGUI (guiPaneId, sessionContext);
		}
		else
		{
			window.initGUI (guiPaneId, prototypeable, sessionContext);
		}

		Client.instance ().getClientGUI ().getDesktopManager ().addDisplay (window);

		window.show ();
	}

	/**
	 * @see de.iritgo.openmetix.core.command.Command#canPerform()
	 */
	public boolean canPerform ()
	{
		return true;
	}

	/**
	 * Command initialization.
	 *
	 * @param guiPaneId
	 * @param desktopName
	 * @param prototypeable
	 * @param sessionContext
	 */
	private void init (
		String guiPaneId, String desktopName, IObject prototypeable, SessionContext sessionContext)
	{
		this.guiPaneId = guiPaneId;
		this.desktopName = desktopName;
		this.prototypeable = prototypeable;
		this.sessionContext = sessionContext;
	}
}