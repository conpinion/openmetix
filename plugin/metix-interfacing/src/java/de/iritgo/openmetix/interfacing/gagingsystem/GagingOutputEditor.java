/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing.gagingsystem;


import de.iritgo.openmetix.app.AppPlugin;
import de.iritgo.openmetix.app.util.Dimension;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Properties;


/**
 * This gui pane is used to edit gaging system outputs.
 *
 * @version $Id: GagingOutputEditor.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class GagingOutputEditor
	extends SwingGUIPane
{
	/** The output number. */
	public ITextField number;

	/** The name of the output. */
	public ITextField name;

	/** The output dimension. */
	public JComboBox dimension;

	/** Panel containng the custom output parameters. */
	public JPanel customParameterPanel;

	/** Custom config properties. */
	protected Properties customConfig;

	/**
	 * Create a new GagingOutputEditor.
	 */
	public GagingOutputEditor ()
	{
		this("GagingOutputEditor");
	}

	/**
	 * Create a new GagingOutputEditor.
	 *
	 * @param id The gui pane id.
	 */
	public GagingOutputEditor (String id)
	{
		super(id);
		customConfig = new Properties();
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
					getClass ().getResource ("/swixml/GagingOutputEditor.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			dimension.setModel (Dimension.createComboBoxModel ());

			JPanel customPanel = createCustomParameterPanel ();

			if (customPanel != null)
			{
				customParameterPanel.add (BorderLayout.NORTH, customPanel);
			}
		}
		catch (Exception x)
		{
			Log.logError ("client", "GagingOutputEditor.initGUI", x.toString ());
		}
	}

	/**
	 * Create a panel for custom output parameter editing.
	 *
	 * @return The custom parameter panel.
	 */
	protected JPanel createCustomParameterPanel ()
	{
		return null;
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		GagingOutput output = (GagingOutput) iobject;

		if (output == null)
		{
			return;
		}

		number.setText (getDisplay ().getProperties ().get ("index").toString ());
		name.setText (output.getName ());
		dimension.setSelectedItem (output.getDimensionUnit ());
		customConfig = output.getCustomProperties ();
		loadFromProperties ();

		name.selectAll ();
	}

	/**
	 * Store the gui values.
	 */
	public void storeToObject ()
	{
		GagingOutput output = (GagingOutput) iobject;

		output.setName (name.getText ());
		output.setDimensionUnit ((Dimension.Unit) dimension.getSelectedItem ());

		customConfig.clear ();
		storeToProperties ();
		output.setCustomProperties (customConfig);

		output.update ();
	}

	/**
	 * Load the configuration properties into the gui.
	 */
	protected void loadFromProperties ()
	{
	}

	/**
	 * Store the gui values to the configuration properties.
	 */
	protected void storeToProperties ()
	{
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject ()
	{
		return new GagingOutput();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new GagingOutputEditor();
	}

	/**
	 * Save the data object and close the display.
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
	 * Close the display.
	 */
	public Action cancelAction =
		new AbstractAction()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (sessionContext != null)
				{
					GagingSystem list = (GagingSystem) sessionContext.get ("gagingsystem");

					list.removeOutput ((GagingOutput) iobject);
				}

				display.close ();
			}
		};
}