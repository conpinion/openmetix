/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.gui;


import de.iritgo.openmetix.app.gui.InstrumentConfiguratorGUIPane;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.IFieldLabel;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.gui.swing.ITextField;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.client.command.CloseDisplay;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


public class KeyCheckResultDisplay
	extends InstrumentConfiguratorGUIPane
{
	public ILabel labelResultChecking;
	public ILabel labelResultFalse;
	public ILabel labelResultTrue;
	public IFieldLabel labelNumber;
	public ITextField textNumber;
	public boolean checkStart = false;

	/**
	 * Create a new MinMidMaxResultGUIPane
	 */
	public KeyCheckResultDisplay ()
	{
		super("KeyCheckResultDisplay");
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
					getClass ().getResource ("/swixml/KeyCheckResultDisplay.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 200, 200, null));

			labelResultFalse.setVisible (false);
			labelResultTrue.setVisible (false);
			labelNumber.setVisible (false);
			textNumber.setVisible (false);
			textNumber.setEditable (false);
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
		return new KeyCheckResultDisplay();
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
	}

	public void viewKeyEntry (boolean entry, int number, boolean start)
	{
		// 		if(entry) //the result of key checking is true
		// 		{
		if (start)
		{
			CommandTools.performAsync (new CloseDisplay("KeyCheckResultDisplay"));
		}
		else
		{
			labelResultFalse.setVisible (false);
			labelResultChecking.setVisible (false);
			labelResultTrue.setVisible (true);
			labelNumber.setVisible (true);
			textNumber.setVisible (true);
			textNumber.setText (String.valueOf (number));
		}

		// 		}
		// 		else
		// 		{
		// 			if(start)
		// 			{
		// 				CommandTools.performAsync (new ShowDialog ("KeyCheckStart"));
		// 			}
		// 			else
		// 			{
		// 				labelResultChecking.setVisible(false);
		// 				labelResultTrue.setVisible(false);
		// 				labelResultFalse.setVisible(true);
		// 			}
		// 		}
	}

	public void setCheckStart (boolean value)
	{
		this.checkStart = value;
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