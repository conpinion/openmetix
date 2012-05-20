/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.JCheckBox;


/**
 * ICheckBox is an extended JCheckBox that loads it's labels from the
 * application resources.
 *
 * @version $Id: ICheckBox.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class ICheckBox
	extends JCheckBox
{
	/**
	 * Create a check box with no text or icon.
	 */
	public ICheckBox ()
	{
		super();
	}

	/**
	 * Create a check box with text.
	 *
	 * @param text The text of the check box.
	 */
	public ICheckBox (String textKey)
	{
		super(textKey);
	}

	/**
	 * Set the check box text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	public void setText (String textKey)
	{
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}
}