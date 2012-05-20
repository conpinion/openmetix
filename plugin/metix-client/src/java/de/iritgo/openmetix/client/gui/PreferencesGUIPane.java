/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.tools.NumberTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;


/**
 * This gui pane is used to edit the user preferences.
 *
 * @version $Id: PreferencesGUIPane.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class PreferencesGUIPane
	extends SwingGUIPane
{
	/** Language selection. */
	public JComboBox language;

	/** Look and feel selection. */
	public JComboBox lookAndFeel;

	/** Wether to always redraw the window contents. */
	public JCheckBox alwaysDrawWindowContents;

	/** Wether to draw antialiased instrument displays. */
	public JCheckBox drawAntiAliased;

	/** Wether to align instruments to a grid. */
	public JCheckBox alignWindowsToRaster;

	/** The grid spacing. */
	public JTextField rasterSize;

	/**
	 * Items of the color scheme combo box.
	 */
	private class ColorSchemeItem
	{
		public String name;
		public String className;

		public ColorSchemeItem (String name, String className)
		{
			this.name = name;
			this.className = className;
		}

		public String toString ()
		{
			return name;
		}
	}

	/**
	 * Items of the language combo box.
	 */
	private class LanguageItem
	{
		/** Language description. */
		public String label;

		/** Locale id of the language. */
		public String localeId;

		/**
		 * Create a new LanguageItem.
		 *
		 * @param label Language description.
		 * @param localeId Locale id of the language.
		 */
		public LanguageItem (String label, String localeId)
		{
			this.label = label;
			this.localeId = localeId;
		}

		/**
		 * Create a string representation of the lanuage item.
		 *
		 * @return The items string representation.
		 */
		public String toString ()
		{
			return label;
		}
	}

	/**
	 * Create a new PreferencesGUIPane.
	 */
	public PreferencesGUIPane ()
	{
		super("PreferencesGUIPane");
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
				(JPanel) swingEngine.render (getClass ().getResource ("/swixml/Preferences.xml"));

			alwaysDrawWindowContents.setSelected (true);
			drawAntiAliased.setSelected (true);

			language.addItem (new LanguageItem("Deutsch", "de"));
			language.addItem (new LanguageItem("English", "en"));

			List themes = com.jgoodies.plaf.plastic.PlasticLookAndFeel.getInstalledThemes ();

			for (Iterator i = themes.iterator (); i.hasNext ();)
			{
				MetalTheme theme = (MetalTheme) i.next ();

				lookAndFeel.addItem (
					new ColorSchemeItem(theme.getName (), theme.getClass ().getName ()));
			}

			// 			UIManager.LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels ();
			// 			for (int i = 0; i < installed.length; i++)
			// 			{
			// 				lookAndFeel.addItem (installed[i].getName ());
			// 				if (installed[i].getName ().equals (UIManager.getLookAndFeel ()
			// 															 .getName ()))
			// 				{
			// 					lookAndFeel.setSelectedItem (installed[i].getName ());
			// 				}
			// 			}
			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "PreferencesGUIPane.initGUI", x.toString ());
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new PreferencesGUIPane();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new UserProfile();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		UserProfile profile = (UserProfile) iobject;

		Engine.instance ().getProxyEventRegistry ().addEventListener (
			profile.getPreferences (), this);

		for (int i = 0; i < language.getItemCount (); ++i)
		{
			if (
				((LanguageItem) language.getItemAt (i)).localeId.equals (
					profile.getPreferences ().getLanguage ()))
			{
				language.setSelectedIndex (i);

				break;
			}
		}

		for (int i = 0; i < lookAndFeel.getItemCount (); ++i)
		{
			if (
				(((ColorSchemeItem) lookAndFeel.getItemAt (i))).className.equals (
					profile.getPreferences ().getLookAndFeel ()))
			{
				lookAndFeel.setSelectedIndex (i);

				break;
			}
		}

		// 		for (int i = 0; i < lookAndFeel.getItemCount (); ++i)
		// 		{
		// 			if (lookAndFeel.getItemAt (i).toString ().equals (profile.getPreferences ()
		// 																	 .getLookAndFeel ()))
		// 			{
		// 				lookAndFeel.setSelectedIndex (i);
		// 				break;
		// 			}
		// 		}
		alwaysDrawWindowContents.setSelected (
			profile.getPreferences ().getAlwaysDrawWindowContents ());
		drawAntiAliased.setSelected (profile.getPreferences ().getDrawAntiAliased ());
		alignWindowsToRaster.setSelected (profile.getPreferences ().getAlignWindowsToRaster ());
		rasterSize.setText (String.valueOf (profile.getPreferences ().getRasterSize ()));
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		UserProfile profile = (UserProfile) iobject;

		profile.getPreferences ().setLanguage (
			((LanguageItem) language.getSelectedItem ()).localeId);

		profile.getPreferences ().setLookAndFeel (
			((ColorSchemeItem) lookAndFeel.getSelectedItem ()).className);

		// 		profile.getPreferences ().setLookAndFeel (lookAndFeel.getSelectedItem ().toString ());
		profile.getPreferences ().setAlwaysDrawWindowContents (
			alwaysDrawWindowContents.isSelected ());
		profile.getPreferences ().setDrawAntiAliased (drawAntiAliased.isSelected ());
		profile.getPreferences ().setAlignWindowsToRaster (alignWindowsToRaster.isSelected ());
		profile.getPreferences ().setRasterSize (NumberTools.toInt (rasterSize.getText (), 10));
		profile.getPreferences ().update ();
	}

	/**
	 * Store the preferences and close the dialog.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
				storeToObject ();
			}
		};

	/**
	 * Close the dialog.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				display.close ();
			}
		};
}