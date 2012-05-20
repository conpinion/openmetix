/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.app.gui.IClockTextField;
import de.iritgo.openmetix.app.gui.IDataReductionSensorSelector;
import de.iritgo.openmetix.app.gui.ISensorSelector;
import de.iritgo.openmetix.app.gui.IStationSensorSelector;
import de.iritgo.openmetix.app.gui.ITimeComboBox;
import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.client.manager.MetixClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDialog;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.gui.swing.IMenuBar;
import de.iritgo.openmetix.core.gui.swing.IToolBar;
import de.iritgo.openmetix.core.gui.swing.SwingDesktopFrame;
import de.iritgo.openmetix.core.gui.swing.SwingDesktopManager;
import de.iritgo.openmetix.core.gui.swing.SwingDialogFrame;
import de.iritgo.openmetix.core.gui.swing.SwingGUIFactory;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.InitIritgoException;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.client.gui.ClientGUI;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import org.swixml.SwingTagLibrary;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class implements the main Metix frame window.
 *
 * @version $Id: MetixClientGUI.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class MetixClientGUI
	extends BaseObject
	implements ClientGUI
{
	/** The desktop manager. */
	private SwingDesktopManager desktopManager;

	/** The desktop frame window. */
	private SwingDesktopFrame desktopFrame;

	/** This tabbed pane contains all desktops. */
	private JTabbedPane desktopTabs;

	/** For auto generated desktop names. */
	private int nextDesktopNumber;

	/** Clock display. */
	public IClockTextField clock;

	/** The internal frame manager. */
	private MetixDesktopManager metixDesktopManager;

	/** Status display: user name. */
	public JTextField user;

	/** Status display: deskription of the current desktop. */
	public JTextField description;

	/** The menu containing administration functions. */
	public JMenu adminMenu;

	/** A list of desktops. */
	private List desktopIdByIndex;

	/** Status progress bar. */
	public JProgressBar progress;

	/** Determines wether to draw antialiased. */
	private boolean drawAntialiased;

	/** Is the chatter visible. */
	private boolean isChatterVisible;

	/** Our tool bar. */
	private IToolBar toolbar;

	/** The current color scheme. */
	protected String colorScheme;

	/**
	 * Create a new client gui.
	 */
	public MetixClientGUI ()
	{
		nextDesktopNumber = 1;
		desktopIdByIndex = new ArrayList(10);
		drawAntialiased = false;
	}

	/**
	 * Retrive the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager ()
	{
		return desktopManager;
	}

	/**
	 * Initialize the client gui.
	 *
	 * @throws InitIritgoException If anything went wrong.
	 */
	public void init ()
		throws InitIritgoException
	{
		Engine.instance ().setGUIFactory (new SwingGUIFactory());

		SwingTagLibrary.getInstance ().registerTag ("itimecombobox", ITimeComboBox.class);
		SwingTagLibrary.getInstance ().registerTag (
			"istationsensorselector", IStationSensorSelector.class);
		SwingTagLibrary.getInstance ().registerTag (
			"idatareductionsensorselector", IDataReductionSensorSelector.class);
		SwingTagLibrary.getInstance ().registerTag ("isensorselector", ISensorSelector.class);
		SwingTagLibrary.getInstance ().registerTag ("iclocktextfield", IClockTextField.class);

		try
		{
			desktopFrame = new SwingDesktopFrame();
			desktopFrame.setTitle (
				Engine.instance ().getResourceService ().getStringWithoutException (
					"metix.applicationName"));
			desktopFrame.init ();
			desktopFrame.addCloseListener (
				new ActionListener()
				{
					public void actionPerformed (ActionEvent e)
					{
						quitAction.actionPerformed (e);
					}
				});

			desktopManager = new SwingDesktopManager();
			desktopManager.setDesktopFrame (desktopFrame);

			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader (AppPlugin.class.getClassLoader ());

			JFrame frame = desktopFrame.getJFrame ();

			frame.setIconImage (
				new ImageIcon(getClass ().getResource ("/resources/metix-icon-32.gif")).getImage ());

			frame.getContentPane ().setLayout (new BorderLayout());

			IMenuBar menubar =
				(IMenuBar) swingEngine.render (getClass ().getResource ("/swixml/MetixMenu.xml"));

			frame.setJMenuBar (menubar);

			toolbar =
				(IToolBar) swingEngine.render (
					getClass ().getResource ("/swixml/MetixToolBar.xml"));

			frame.getContentPane ().add (toolbar, BorderLayout.NORTH);

			JPanel statusBar =
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/MetixStatusBar.xml"));

			frame.getContentPane ().add (statusBar, BorderLayout.SOUTH);

			desktopTabs = new JTabbedPane();
			frame.getContentPane ().add (desktopTabs, BorderLayout.CENTER);

			desktopTabs.addChangeListener (
				new ChangeListener()
				{
					public void stateChanged (ChangeEvent e)
					{
						int selectedIndex = desktopTabs.getSelectedIndex ();

						if (selectedIndex != -1)
						{
							desktopManager.activateDesktopPane (
								(String) desktopIdByIndex.get (selectedIndex));
							setStatusDescription (desktopTabs.getTitleAt (selectedIndex));
						}
					}
				});

			JDesktopPane desktopPane = new JDesktopPane();

			metixDesktopManager = new MetixDesktopManager();
			desktopPane.setDesktopManager (metixDesktopManager);

			setColorScheme ("com.jgoodies.plaf.plastic.theme.KDE");
		}
		catch (Exception x)
		{
			Log.logError ("client", "MetixClientGUI.initDesktop", x.toString ());
			throw new InitIritgoException(x);
		}
	}

	/**
	 * Called when the server connection was lost.
	 * This method shuts down the client and redisplays the login dialog.
	 */
	public void lostNetworkConnection ()
	{
		JOptionPane.showMessageDialog (
			desktopFrame.getJFrame (),
			Engine.instance ().getResourceService ().getStringWithoutException (
				"metix-client.lostnetworkconnection"), "Metix", JOptionPane.OK_OPTION);

		IDesktopManager iDesktopManager = getDesktopManager ();

		iDesktopManager.closeAllDisplays ();
		removeAllDesktops ();

		CommandTools.performAsync (new ShowDialog("UserLoginGUIPane"));
	}

	/**
	 * Update the status display and set a new user name.
	 *
	 * @param userName The new user name.
	 */
	public void setStatusUser (String userName)
	{
		user.setText (userName);
	}

	/**
	 * Update the status display and set a new desktop description.
	 *
	 * @param text The new desktop description.
	 */
	public void setStatusDescription (String text)
	{
		description.setText (text);
	}

	/**
	 * Update the status display and set a new progress range.
	 *
	 * @param min The minimum progress value.
	 * @param max The maximum progress value.
	 */
	public void setStatusProgressRange (int min, int max)
	{
		progress.setMinimum (min);
		progress.setMaximum (max);
		progress.setValue (min);
	}

	/**
	 * Update the status display and set a new progress value.
	 *
	 * @param val The new progress value.
	 */
	public void setStatusProgress (int val)
	{
		progress.setValue (val);
	}

	/**
	 * Update the status display increment the current progress value.
	 */
	public void setStatusProgressStep ()
	{
		int val = progress.getValue ();

		if (progress.getMaximum () == 0)
		{
			return;
		}

		if (val + 1 >= progress.getMaximum ())
		{
			setStatusProgressRange (0, 0);
		}
		else
		{
			progress.setValue (val + 1);
		}
	}

	/**
	 * Set a new raster size in the internal frame manager.
	 *
	 * @param rasterSize The new raster size.
	 */
	public void setRasterSize (int rasterSize)
	{
		metixDesktopManager.setRasterSize (rasterSize);
	}

	/**
	 * Reload the menu bar.
	 * This method is called after a change to the language to reload
	 * the menu labels.
	 */
	public void reloadMenuBar ()
	{
		JFrame frame = desktopFrame.getJFrame ();

		((IMenuBar) frame.getJMenuBar ()).reloadText ();
	}

	/**
	 * Reload the tool bar.
	 * This method is called after a change to the language to reload
	 * the menu labels.
	 */
	public void reloadToolBar ()
	{
		toolbar.reloadText ();
	}

	/**
	 * Show/hide the administration menu.
	 *
	 * @param visible If true the admin menu is visible.
	 */
	public void setAdminMenuVisible (boolean visible)
	{
		adminMenu.setVisible (visible);
		clock.start ();
	}

	/**
	 * Start up the client gui.
	 */
	public void startGUI ()
	{
		desktopFrame.setVisible ();
	}

	/**
	 * Start up the application.
	 */
	public void startApplication ()
	{
		CommandTools.performAsync (new ShowDialog("UserLoginGUIPane"));
	}

	/**
	 * Shut down the client gui and close the application
	 */
	public void stopApplication ()
	{
	}

	/**
	 * Shutdown the client gui, the application runs...
	 */
	public void stopGUI ()
	{
		desktopFrame.addCloseListener (
			new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
				}
			});

		clock.stop ();

		desktopFrame.close ();
	}

	/**
	 * Get an iterator over all id's of the desktop panes.
	 *
	 * @return A desktop pane id iterator.
	 */
	public Iterator desktopIdIterator ()
	{
		return desktopIdByIndex.iterator ();
	}

	/**
	 * Create a new desktop page.
	 *
	 * @param desktopName The name of the new desktop page.
	 */
	public void createDesktop (final String desktopName)
	{
		createDesktop (new UID().toString (), desktopName);
	}

	/**
	 * Create a new desktop page.
	 *
	 * @param desktopId The id of the new desktop page.
	 * @param desktopName The name of the new desktop page.
	 */
	public void createDesktop (final String desktopId, final String desktopName)
	{
		final JDesktopPane desktopPane = new JDesktopPane();

		desktopPane.setDesktopManager (metixDesktopManager);
		desktopManager.addDesktopPane (desktopId, desktopPane, desktopName);
		desktopIdByIndex.add (desktopId);

		try
		{
			SwingUtilities.invokeLater (
				new Runnable()
				{
					public void run ()
					{
						desktopTabs.addTab (desktopName, desktopPane);
						desktopTabs.setSelectedIndex (desktopTabs.getTabCount () - 1);
					}
				});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Remove all desktops.
	 */
	public void removeAllDesktops ()
	{
		desktopTabs.removeAll ();
		desktopManager.removeAllDesktopPanes ();
		desktopIdByIndex.clear ();
	}

	/**
	 * Activate a desktop page.
	 *
	 * @param desktopId The id of the desktop page to activate.
	 */
	public void activateDesktop (final String desktopId)
	{
		desktopManager.activateDesktopPane (desktopId);

		try
		{
			SwingUtilities.invokeLater (
				new Runnable()
				{
					public void run ()
					{
						desktopTabs.setSelectedIndex (desktopIdByIndex.indexOf (desktopId));
					}
				});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Set an icon on the tab header of a desktop pane.
	 *
	 * @param desktopPaneId The id of the desktop pane for which to set the icon-
	 * @param icon The icon to set.
	 */
	public void setDesktopPaneIcon (String desktopPaneId, Icon icon)
	{
		for (int i = 0; i < desktopIdByIndex.size (); ++i)
		{
			if (desktopPaneId.equals ((String) desktopIdByIndex.get (i)))
			{
				if (desktopTabs.getIconAt (i) != icon)
				{
					desktopTabs.setIconAt (i, icon);
				}

				break;
			}
		}
	}

	/**
	 * Check wether to draw antialiased.
	 *
	 * @return True if we should draw antialiased.
	 */
	public boolean isDrawAntialiased ()
	{
		return drawAntialiased;
	}

	/**
	 * Determine wether to draw antialiased.
	 *
	 * @param drawAntialiased True if we should draw antialiased.
	 */
	public void setDrawAntialiased (boolean drawAntialiased)
	{
		this.drawAntialiased = drawAntialiased;
	}

	/**
	 * Get the name of the current color scheme.
	 *
	 * @retrurn The color scheme name.
	 */
	public String getColorScheme ()
	{
		return colorScheme;
	}

	/**
	 * Change the color scheme.
	 *
	 * @param colorScheme The new color scheme.
	 */
	public void setColorScheme (String colorScheme)
	{
		try
		{
			this.colorScheme = colorScheme;

			com.jgoodies.plaf.plastic.PlasticLookAndFeel.setMyCurrentTheme (
				(com.jgoodies.plaf.plastic.PlasticTheme) Class.forName (colorScheme).newInstance ());
			com.jgoodies.plaf.Options.setPopupDropShadowEnabled (true);
			UIManager.put ("jgoodies.popupDropShadowEnabled", Boolean.TRUE);

			LookAndFeel lnf =
				(LookAndFeel) getClass ().getClassLoader ()
								  .loadClass ("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel")
								  .newInstance ();

			UIManager.setLookAndFeel (lnf);

			UIManager.getLookAndFeelDefaults ().put ("ClassLoader", getClass ().getClassLoader ());

			if (desktopFrame != null)
			{
				SwingUtilities.updateComponentTreeUI (desktopFrame);

				for (Iterator i = desktopManager.getDisplayIterator (); i.hasNext ();)
				{
					IDisplay display = (IDisplay) i.next ();

					if (display instanceof IDialog)
					{
						SwingUtilities.updateComponentTreeUI (
							((SwingDialogFrame) ((IDialog) display).getDialogFrame ()));
					}
				}
			}
		}
		catch (Exception x)
		{
			Log.logError ("client", "MetixClientGUI.setColorScheme", x.toString ());
		}
	}

	/**
	 * Terminate the client.
	 */
	public Action quitAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				MetixClientManager manager =
					(MetixClientManager) Engine.instance ().getManagerRegistry ().getManager (
						"client");

				manager.saveAllActiveDisplaysOnServer ();

				IritgoEngine.instance ().shutdown ();
			}
		};

	/**
	 * Display the preferences dialog.
	 */
	public Action preferencesAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				UserProfile userProfile = (UserProfile) AppContext.instance ().getAppObject ();

				CommandTools.performAsync (new ShowDialog("PreferencesGUIPane", userProfile));
			}
		};

	/**
	 * Display the configuration dialog for inserting a new report
	 * instrument.
	 */
	public Action insertReportInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateReportInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new simple
	 * instrument.
	 */
	public Action insertSimpleInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateSimpleInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new list
	 * instrument.
	 */
	public Action insertListInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateListInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a bar list
	 * instrument.
	 */
	public Action insertBarInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateBarInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new round
	 * instrument.
	 */
	public Action insertRoundInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateRoundInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new line
	 * chart.
	 */
	public Action insertLineChartAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateLineChart");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new
	 * wind rose instrument.
	 */
	public Action insertWindRoseInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateWindRoseInstrument");
			}
		};

	/**
	 * Display the configuration dialog for inserting a new
	 * web instrument.
	 */
	public Action insertWebInstrumentAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("CreateWebInstrument");
			}
		};

	/**
	 * Display the configuration dialog of the currently active
	 * instrument.
	 */
	public Action editInstrumentOrDiagramAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("EditInstrument");
			}
		};

	/**
	 * Print the currently active instrument.
	 */
	public Action printInstrumentOrDiagramAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				IDisplay display = desktopManager.getActiveDisplay ();

				if (
					display != null && display.getGUIPane () instanceof InstrumentDisplay &&
					((InstrumentDisplay) display.getGUIPane ()).isPrintable ())
				{
					((InstrumentDisplay) display.getGUIPane ()).print ();
				}
			}
		};

	/**
	 * Create a new desktop page.
	 */
	public Action newDesktopAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				final String desktopName =
					JOptionPane.showInputDialog (
						desktopFrame.getJFrame (),
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-client.pleaseInputDesktopName"), "Desktop " +
						nextDesktopNumber++);

				if (desktopName == null)
				{
					return;
				}

				createDesktop (desktopName);
			}
		};

	/**
	 * Rename the currently active desktop page.
	 */
	public Action renameDesktopAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				String desktopId = desktopManager.getActiveDesktopPaneId ();
				String oldName = desktopManager.getTitle (desktopId);
				final String newName =
					JOptionPane.showInputDialog (
						desktopFrame.getJFrame (),
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix-client.pleaseInputDesktopName"), oldName);

				if (newName != null)
				{
					desktopManager.setTitle (desktopId, newName);

					SwingUtilities.invokeLater (
						new Runnable()
						{
							public void run ()
							{
								desktopTabs.setTitleAt (desktopTabs.getSelectedIndex (), newName);
								setStatusDescription (newName);
							}
						});
				}
			}
		};

	/**
	 * delete the currently active desktop page.
	 */
	public Action deleteDesktopAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (
					desktopTabs.getTabCount () > 0 &&
					JOptionPane.showConfirmDialog (
						desktopFrame.getJFrame (),
						Engine.instance ().getResourceService ().getStringWithoutException (
							"metix.deleteDesktopQuestion"), "Metix", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					final int selectedTabIndex = desktopTabs.getSelectedIndex ();

					List displaysToDelete =
						desktopManager.getDisplaysOnDesktopPane (
							desktopManager.getActiveDesktopPaneId ());

					for (Iterator i = displaysToDelete.iterator (); i.hasNext ();)
					{
						IDisplay display = (IDisplay) i.next ();

						display.systemClose ();
					}

					desktopManager.removeDesktopPane (desktopManager.getActiveDesktopPaneId ());
					desktopTabs.removeTabAt (selectedTabIndex);
					desktopIdByIndex.remove (selectedTabIndex);

					if (desktopManager.getDesktopPaneCount () == 0)
					{
						createDesktop (
							Engine.instance ().getResourceService ().getStringWithoutException (
								"metix.standardDesktop"));
					}
					else
					{
						desktopManager.activateDesktopPane (
							(String) desktopIdByIndex.get (desktopTabs.getSelectedIndex ()));
						setStatusDescription (
							desktopTabs.getTitleAt (desktopTabs.getSelectedIndex ()));
					}
				}
			}
		};

	/**
	 * Display the chatter window.
	 */
	public Action showChatter =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("show.chatter");
			}
		};

	/**
	 * Display the user administration dialog.
	 */
	public Action manageUsers =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					long uniqueId =
						AppContext.instance ().getUser ().getNamedIritgoObjectId (
							"MetixUserRegistry");

					CommandTools.performAsync (new ShowDialog("UserListGUIPane", uniqueId));
				}
				catch (NoSuchIObjectException x)
				{
				}
			}
		};

	/**
	 * Display the gaging system administration dialog.
	 */
	public Action manageGagingSystemsAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					long uniqueId =
						AppContext.instance ().getUser ().getNamedIritgoObjectId (
							"InterfaceRegistry");

					CommandTools.performAsync (new ShowDialog("GagingSystemListEditor", uniqueId));
				}
				catch (NoSuchIObjectException x)
				{
				}
			}
		};

	/**
	 * Display the gaging station administration dialog.
	 */
	public Action manageGagingStationsAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					long uniqueId =
						AppContext.instance ().getUser ().getNamedIritgoObjectId (
							"GagingStationRegistry");

					CommandTools.performAsync (new ShowDialog("GagingStationListEditor", uniqueId));
				}
				catch (NoSuchIObjectException x)
				{
				}
			}
		};

	/**
	 * Transfer the current instrument layout to the server.
	 */
	public Action saveDesktopAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				MetixClientManager manager =
					(MetixClientManager) Engine.instance ().getManagerRegistry ().getManager (
						"client");

				manager.saveAllActiveDisplaysOnServer ();

				JOptionPane.showMessageDialog (
					desktopFrame.getJFrame (),
					Engine.instance ().getResourceService ().getStringWithoutException (
						"metix.desktopLayoutSuccessfullySaved"), "Metix", JOptionPane.OK_OPTION);
			}
		};

	/**
	 * Display the record measurement dialog.
	 */
	public Action recordMeasurementAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					long uniqueId =
						AppContext.instance ().getUser ().getNamedIritgoObjectId (
							"GagingStationRegistry");

					CommandTools.performAsync (new ShowDialog("MeasurementRecordDialog", uniqueId));
				}
				catch (NoSuchIObjectException x)
				{
				}
			}
		};

	/**
	 * Display the about dialog.
	 */
	public Action helpAboutAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("AboutPane"));
			}
		};

	/**
	 * Display the system monitor gui pane
	 */
	public Action showSystemMonitor =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync ("ShowSystemMonitor");
			}
		};

	/**
	 * Manage the interfaces.
	 */
	public Action manageInterfacesAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				try
				{
					long uniqueId =
						AppContext.instance ().getUser ().getNamedIritgoObjectId (
							"InterfaceRegistry");

					CommandTools.performAsync (new ShowDialog("InterfaceListEditor", uniqueId));
				}
				catch (NoSuchIObjectException x)
				{
				}
			}
		};

	/**
	 * Display the dimension administration dialog.
	 */
	public Action manageDimensionAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("DimensionEditor"));
			}
		};

	/**
	 * Display the license administration dialog.
	 */
	public Action manageLicensingAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("KeyCheckConfigurator"));
			}
		};

	/**
	 * Display the data reduction administration dialog.
	 */
	public Action manageDataReductionAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("DataReductionConfigurator"));
			}
		};

	/**
	* Display the configuration dialog for the export dialog
	*/
	public Action showExporterWizardAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				CommandTools.performAsync (new ShowDialog("ExporterWizard"));
			}
		};
}