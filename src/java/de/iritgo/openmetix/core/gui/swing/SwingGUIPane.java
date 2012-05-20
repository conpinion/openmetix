/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.IDialog;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.gui.IWindow;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * This is a Swing implementation of the GUIPane.
 *
 * @version $Id: SwingGUIPane.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public abstract class SwingGUIPane
	extends GUIPane
{
	/** The content panel. */
	protected JPanel content;

	/**
	 * Create a new SwingGUIPane.
	 *
	 * @param guiPaneId The id of the new gui pane.
	 */
	public SwingGUIPane (String guiPaneId)
	{
		super(guiPaneId);
	}

	/**
	 * Set the content pane.
	 *
	 * @param content The new content pane.
	 */
	public void setContentPane (JPanel content)
	{
		this.content = content;
	}

	/**
	 * Implementation specific tasks for setting the display.
	 *
	 * @param display The new display.
	 */
	public void setIDisplayImpl (IDisplay display)
	{
		if (display instanceof IWindow)
		{
			setContentPane (
				((SwingWindowFrame) (((IWindow) display).getWindowFrame ())).getContentPanel ());
		}
		else if (display instanceof IDialog)
		{
			setContentPane (
				((SwingDialogFrame) (((IDialog) display).getDialogFrame ())).getContentPanel ());
		}
	}

	/**
	 * This method is called when the attributes of the gui pane's
	 * iobject are received.
	 */
	public void waitingForNewObjectFinished ()
	{
		try
		{
			SwingUtilities.invokeAndWait (
				new Runnable()
				{
					public void run ()
					{
						loadFromObject ();
					}
				});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (
		int x, int y, int width, int height, int fill, int wx, int wy, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param anchor The anchor.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (
		int x, int y, int width, int height, double wx, double wy, int fill, int anchor,
		Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Set the IDisplay icon.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon (Icon icon)
	{
		if (display instanceof IWindow)
		{
			((IWindow) display).setIcon (icon);
		}
		else if (display instanceof IDialog)
		{
			((IDialog) display).setIcon (icon);
		}
	}

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon ()
	{
		if (display instanceof IWindow)
		{
			return ((IWindow) display).getIcon ();
		}
		else if (display instanceof IDialog)
		{
			return ((IDialog) display).getIcon ();
		}

		return null;
	}

	/**
	 * Get the content panel.
	 *
	 * @return The content panel.
	 */
	public JPanel getPanel ()
	{
		return content;
	}
}