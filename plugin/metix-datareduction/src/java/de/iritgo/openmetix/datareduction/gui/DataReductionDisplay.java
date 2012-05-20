/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.gui;


import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.iobject.IObject;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


/**
 * @version $Id: DataReductionDisplay.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class DataReductionDisplay
	extends InstrumentConfiguratorGUIPane
{
	public ILabel resultLabelChecking;
	public ILabel resultLabelTrue;
	public ILabel resultLabelFalse;

	public DataReductionDisplay ()
	{
		super("DataReductionDisplay");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
		super.initGUI ();

		try
		{
			JPanel panel =
				(JPanel) new SwingEngine(this).render (
					getClass ().getResource ("/swixml/DataReductionDisplay.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 200, 200, null));

			resultLabelTrue.setVisible (false);
			resultLabelFalse.setVisible (false);
		}
		catch (Exception x)
		{
		}

		super.loadFromObject ();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
		super.loadFromObject ();
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
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new DataReductionDisplay();
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
	}

	public void setResult (boolean result)
	{
		if (result)
		{
			resultLabelTrue.setVisible (true);
			resultLabelFalse.setVisible (false);
			resultLabelChecking.setVisible (false);
		}
		else
		{
			resultLabelFalse.setVisible (true);
			resultLabelTrue.setVisible (false);
			resultLabelChecking.setVisible (false);
		}
	}

	/**
	 * Close the result window
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