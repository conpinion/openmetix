/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.framework.user.action.UserLoginFailureAction;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;


/**
 * This gui pane is displayed after an unsuccessful attempt to log into the
 * server.
 *
 * @version $Id: UserLoginFailureGUIPane.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class UserLoginFailureGUIPane
	extends SwingGUIPane
{
	/** The message panel. */
	public Box messagePanel;

	/**
	 * Create a new UserLoginFailureGUIPane.
	 */
	public UserLoginFailureGUIPane ()
	{
		super("UserLoginFailureGUIPane");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (ClientPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/UserLoginFailure.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			int failure = ((Integer) getDisplay ().getProperties ().get ("failure")).intValue ();

			String msg;

			if (failure == UserLoginFailureAction.USER_ALREADY_ONLINE)
			{
				msg = Engine.instance ().getResourceService ().getStringWithoutException (
						"metix.userLoginFailureUserAlreadyOnline");
			}
			else
			{
				msg = Engine.instance ().getResourceService ().getStringWithoutException (
						"metix.userLoginFailureBadNameOrPassword");
			}

			StringTokenizer st = new StringTokenizer(msg, "\n");

			while (st.hasMoreTokens ())
			{
				messagePanel.add (new JLabel(st.nextToken ()));
			}
		}
		catch (Exception x)
		{
			Log.logError ("client", "UserLoginFailureGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Close the dialog.
	 *
	 * @param event The action event.
	 */
	/**
	 * Log into the server.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
				CommandTools.performAsync (new ShowDialog("UserLoginGUIPane"));
			}
		};

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		UserLoginFailureGUIPane userLoginFailureGUIPane = new UserLoginFailureGUIPane();

		return userLoginFailureGUIPane;
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return null;
	}
}