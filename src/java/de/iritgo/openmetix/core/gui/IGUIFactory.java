/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;



/**
 * GUI factories are used to create specific implementations of
 * abstract gui elements like windows and dialogs.
 *
 * @version $Id: IGUIFactory.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public interface IGUIFactory
{
	/**
	 * Create a new window frame.
	 *
	 * @param window The IWindow to which this frame belongs.
	 * @param titleKey The window title specified as a resource key.
	 * @param resizable True if the window should be resizable.
	 * @param closable True if the window should be closable.
	 * @param maximizable True if the window should be maximizable.
	 * @param iconifiable True if the window should be iconifiable.
	 */
	public IWindowFrame createWindowFrame (
		IWindow window, String titleKey, boolean resizable, boolean closable, boolean maximizable,
		boolean iconifiable);

	/**
	 * Create a dialog frame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 * @return The new dialog frame.
	 */
	public IDialogFrame createDialogFrame (IDialog dialog, String titleKey);
}