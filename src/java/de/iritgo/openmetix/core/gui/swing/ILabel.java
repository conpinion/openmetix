/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.Icon;
import javax.swing.JLabel;


/**
 * ILabel is an extended JLabel that loads it's labels from the
 * application resources.
 *
 * @version $Id: ILabel.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ILabel
	extends JLabel
{
	/**
	 * Create a label with no text or icon.
	 */
	public ILabel ()
	{
	}

	/**
	 * Create a label with text.
	 *
	 * @param text The text of the label.
	 */
	public ILabel (String textKey)
	{
		super(textKey);
	}

	/**
	 * Create a label with an icon.
	 *
	 * @param icon The Icon image to display on the label.
	 */
	public ILabel (Icon icon)
	{
		super(icon);
	}

	/**
	 * Set the label text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	public void setText (String textKey)
	{
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}
}