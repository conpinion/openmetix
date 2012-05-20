/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.branding.lutix;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.logger.Log;

import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import org.swixml.SwingEngine;


/**
* AboutPane
*
* @version $Id: AboutPane.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
*/
public class AboutPane
	extends SwingGUIPane
{
	/**
	 * Standard constructor
	 */
	public AboutPane ()
	{
		super("AboutPane");
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

			swingEngine.setClassLoader (AboutPane.class.getClassLoader ());

			JPanel panel =
				(JPanel) swingEngine.render (getClass ().getResource ("/swixml/AboutPane.xml"));

			content.add (
				panel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
		}
		catch (Exception x)
		{
			Log.logError ("client", "AboutPane.initGUI", x.toString ());
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public GUIPane cloneGUIPane ()
	{
		return new AboutPane();
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
}