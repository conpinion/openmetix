/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import javax.swing.JToolBar;
import java.awt.Component;


/**
 * IToolBar is an extended JToolBar that loads it's labels from the
 * application resources.
 *
 * @version $Id: IToolBar.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IToolBar
	extends JToolBar
{
	/**
	 * Create new tool bar.
	 */
	public IToolBar ()
	{
	}

	/**
	 * Reload the labels of all menu items in this tool bar.
	 */
	public void reloadText ()
	{
		for (int i = 0; i < getComponentCount (); ++i)
		{
			Component component = getComponentAtIndex (i);

			if (component instanceof IButton)
			{
				((IButton) component).reloadText ();
			}
		}
	}
}