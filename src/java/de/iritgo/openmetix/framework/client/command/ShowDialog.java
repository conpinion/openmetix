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
import de.iritgo.openmetix.core.gui.IDialog;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.client.Client;


/**
 * ShowDialog.
 *
 * @version $Id: ShowDialog.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ShowDialog
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
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 */
	public ShowDialog (String guiPaneId)
	{
		init (guiPaneId, null, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param prototypeable A data object prototype.
	 */
	public ShowDialog (String guiPaneId, IObject prototypeable)
	{
		init (guiPaneId, prototypeable, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param unqiueId The id of the data object
	 */
	public ShowDialog (String guiPaneId, long unqiueId)
	{
		GUIPane guiPane = GUIPaneRegistry.instance ().create (guiPaneId);

		IObject prototypeable = (IObject) Engine.instance ().getBaseRegistry ().get (unqiueId);

		if (prototypeable == null)
		{
			prototypeable = guiPane.getSampleObject ();
			prototypeable.setUniqueId (unqiueId);
			Engine.instance ().getBaseRegistry ().add ((BaseObject) prototypeable);

			FrameworkProxy appProxy = new FrameworkProxy(prototypeable);

			appProxy.setSampleRealObject ((IObject) prototypeable);
			Engine.instance ().getProxyRegistry ().addProxy (appProxy);
		}

		init (guiPaneId, prototypeable, null);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param sessionContext The session context.
	 */
	public ShowDialog (String guiPaneId, SessionContext sessionContext)
	{
		init (guiPaneId, null, sessionContext);
	}

	/**
	 * Create a new ShowDialog command.
	 *
	 * @param guiPaneId The GUIPane to show.
	 * @param prototypeable A data object prototype.
	 * @param sessionContext The session context.
	 */
	public ShowDialog (String guiPaneId, IObject prototypeable, SessionContext sessionContext)
	{
		init (guiPaneId, prototypeable, sessionContext);
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform ()
	{
		final IDialog dialog = new IDialog(guiPaneId);

		dialog.setProperties (properties);

		if (prototypeable == null)
		{
			dialog.initGUI (guiPaneId, sessionContext);
		}
		else
		{
			dialog.initGUI (guiPaneId, prototypeable, sessionContext);
		}

		Client.instance ().getClientGUI ().getDesktopManager ().addDisplay (dialog);

		dialog.show ();
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
	 * @param prototypeable
	 * @param sessionContext
	 */
	private void init (String guiPaneId, IObject prototypeable, SessionContext sessionContext)
	{
		this.guiPaneId = guiPaneId;
		this.prototypeable = prototypeable;
		this.sessionContext = sessionContext;
	}
}