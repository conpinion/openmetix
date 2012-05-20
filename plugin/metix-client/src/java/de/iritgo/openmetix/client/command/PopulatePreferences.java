/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.command;


import de.iritgo.openmetix.app.userprofile.Preferences;
import de.iritgo.openmetix.client.gui.MetixClientGUI;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.swing.SwingDesktopManager;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.ChangeLanguage;
import de.iritgo.openmetix.framework.command.CommandTools;
import javax.swing.SwingUtilities;
import java.util.Locale;


/**
 * This command updates the gui depending on the preferences found in
 * the users preferences object.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: PopulatePreferences.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class PopulatePreferences
	extends Command
{
	/**
	 * Create a new command object.
	 */
	public PopulatePreferences ()
	{
		super("PopulatePreferences");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		final Preferences preferences = (Preferences) properties.get ("preferences");

		final MetixClientGUI gui = (MetixClientGUI) Client.instance ().getClientGUI ();

		gui.setRasterSize (
			preferences.getAlignWindowsToRaster () ? preferences.getRasterSize () : 0);

		((SwingDesktopManager) gui.getDesktopManager ()).setDrawAlways (
			preferences.getAlwaysDrawWindowContents ());

		Locale locale = new Locale(preferences.getLanguage ());

		if (! AppContext.instance ().getLocale ().equals (locale))
		{
			CommandTools.performSimple (new ChangeLanguage(locale));

			try
			{
				SwingUtilities.invokeAndWait (
					new Runnable()
					{
						public void run ()
						{
							gui.reloadMenuBar ();
							gui.reloadToolBar ();
						}
					});
			}
			catch (Exception x)
			{
			}
		}
		else
		{
			if (gui.isDrawAntialiased () != preferences.getDrawAntiAliased ())
			{
				gui.setDrawAntialiased (preferences.getDrawAntiAliased ());
				Client.instance ().getClientGUI ().getDesktopManager ().saveVisibleDisplays ();
				Client.instance ().getClientGUI ().getDesktopManager ().closeAllDisplays ();
				Client.instance ().getClientGUI ().getDesktopManager ().showSavedDisplays ();
			}
		}

		if (! gui.getColorScheme ().equals (preferences.getLookAndFeel ()))
		{
			try
			{
				SwingUtilities.invokeAndWait (
					new Runnable()
					{
						public void run ()
						{
							gui.setColorScheme (preferences.getLookAndFeel ());
						}
					});
			}
			catch (Exception x)
			{
			}
		}

		// 		if (! UIManager.getLookAndFeel ().getName ().equals (preferences.getLookAndFeel ()) &&
		// 			! "Mac OS".equals (System.getProperty ("os.name")))
		// 		{
		// 			UIManager.LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels ();
		// 			for (int i = 0; i < installed.length; i++)
		// 			{
		// 				if (preferences.getLookAndFeel ().equals (installed[i].getName ()))
		// 				{
		// 					final String lnfClassName = installed[i].getClassName ();
		// 					try
		// 					{
		// 						SwingUtilities.invokeAndWait (
		// 							new Runnable()
		// 							{
		// 								public void run ()
		// 								{
		// 									try
		// 									{
		// 										UIManager.setLookAndFeel (lnfClassName);
		// 										SwingUtilities.updateComponentTreeUI (
		// 											((SwingDesktopManager) gui.getDesktopManager ()).getActiveDesktopPane ()
		// 											 .getRootPane ());
		// 									}
		// 									catch (Exception x)
		// 									{
		// 									}
		// 								}
		// 							});
		// 					}
		// 					catch (Exception x)
		// 					{
		// 					}
		// 					break;
		// 				}
		// 			}
		// 			SwingUtilities.invokeLater (
		// 				new Runnable()
		// 				{
		// 					public void run ()
		// 					{
		// 						for (
		// 							Iterator i =
		// 								((SwingDesktopManager) gui.getDesktopManager ()).getDisplayIterator ();
		// 							i.hasNext ();)
		// 						{
		// 							try
		// 							{
		// 								((InstrumentDisplay) ((IDisplay) i.next ()).getGUIPane ()).configureDisplay ();
		// 							}
		// 							catch (ClassCastException noInstrumentDisplayIgnored)
		// 							{
		// 							}
		// 						}
		// 					}
		// 				});
		// 		}
	}
}