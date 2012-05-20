/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.app.userprofile.UserProfile;
import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IButton;
import de.iritgo.openmetix.core.gui.swing.IFieldLabel;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * This gui pane is used to edit the user preferences.
 *
 * @version $Id: DimensionEditor.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class DimensionEditor
	extends SwingGUIPane
{
	/** The Key for the new dimension. */
	public ITextField newDimensionKey;

	/** German name of the new dimension. */
	public ITextField newGermanName;

	/** English name of the new dimesnion. */
	public ITextField newEnglishName;

	/** The Key for the new Unit. */
	public ITextField newUnitKey;

	/** The minimum value for the new Unit. */
	public ITextField newUnitMin;

	/** The mximum value for the new Unit. */
	public ITextField newUnitMax;

	/** Name of the new unit. */
	public ITextField newUnitName;

	/** The output dimensionkey. */
	public JComboBox selectDimensionKey;

	/** Label for the selected dimensionkey */
	public IFieldLabel dimensionKeyLabel;

	/** Label for the new dimensionkey */
	public IFieldLabel newDimensionKeyLabel;

	/** Label for the german dimension name */
	public IFieldLabel germanDimensionNameLabel;

	/** Label for the english dimension name */
	public IFieldLabel englishDimensionNameLabel;
	public IButton okButton;

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
	public DimensionEditor ()
	{
		super("DimensionEditor");
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
				(JPanel) swingEngine.render (
					getClass ().getResource ("/swixml/DimensionEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			selectDimensionKey.setModel (Dimension.createComboBoxModelKey ());

			okButton.setEnabled (false);

			selectDimensionKey.addItemListener (
				new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						JComboBox selectedChoice = (JComboBox) e.getSource ();

						if (selectedChoice.getSelectedIndex () > 0)
						{
							newDimensionKey.setEnabled (false);
							newGermanName.setEnabled (false);
							newEnglishName.setEnabled (false);
							checkEntries ();
						}
						else
						{
							newDimensionKey.setEnabled (true);
							newGermanName.setEnabled (true);
							newEnglishName.setEnabled (true);
							okButton.setEnabled (false);
							checkEntries ();
						}
					}
				});

			newDimensionKey.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newGermanName.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newEnglishName.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newUnitKey.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newUnitMin.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newUnitMax.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});

			newUnitName.addKeyListener (
				new KeyAdapter()
				{
					public void keyReleased (KeyEvent event)
					{
						checkEntries ();
					}
				});
		}
		catch (Exception x)
		{
			Log.logError ("client", "DiemensionEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new DimensionEditor();
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
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	public void storeToObject ()
	{
		if (selectDimensionKey.getSelectedIndex () == 0)
		{
			double min = Double.parseDouble (newUnitMin.getText ());
			double max = Double.parseDouble (newUnitMax.getText ());

			Dimension.addDimensionUnit (
				newDimensionKey.getText (), newUnitKey.getText (), min, max);
			Dimension.writeDimensionUnitRessource (
				newDimensionKey.getText (), newGermanName.getText (), newEnglishName.getText (),
				newUnitKey.getText (), newUnitName.getText ());
		}
		else
		{
			double min = Double.parseDouble (newUnitMin.getText ());
			double max = Double.parseDouble (newUnitMax.getText ());

			Dimension.addUnit (
				(String) selectDimensionKey.getSelectedItem (), newUnitKey.getText (), min, max);
			Dimension.writeUnitRessource (
				(String) selectDimensionKey.getSelectedItem (), newUnitKey.getText (),
				newUnitName.getText ());
		}
	}

	/**
	 * Check the entries. Min. and max. value must be values of the type double.
	 *
	 */
	public void checkEntries ()
	{
		if (selectDimensionKey.getSelectedIndex () == 0)
		{
			try
			{
				double min = Double.parseDouble (newUnitMin.getText ());
				double max = Double.parseDouble (newUnitMax.getText ());

				if (
					(! newDimensionKey.getText ().equals ("")) &&
					(! newGermanName.getText ().equals ("")) &&
					(! newEnglishName.getText ().equals ("")) &&
					(! newUnitKey.getText ().equals ("")) &&
					(! newUnitName.getText ().equals ("")))
				{
					okButton.setEnabled (true);
				}
				else
				{
					okButton.setEnabled (false);
				}
			}
			catch (Exception e)
			{
				okButton.setEnabled (false);
			}
		}
		else
		{
			try
			{
				double min = Double.parseDouble (newUnitMin.getText ());
				double max = Double.parseDouble (newUnitMax.getText ());

				if ((! newUnitKey.getText ().equals ("")) &&
					(! newUnitName.getText ().equals ("")))
				{
					okButton.setEnabled (true);
				}
				else
				{
					okButton.setEnabled (false);
				}
			}
			catch (NumberFormatException e)
			{
				okButton.setEnabled (false);
			}
		}
	}

	/**
	 * Store the preferences and close the dialog.
	 */
	public Action okAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				storeToObject ();
				display.close ();
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