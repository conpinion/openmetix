/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.gui.IDialog;
import de.iritgo.openmetix.core.gui.IDialogFrame;
import de.iritgo.openmetix.core.gui.IGUIFactory;
import de.iritgo.openmetix.core.gui.IWindow;
import de.iritgo.openmetix.core.gui.IWindowFrame;
import org.swixml.SwingTagLibrary;


/**
 * This is a GUI factory that creates swing components.
 *
 * @version $Id: SwingGUIFactory.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SwingGUIFactory
	implements IGUIFactory
{
	/**
	 * Create a new SwingGUIFactory.
	 */
	public SwingGUIFactory ()
	{
		SwingTagLibrary.getInstance ().registerTag ("ilabel", ILabel.class);
		SwingTagLibrary.getInstance ().registerTag ("ifieldlabel", IFieldLabel.class);
		SwingTagLibrary.getInstance ().registerTag ("ibutton", IButton.class);
		SwingTagLibrary.getInstance ().registerTag ("icheckbox", ICheckBox.class);
		SwingTagLibrary.getInstance ().registerTag ("imenubar", IMenuBar.class);
		SwingTagLibrary.getInstance ().registerTag ("imenu", IMenu.class);
		SwingTagLibrary.getInstance ().registerTag ("imenuitem", IMenuItem.class);
		SwingTagLibrary.getInstance ().registerTag ("itoolbar", IToolBar.class);
		SwingTagLibrary.getInstance ().registerTag ("iradiobutton", IRadioButton.class);
		SwingTagLibrary.getInstance ().registerTag ("ititledpanel", ITitledPanel.class);
		SwingTagLibrary.getInstance ().registerTag ("iclocktextfield", IClockTextField.class);
		SwingTagLibrary.getInstance ().registerTag ("itextfield", ITextField.class);
		SwingTagLibrary.getInstance ().registerTag (
			"iformattedtextfield", IFormattedTextField.class);
	}

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
		boolean iconifiable)
	{
		return new SwingWindowFrame(
			window, titleKey, resizable, closable, maximizable, iconifiable);
	}

	/**
	 * Create a dialog frame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 * @return The new dialog frame.
	 */
	public IDialogFrame createDialogFrame (IDialog dialog, String titleKey)
	{
		return new SwingDialogFrame(dialog, titleKey);
	}
}