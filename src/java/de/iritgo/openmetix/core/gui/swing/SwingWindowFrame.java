/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.IWindow;
import de.iritgo.openmetix.core.gui.IWindowFrame;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;


/**
 * SwingWindowFrame
 *
 * @version $Id: SwingWindowFrame.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SwingWindowFrame
	extends JInternalFrame
	implements InternalFrameListener, IWindowFrame
{
	/** The desktop pane on which the window is displayed. */
	private JDesktopPane desktopPane;

	/** Our window. */
	private IWindow window;

	/**
	 * Create a new SwingWindowFrame.
	 *
	 * @param window The IWindow to which this frame belongs.
	 * @param titleKey The window title specified as a resource key.
	 * @param resizable True if the window should be resizable.
	 * @param closable True if the window should be closable.
	 * @param maximizable True if the window should be maximizable.
	 * @param iconifiable True if the window should be iconifiable.
	 */
	public SwingWindowFrame (
		IWindow window, String titleKey, boolean resizable, boolean closable, boolean maximizable,
		boolean iconifiable)
	{
		super(
			Engine.instance ().getResourceService ().getStringWithoutException (titleKey), resizable,
			closable, maximizable, iconifiable);

		addInternalFrameListener (this);

		this.window = window;
		this.desktopPane =
			((SwingDesktopManager) window.getDesktopManager ()).getDesktopPane (
				window.getDesktopId ());

		getContentPanel ().setLayout (new GridBagLayout());

		setBounds (getUserBounds ());

		setGlassPane (new IGlassPane());

		desktopPane.add (this);
	}

	/**
	 * Return the ContentPabel.
	 *
	 * @return Return the ContentPanel
	 */
	public JPanel getContentPanel ()
	{
		return (JPanel) getContentPane ();
	}

	/**
	 * Invoked when the user attempts to close the window from the window's system menu.
	 *
	 * @param e The window event.
	 */
	public void internalFrameClosing (InternalFrameEvent e)
	{
		window.systemClose ();
	}

	/**
	 * Invoked when a window has been closed as the result of calling dispose on the window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameClosed (InternalFrameEvent e)
	{
	}

	/**
	 * Invoked the first time a window is made visible.
	 *
	 * @param e The window event.
	 */
	public void internalFrameOpened (InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when a window is changed from a normal to a minimized state.
	 *
	 * @param e The window event.
	 */
	public void internalFrameIconified (InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when a window is changed from a minimized to a normal state.
	 *
	 * @param e The window event.
	 */
	public void internalFrameDeiconified (InternalFrameEvent e)
	{
	}

	/**
	 * Invoked when the Window is set to be the active Window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameActivated (InternalFrameEvent e)
	{
		((IGlassPane) getGlassPane ()).setVisible (true);

		saveBounds ();

		if (window.getDesktopManager () != null)
		{
			window.getDesktopManager ().setActiveDisplay (window);
		}
	}

	/**
	 * Invoked when a Window is no longer the active Window.
	 *
	 * @param e The window event.
	 */
	public void internalFrameDeactivated (InternalFrameEvent e)
	{
		saveBounds ();
	}

	/**
	 * Controlls the Minimize and Close functions
	 */
	public void actionPerformed (ActionEvent event)
	{
	}

	/**
	 * Generates a constraints object.
	 */
	protected GridBagConstraints getConstraints (
		int x, int y, int width, int height, int anchor, int fill, int wx, int wy)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		return gbc;
	}

	/**
	 * Return the bounds of this IWindow
	 *
	 * @return Return the ContentPanel
	 */
	protected Rectangle getUserBounds ()
	{
		Engine engine = Engine.instance ();
		long iObjectUniqueId =
			window.getDataObject () != null ? window.getDataObject ().getUniqueId () : 0;

		int x =
			engine.getSystemProperties ().getInt (
				window.getTypeId () + iObjectUniqueId + ".x", 300);
		int y =
			engine.getSystemProperties ().getInt (
				window.getTypeId () + iObjectUniqueId + ".y", 300);
		int xw =
			engine.getSystemProperties ().getInt (
				window.getTypeId () + iObjectUniqueId + ".xw", 300);
		int yw =
			engine.getSystemProperties ().getInt (
				window.getTypeId () + iObjectUniqueId + ".yw", 300);

		return new Rectangle(x, y, xw, yw);
	}

	/**
	 * Check wether the user has maximized the window.
	 *
	 * @return True for a maxmimized window.
	 */
	public boolean isUserMaximized ()
	{
		Engine engine = Engine.instance ();
		long iObjectUniqueId =
			window.getDataObject () != null ? window.getDataObject ().getUniqueId () : 0;

		return engine.getSystemProperties ().getInt (
			window.getTypeId () + iObjectUniqueId + ".maximized", 0) != 0;
	}

	/**
	 * Get the window bounds.
	 *
	 * @return The window bounds.
	 */
	public Rectangle getWindowBounds ()
	{
		return getBounds ();
	}

	/**
	 * Save the window bounds into the system properties.
	 */
	private void saveBounds ()
	{
		if (getContentPanel ().isVisible ())
		{
			Engine engine = Engine.instance ();

			long iObjectUniqueId =
				window.getDataObject () != null ? window.getDataObject ().getUniqueId () : 0;

			engine.getSystemProperties ().put (
				window.getTypeId () + iObjectUniqueId + ".x", getBounds ().x);
			engine.getSystemProperties ().put (
				window.getTypeId () + iObjectUniqueId + ".y", getBounds ().y);
			engine.getSystemProperties ().put (
				window.getTypeId () + iObjectUniqueId + ".xw", getWidth ());
			engine.getSystemProperties ().put (
				window.getTypeId () + iObjectUniqueId + ".yw", getHeight ());
			engine.getSystemProperties ().put (
				window.getTypeId () + iObjectUniqueId + ".maximized", isMaximum () ? 1 : 0);
		}
	}

	/**
	 * Close the window frame.
	 */
	public void close ()
	{
		saveBounds ();
		setVisible (false);
		dispose ();
		desktopPane.remove (this);
	}

	/**
	 * Close the window frame.
	 */
	public void systemClose ()
	{
		saveBounds ();
		setVisible (false);
		dispose ();
		desktopPane.remove (this);
	}

	/**
	 * Set the window title. This title will be displayed on the
	 * window frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title)
	{
		super.setTitle (title);
	}

	/**
	 * Set the window icon. This icon will be displayed on the
	 * window frame's title bar.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon (Icon icon)
	{
		setFrameIcon (icon);
	}

	/**
	 * Get the frame's icon.
	 *
	 * @return The frame's icon.
	 */
	public Icon getIcon ()
	{
		return getFrameIcon ();
	}

	/**
	 * Show the window frame
	 */
	public void showWindow ()
	{
		try
		{
			setSelected (true);
			setVisible (true);
		}
		catch (java.beans.PropertyVetoException e)
		{
		}
	}

	/**
	 * Set the maxmized state of the window frame.
	 */
	public void setMaximized (boolean maximized)
	{
		try
		{
			setMaximum (maximized);
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Enable/disable the window frame.
	 *
	 * @param enabled If true the window frame is enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		((IGlassPane) getGlassPane ()).setEnabled (enabled);
	}
}