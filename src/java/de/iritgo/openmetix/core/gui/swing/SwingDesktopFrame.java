/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.gui.IDesktopFrame;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Swing implementation of the IDesktopFrame.
 *
 * @version $Id: SwingDesktopFrame.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class SwingDesktopFrame
	extends JFrame
	implements IDesktopFrame
{
	/** Called when the frame is to be closed. */
	private ActionListener windowClosingEvent;

	/**
	 * Create a new desktop frame.
	 */
	public SwingDesktopFrame ()
	{
		this("Iritgo");
	}

	/**
	 * Create a new desktop frame.
	 *
	 * @param title The desktop frame's title.
	 */
	public SwingDesktopFrame (String title)
	{
		super(title);
		getGlassPane ().addMouseListener (new MouseAdapter()
			{
			});
		getGlassPane ().addMouseMotionListener (new MouseMotionAdapter()
			{
			});
		getGlassPane ().addKeyListener (new KeyAdapter()
			{
			});
		getGlassPane ().setVisible (false);

		setSize (getToolkit ().getScreenSize ());
	}

	/**
	 * Get the swing frame.
	 *
	 * @return The swing frame.
	 */
	public JFrame getJFrame ()
	{
		return this;
	}

	/**
	 * Set the fullscreen mode.
	 *
	 * @param fullScreen If true, the desktop frame is displayed in
	 *   fullscreen mode.
	 */
	public void setFullScreen (boolean fullScreen)
	{
		setUndecorated (fullScreen);
	}

	/**
	 * Check wether the frame is currently displayed in fullscreen mode
	 * or not.
	 *
	 * @return True if the desktop is displayed in fullscreen mode.
	 */
	public boolean isFullScreen ()
	{
		return isUndecorated ();
	}

	/**
	 * Check wether the desktop frame implementation supports fullscreen
	 * mode.
	 *
	 * @return True if the desktop frame supports the fullscreen mode.
	 */
	public boolean canFullScreen ()
	{
		return true;
	}

	/**
	 * Initialize the desktop frame.
	 */
	public void init ()
	{
		addWindowListener (
			new WindowAdapter()
			{
				public void windowActivated (WindowEvent e)
				{
				}

				public void windowClosed (WindowEvent e)
				{
				}

				public void windowClosing (WindowEvent e)
				{
					if (windowClosingEvent != null)
					{
						windowClosingEvent.actionPerformed (
							new ActionEvent(this, 0, "windowClosing"));
					}
				}

				public void windowDeactivated (WindowEvent e)
				{
				}

				public void windowDeiconified (WindowEvent e)
				{
				}

				public void windowIconified (WindowEvent e)
				{
				}

				public void windowOpened (WindowEvent e)
				{
				}
			});

		requestFocus ();
	}

	/**
	 * Show or hide the desktop frame.
	 *
	 * @param visible If true the desktop frame is displayed.
	 */
	public void setVisible (boolean visible)
	{
		super.setVisible (visible);

		if (visible)
		{
			setExtendedState (JFrame.MAXIMIZED_BOTH);
		}
	}

	/**
	 * Show the desktop frame.
	 */
	public void setVisible ()
	{
		setVisible (true);
	}

	/**
	 * Get the screen size of the desktop frame.
	 *
	 * @return The screen size.
	 */
	public Dimension getScreenSize ()
	{
		return getToolkit ().getScreenSize ();
	}

	/**
	 * Add a listener that will be called when the desktop manager is
	 * about to be closed.
	 *
	 * @param listener The action listener.
	 */
	public void addCloseListener (ActionListener listener)
	{
		this.windowClosingEvent = listener;
	}

	/**
	 * Close the desktop manager and free all resources.
	 */
	public void close ()
	{
		setVisible (false);
		dispose ();
	}

	/**
	 * Enable or disable the desktop frame.
	 *
	 * @param enabled If true the desktop is enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		getGlassPane ().setVisible (! enabled);
	}
}