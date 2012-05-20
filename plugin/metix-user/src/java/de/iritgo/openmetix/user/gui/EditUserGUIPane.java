/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IRadioButton;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.user.MetixUser;
import de.iritgo.openmetix.user.MetixUserRegistry;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


/**
 * This gui pane is used to edit users.
 *
 * @version $Id: EditUserGUIPane.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class EditUserGUIPane
	extends SwingGUIPane
{
	/** The user name. */
	public ITextField username;

	/** The user password. */
	public JPasswordField password;

	/** The repeated user password. */
	public JPasswordField passwordRepeat;

	/** The user's email. */
	public ITextField email;

	/** Check for a normal user role. */
	public IRadioButton normalUser;

	/** Check for a read only user role. */
	public IRadioButton readonlyUser;

	/** Check for an administration user role. */
	public IRadioButton adminUser;

	/**
	 * Create a new EditUserGUIPane.
	 */
	public EditUserGUIPane ()
	{
		super("EditUserGUIPane");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/AdminEditUser.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "EditUserGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		MetixUser user = (MetixUser) iobject;

		username.setText (user.getName ());
		password.setText (user.getPassword ());
		passwordRepeat.setText (user.getPassword ());
		email.setText (user.getEmail ());

		if (user.getRole () == MetixUser.ROLE_NORMAL)
		{
			normalUser.setSelected (true);
		}

		if (user.getRole () == MetixUser.ROLE_READONLY)
		{
			readonlyUser.setSelected (true);
		}

		if (user.getRole () == MetixUser.ROLE_ADMIN)
		{
			adminUser.setSelected (true);
		}

		username.selectAll ();
	}

	/**
	 * StoreFromObject, load the Data from Object.
	 */
	public void storeToObject ()
	{
		MetixUser user = (MetixUser) iobject;

		user.setName (username.getText ());
		user.setPassword (new String(password.getPassword ()));
		user.setEmail (email.getText ());

		if (normalUser.isSelected ())
		{
			user.setRole (MetixUser.ROLE_NORMAL);
		}

		if (readonlyUser.isSelected ())
		{
			user.setRole (MetixUser.ROLE_READONLY);
		}

		if (adminUser.isSelected ())
		{
			user.setRole (MetixUser.ROLE_ADMIN);
		}

		user.update ();
	}

	/**
	 * Save the data object and close the display.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (
					new String(password.getPassword ()).equals (
						new String(passwordRepeat.getPassword ())))
				{
					if (username.getText ().equals (""))
					{
						JOptionPane.showMessageDialog (
							content,
							Engine.instance ().getResourceService ().getStringWithoutException (
								"metix-user.noname"), "Metix", JOptionPane.OK_OPTION);

						return;
					}

					storeToObject ();
					display.close ();
				}
				else
				{
					JOptionPane.showMessageDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-user.passwordrepeatfailure"), "Metix", JOptionPane.OK_OPTION);
				}
			}
		};

	/**
	 * Close the display.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (sessionContext != null)
				{
					MetixUserRegistry list = (MetixUserRegistry) sessionContext.get ("userlist");

					list.removeUser ((MetixUser) iobject);
				}

				display.close ();
			}
		};

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new MetixUser();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new EditUserGUIPane();
	}
}