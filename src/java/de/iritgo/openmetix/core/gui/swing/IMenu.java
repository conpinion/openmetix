/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import javax.swing.Action;
import javax.swing.JMenu;
import java.awt.Component;


/**
 * IMenu is an extended JMenu that loads it's labels from the
 * application resources.
 *
 * @version $Id: IMenu.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IMenu
	extends JMenu
{
	/** The resource key of the menu label. */
	private String textKey;

	/**
	 * Creates a menu item with no set text or icon.
	 */
	public IMenu ()
	{
		super();
	}

	/**
	 * Creates a menu item where properties are taken from the Action supplied.
	 *
	 * @param action The Action used to specify the new menu item.
	 */
	public IMenu (Action action)
	{
		super(action);
	}

	/**
	 * Creates a menu item with text.
	 *
	 * @param text The text of the menu item.
	 */
	public IMenu (String text)
	{
		super(text);
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
	 * Reload the labels of all menu items in this menu.
	 */
	public void reloadText ()
	{
		super.setText (
			Engine.instance ().getResourceService ().getStringWithoutException (textKey));

		for (int i = 0; i < getMenuComponentCount (); ++i)
		{
			Component item = getMenuComponent (i);

			if (item.getClass ().equals (IMenu.class))
			{
				((IMenu) item).reloadText ();
			}

			if (item.getClass ().equals (IMenuItem.class))
			{
				((IMenuItem) item).reloadText ();
			}
		}
	}
}