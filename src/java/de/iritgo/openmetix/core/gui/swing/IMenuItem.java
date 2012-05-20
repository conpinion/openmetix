/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;


/**
 * IMenuItem is an extended JMenuItem that loads it's labels from the
 * application resources.
 *
 * @version $Id: IMenuItem.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IMenuItem
	extends JMenuItem
{
	/** The resource key of the item label. */
	private String textKey;

	/**
	 * Creates a menu item with no set text or icon.
	 */
	public IMenuItem ()
	{
		super();
	}

	/**
	 * Creates a menu item where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new menu item.
	 */
	public IMenuItem (Action action)
	{
		super(action);
	}

	/**
	 * Creates a menu item with an icon.
	 *
	 * @param icon The Icon image to display on the menu item.
	 */
	public IMenuItem (Icon icon)
	{
		super(icon);
	}

	/**
	 * Creates a menu item with text.
	 *
	 * @param text The text of the menu item.
	 */
	public IMenuItem (String text)
	{
		super(text);
	}

	/**
	 * Creates a menu item with initial text and an icon.
	 *
	 * @param text The text of the menu item.
	 * @param icon The Icon image to display on the menu item.
	 */
	public IMenuItem (String text, Icon icon)
	{
		super(text, icon);
	}

	/**
	 * Set the menu item text.
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
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText ()
	{
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));
	}
}