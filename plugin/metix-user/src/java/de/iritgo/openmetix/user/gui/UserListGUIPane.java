/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.user.gui;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.app.key.UserCheckKeyLicense;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectTableModel;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.user.MetixUser;
import de.iritgo.openmetix.user.MetixUserRegistry;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;


/**
 * This gui pane displays a list of all users and lets the administrator
 * add, edit, and delete users.
 *
 * @version $Id: UserListGUIPane.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class UserListGUIPane
	extends SwingGUIPane
	implements TableModelListener
{
	/** The table of all users. */
	public JTable userTable;

	/** Model for the user table. */
	private IObjectTableModel userModel;

	/**
	 * Create a new UserListGUIPane.
	 */
	public UserListGUIPane ()
	{
		super("UserListGUIPane");
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

			swingEngine.setClassLoader (AppPlugin.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (getClass ().getResource ("/swixml/UserList.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			userModel =
				new IObjectTableModel()
					{
						private String[] columnNames =
							new String[]
							{
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix.name"),
								Engine.instance ().getResourceService ().getStringWithoutException (
									"metix-user.role")
							};

						public int getColumnCount ()
						{
							return columnNames.length;
						}

						public String getColumnName (int col)
						{
							return columnNames[col];
						}

						public int getRowCount ()
						{
							MetixUserRegistry list = (MetixUserRegistry) iobject;

							if (list == null)
							{
								return 0;
							}

							return list.getUserCount ();
						}

						public Object getValueAt (int row, int col)
						{
							MetixUserRegistry list = (MetixUserRegistry) iobject;
							MetixUser metixUser = (MetixUser) list.getUser (row);

							switch (col)
							{
								case 0:
									return metixUser.getName ();

								case 1:
									return metixUser.getRoleString ();

								default:
									return null;
							}
						}
					};

			userModel.addTableModelListener (this);
			userTable.setModel (userModel);

			userTable.addMouseListener (
				new MouseAdapter()
				{
					public void mouseClicked (MouseEvent e)
					{
						if (e.getClickCount () == 2)
						{
							editUserAction.actionPerformed (null);
						}
					}
				});

			getDisplay ().putProperty ("weighty", new Double(0.5));
		}
		catch (Exception x)
		{
			Log.logError ("client", "UserListGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Load the data object into the gui.
	 */
	public void loadFromObject ()
	{
		MetixUserRegistry list = (MetixUserRegistry) iobject;
		int y = 0;

		for (Iterator i = list.getUserIterator (); i.hasNext ();)
		{
			userModel.setValueAt (i.next (), y, 0);
			++y;
		}

		userTable.revalidate ();
	}

	/**
	 * Store the gui values to the data object.
	 */
	public void storeToObject ()
	{
	}

	/**
	 * Called when the table model has changed.
	 */
	public void tableChanged (TableModelEvent x)
	{
		userTable.revalidate ();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new MetixUserRegistry();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new UserListGUIPane();
	}

	/**
	 * Create a new user.
	 */
	public Action addUserAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				MetixUserRegistry list = (MetixUserRegistry) iobject;

				if (UserCheckKeyLicense.LICENSE < list.getUserCount ())
				{
					JOptionPane.showMessageDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-user.licenceerror"), "Metix-Licence", JOptionPane.OK_OPTION);
				}
				else
				{
					MetixUser user = new MetixUser();
					SessionContext sessionContext = new SessionContext("user");

					sessionContext.add ("userlist", list);
					list.addUser (user);
					CommandTools.performAsync (
						new ShowDialog("EditUserGUIPane", user, sessionContext));
				}
			}
		};

	/**
	 * Edit an existing user.
	 */
	public Action editUserAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				int selectedRow = userTable.getSelectedRow ();

				if (selectedRow < 0)
				{
					return;
				}

				CommandTools.performAsync (
					new ShowDialog(
						"EditUserGUIPane", ((MetixUserRegistry) iobject).getUser (selectedRow)));
			}
		};

	/**
	 * Delete an existing user.
	 */
	public Action deleteUserAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				MetixUserRegistry list = (MetixUserRegistry) iobject;
				int selectedRow = userTable.getSelectedRow ();

				if (selectedRow < 0)
				{
					return;
				}

				if (
					JOptionPane.showConfirmDialog (
						content,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-user.remove"), "Metix", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					list.removeUser (((MetixUserRegistry) iobject).getUser (selectedRow));
				}
			}
		};

	/**
	 * Close the display
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};
}