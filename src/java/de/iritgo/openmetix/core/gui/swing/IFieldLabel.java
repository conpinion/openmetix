/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.JLabel;


/**
 * IFieldLabel is an extended JLabel that loads it's labels from the
 * application resources and appends a ':' to them.
 *
 * @version $Id: IFieldLabel.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IFieldLabel
	extends JLabel
{
	/**
	 * Create a field label with no text or icon.
	 */
	public IFieldLabel ()
	{
	}

	/**
	 * Create a field label with text.
	 *
	 * @param text The text of the label.
	 */
	public IFieldLabel (String textKey)
	{
		super(textKey);
	}

	/**
	 * Set the label text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	public void setText (String textKey)
	{
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey) + ":");
	}
}