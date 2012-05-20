/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


/**
 * IButton is an extended JButton that loads it's labels from the
 * application resources.
 *
 * @version $Id: IButton.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IButton
	extends JButton
{
	/** The resource key of the button label. */
	private String textKey;

	/** The resource key of the button's tool tip. */
	private String toolTipTextKey;

	/**
	 * Create a button with no text or icon.
	 */
	public IButton ()
	{
		super();
	}

	/**
	 * Create a button where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new button.
	 */
	public IButton (Action action)
	{
		super(action);
	}

	/**
	 * Create a button with an icon.
	 *
	 * @param icon The Icon image to display on the button.
	 */
	public IButton (Icon icon)
	{
		super(icon);
	}

	/**
	 * Create a button with text.
	 *
	 * @param text The text of the button.
	 */
	public IButton (String text)
	{
		super(text);
	}

	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text The text of the button.
	 * @param icon The Icon image to display on the button.
	 */
	public IButton (String text, Icon icon)
	{
		super(text, icon);
	}

	/**
	 * Set the button text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	public void setText (String textKey)
	{
		this.textKey = textKey;
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}

	/**
	 * Set the tool tip text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
	public void setToolTipText (String textKey)
	{
		this.toolTipTextKey = textKey;
		super.setToolTipText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}

	/**
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText ()
	{
		setText (textKey);
		setToolTipText (toolTipTextKey);
	}
}