/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


/**
 * ITitledPanel is an extended JPanel with a titled border that loads it's labels
 * from the application resources.
 *
 * @version $Id: ITitledPanel.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ITitledPanel
	extends JPanel
{
	/** The titled border. */
	TitledBorder border;

	/**
	 * Create a new ITitledPanel.
	 */
	public ITitledPanel ()
	{
		border = new TitledBorder(" ");
		setBorder (border);
	}

	/**
	 * Set the border title.
	 *
	 * @param titleKey The title specified by a resource key.
	 */
	public void setTitle (String titleKey)
	{
		border.setTitle (
			Engine.instance ().getResourceService ().getStringWithoutException (titleKey));
	}
}