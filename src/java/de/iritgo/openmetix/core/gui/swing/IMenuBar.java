/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import javax.swing.JMenuBar;


/**
 * IMenuBar is an extended JMenuBar that loads it's labels from the
 * application resources.
 *
 * @version $Id: IMenuBar.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IMenuBar
	extends JMenuBar
{
	/**
	 * Create new menu bar.
	 */
	public IMenuBar ()
	{
	}

	/**
	 * Reload the labels of all menu items in this menu bar.
	 */
	public void reloadText ()
	{
		for (int i = 0; i < getMenuCount (); ++i)
		{
			((IMenu) getMenu (i)).reloadText ();
		}
	}
}