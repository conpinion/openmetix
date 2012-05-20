/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.util.Tools;
import de.iritgo.openmetix.client.ClientPlugin;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.SystemProperties;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.flowcontrol.FlowControl;
import de.iritgo.openmetix.core.flowcontrol.FlowRule;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IButton;
import de.iritgo.openmetix.core.gui.swing.ICheckBox;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.CloseDisplay;
import de.iritgo.openmetix.framework.client.command.ConnectToServer;
import de.iritgo.openmetix.framework.client.command.ConnectionFailure;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.client.command.UserLogin;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;


/**
 * This gui pane lets the user enter his account data to log into the server.
 *
 * @version $Id: UserLoginGUIPane.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class UserLoginGUIPane
	extends SwingGUIPane
{
	/** The name of the server to connect with. */
	public ITextField server;

	/** The user's system name. */
	public JTextField username;

	/** The user's password. */
	public JPasswordField password;

	/** Short cut for the developers. */
	public JComboBox autoLogin;

	/** Check to store the login data. */
	public ICheckBox remember;
	public IButton login;

	/**
	 * The auto login combo box is provided to the developers for
	 * easier server login. Be sure to remove this combobox from the
	 * gui in a release version!
	 */
	private class AutoLoginItem
	{
		/** The user name to connect with. */
		public String userName;

		/** The password to connect with. */
		public String password;

		/** The server to connect with. */
		public String server;

		/**
		 * Create a new AutoLoginItem.
		 *
		 * @param userName The user name to connect with.
		 * @param password The password to connect with.
		 * @param server The server to connect with.
		 */
		public AutoLoginItem (String userName, String password, String server)
		{
			this.userName = userName;
			this.password = password;
			this.server = server;
		}

		/**
		 * Create a string representation of the auto login item.
		 *
		 * @return A string representation of the item.
		 */
		public String toString ()
		{
			return userName + "@" + server;
		}
	}

	/**
	 * Create a new UserLoginGUIPane.
	 */
	public UserLoginGUIPane ()
	{
		super("UserLoginGUIPane");
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
				(JPanel) swingEngine.render (getClass ().getResource ("/swixml/UserLogin.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			server.setText ("localhost");

			SystemProperties properties = Engine.instance ().getSystemProperties ();

			String storedAccounts = properties.getString ("account.list", "");

			for (StringTokenizer st = new StringTokenizer(storedAccounts, ",");
				st.hasMoreTokens ();)
			{
				String accountNum = st.nextToken ();

				autoLogin.addItem (
					new AutoLoginItem(
						properties.getString ("account." + accountNum + ".name", ""),
						Tools.decode (
							properties.getString ("account." + accountNum + ".password", "")),
						properties.getString ("account." + accountNum + ".server", "")));
			}

			autoLogin.setSelectedIndex (-1);

			autoLogin.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange () == ItemEvent.SELECTED)
						{
							remember.setSelected (true);

							AutoLoginItem item = (AutoLoginItem) autoLogin.getSelectedItem ();

							username.setText (item.userName);
							password.setText (item.password);
							server.setText (item.server);
						}
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "UserLoginGUIPane.initGUI", x.toString ());
		}

		username.grabFocus ();
	}

	/**
	 * Log into the server.
	 */
	private void login ()
	{
		final MetixClientGUI gui = (MetixClientGUI) Client.instance ().getClientGUI ();

		display.close ();

		SystemProperties properties = Engine.instance ().getSystemProperties ();

		CommandTools.performAsync (new ShowDialog("ConnectToServer"));
		AppContext.instance ().setServerIP (server.getText ());

		ConnectToServer connectToServer = new ConnectToServer("ConnectToServer");

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (connectToServer);

		ConnectionFailure connectionFailure = new ConnectionFailure();

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (connectionFailure);

		CommandTools.performAsync (new CloseDisplay("ConnectToServer"));
		CommandTools.performAsync (new ShowDialog("CheckUser"));

		final FlowControl flowControl = Engine.instance ().getFlowControl ();

		UserLogin userLogin =
			new UserLogin(username.getText (), new String(password.getPassword ()));

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (userLogin);
		CommandTools.performAsync (new CloseDisplay("CheckUser"));

		flowControl.add (
			new FlowRule("UserLogin")
			{
				public void success ()
				{
					flowControl.clear ();
					gui.setStatusUser (username.getText () + "@" + server.getText ());
					gui.getDesktopManager ().closeAllDisplays ();

					if (remember.isSelected ())
					{
						rememberAccount ();
					}
					else
					{
						removeAccount ();
					}

					// 					CommandTools.performAsync (new ShowDialog("KeyCheckResultDisplay"));
					// 					KeyCheckCommand kcc = new KeyCheckCommand();
					// 					kcc.setPerformName ("request");
					// 					kcc.setCheckStart (true);
					// 					CommandTools.performAsync (kcc);
				}

				public void failure (Object arg)
				{
					flowControl.clear ();
					gui.getDesktopManager ().closeAllDisplays ();
					Client.instance ().getNetworkService ().closeAllChannel ();

					Properties props = new Properties();

					props.put ("failure", arg);

					Command cmd = new ShowDialog("UserLoginFailureGUIPane");

					cmd.setProperties (props);
					CommandTools.performAsync (cmd);
				}
			});

		flowControl.add (
			new FlowRule("WrongVersion")
			{
				public void success ()
				{
					flowControl.clear ();

					MetixClientGUI gui = (MetixClientGUI) Client.instance ().getClientGUI ();

					gui.getDesktopManager ().closeAllDisplays ();

					JFrame frame =
						(JFrame) Client.instance ().getClientGUI ().getDesktopManager ()
									   .getDesktopFrame ();

					JOptionPane.showMessageDialog (
						frame,
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.wrongVersion"), "Metix", JOptionPane.OK_OPTION);

					Client.instance ().getNetworkService ().closeAllChannel ();
					CommandTools.performAsync (new ShowDialog("UserLoginGUIPane"));
				}

				public void failure ()
				{
				}
			});
	}

	/**
	 * Remember the current account information.
	 */
	private void rememberAccount ()
	{
		SystemProperties properties = Engine.instance ().getSystemProperties ();
		int nextAccountNum = properties.getInt ("account.next", 0);
		String storedAccounts = properties.getString ("account.list", "");

		for (StringTokenizer st = new StringTokenizer(storedAccounts, ","); st.hasMoreTokens ();)
		{
			String accountNum = st.nextToken ();

			if (
				properties.getString ("account." + accountNum + ".name", "").equals (
					username.getText ()) &&
				properties.getString ("account." + accountNum + ".server", "").equals (
					server.getText ()))
			{
				return;
			}
		}

		properties.put ("account." + nextAccountNum + ".name", username.getText ());
		properties.put (
			"account." + nextAccountNum + ".password",
			Tools.encode (new String(password.getPassword ())));
		properties.put ("account." + nextAccountNum + ".server", server.getText ());
		properties.put (
			"account.list",
			storedAccounts + (storedAccounts.length () > 0 ? "," : "") + nextAccountNum);
		properties.put ("account.next", nextAccountNum + 1);
		Engine.instance ().storeSystemProperties ();
	}

	/**
	 * Check if the current account information was stored in the
	 * properties. In this case remove it from the properties.
	 */
	private void removeAccount ()
	{
		SystemProperties properties = Engine.instance ().getSystemProperties ();
		String storedAccounts = properties.getString ("account.list", "");

		for (StringTokenizer st = new StringTokenizer(storedAccounts, ","); st.hasMoreTokens ();)
		{
			String accountNum = st.nextToken ();

			if (
				properties.getString ("account." + accountNum + ".name", "").equals (
					username.getText ()) &&
				properties.getString ("account." + accountNum + ".server", "").equals (
					server.getText ()))
			{
				properties.remove ("account." + accountNum + ".name");
				properties.remove ("account." + accountNum + ".password");
				properties.remove ("account." + accountNum + ".server");

				try
				{
					storedAccounts =
						storedAccounts.replaceAll ("(^|,)" + accountNum + "(,|$)", "$1$2");
					storedAccounts = storedAccounts.replaceAll ("(^,|,$)", "");
					storedAccounts = storedAccounts.replaceAll ("(,,)", ",");
				}
				catch (PatternSyntaxException ignored)
				{
				}

				properties.put ("account.list", storedAccounts);

				Engine.instance ().storeSystemProperties ();

				return;
			}
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		UserLoginGUIPane userLoginGUIPane = new UserLoginGUIPane();

		return userLoginGUIPane;
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

	/**
	 * Log into the server.
	 */
	public Action loginAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				login ();
			}
		};

	/**
	 * Cancel the login process.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				IritgoEngine.instance ().shutdown ();
			}
		};
}