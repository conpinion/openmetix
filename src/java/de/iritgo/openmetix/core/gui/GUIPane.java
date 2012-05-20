/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxyEvent;
import de.iritgo.openmetix.core.iobject.IObjectProxyListener;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;


/**
 * GUIPanes display the attributes of IObjects.
 *
 * @version $Id: GUIPane.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public abstract class GUIPane
	extends BaseObject
	implements IObjectProxyListener
{
	/** The IObject that is displayed in this gui pane. */
	protected IObject iobject;

	/** The IDisplay in which the gui pane is rendered. */
	protected IDisplay display;

	/** The current session context. */
	protected SessionContext sessionContext;

	/**
	 * Create a new GUIPane.
	 *
	 * @param guiPaneId The id of the new gui pane.
	 */
	public GUIPane (String guiPaneId)
	{
		super(guiPaneId);
	}

	/**
	 * Set the IOject to display.
	 */
	public void setObject (IObject iobject)
	{
		this.iobject = iobject;
	}

	/**
	 * Get the IObject that is displayed in this gui pane.
	 *
	 * @return The IObject.
	 */
	public IObject getObject ()
	{
		return iobject;
	}

	/**
	 * Set the display.
	 *
	 * @param display The new display.
	 */
	public void setIDisplay (IDisplay display)
	{
		this.display = display;
		setIDisplayImpl (display);
	}

	/**
	 * Implementation specific tasks for setting the display.
	 *
	 * @param display The new display.
	 */
	public abstract void setIDisplayImpl (IDisplay display);

	/**
	 * Get the display.
	 *
	 * @return The display.
	 */
	public IDisplay getDisplay ()
	{
		return display;
	}

	/**
	 * Set the session context.
	 *
	 * @param sessionContext The new session context.
	 */
	public void setSessionContext (SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	/**
	 * Get the current session context.
	 *
	 * @return The session context.
	 */
	public SessionContext getSessionContext ()
	{
		return sessionContext;
	}

	/**
	 * Register-Proxy-Event-Listener.
	 */
	public void registerProxyEventListener ()
	{
		Engine.instance ().getProxyEventRegistry ().addEventListener (iobject, this);
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	public void initGUI ()
	{
	}

	/**
	 * This method is called if a proxy event occurred.
	 *
	 * @param event The proxy event.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		if (iobject == null)
		{
			return;
		}

		if (event.isWaitingForNewObject ())
		{
			waitingForNewObject ();
		}
		else
		{
			waitingForNewObjectFinished ();
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	public void loadFromObject ()
	{
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	public void storeToObject ()
	{
	}

	/**
	 * This method is called when the gui pane starts waiting
	 * for the attributes of it's iobject.
	 */
	public void waitingForNewObject ()
	{
	}

	/**
	 * This method is called when the attributes of the gui pane's
	 * iobject are received.
	 */
	public void waitingForNewObjectFinished ()
	{
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	public abstract GUIPane cloneGUIPane ();

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public abstract IObject getSampleObject ();

	/**
	 * Close the display.
	 */
	public void close ()
	{
		if (iobject != null)
		{
			Engine.instance ().getProxyEventRegistry ().removeEventListener (iobject, this);
		}
	}

	/**
	 * Close the display.
	 */
	public void systemClose ()
	{
		if (iobject != null)
		{
			Engine.instance ().getProxyEventRegistry ().removeEventListener (iobject, this);
		}
	}

	/**
	 * Set the gui pane title. This title will be displayed on the
	 * display frame's title bar, to which this gui pane belongs.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title)
	{
		display.setTitle (title);
	}

	/**
	 * Get the gui pane title.
	 *
	 * @return The gui pane title.
	 */
	public String getTitle ()
	{
		return display.getTitle ();
	}
}