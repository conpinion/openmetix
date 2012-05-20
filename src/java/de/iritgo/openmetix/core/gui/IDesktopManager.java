/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A desktop manager handles virtual desktops.
 *
 * @version $Id: IDesktopManager.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public abstract class IDesktopManager
	extends BaseObject
{
	/** The desktop frame. */
	protected IDesktopFrame desktopFrame;

	/** All displays on all desktops. */
	protected List displays;

	/** Mapping from display ids to displays. */
	protected Map displayById;

	/**
	 * Used to temporarily store display information.
	 */
	public class DisplayItem
	{
		/** Gui pane id. */
		public String guiPaneId;

		/** Id of the desktop of this display. */
		public String desktopId;

		/** Id of the IObject displayed in the gui pane. */
		public long iObjectUniqueId;

		/**
		 * Create a new DisplayItem.
		 *
		 * @param guiPaneId Gui pane id.
		 * @param desktopId Id of the desktop of this display.
		 * @param iObject IObject displayed in the gui pane.
		 */
		public DisplayItem (String guiPaneId, String desktopId, IObject iObject)
		{
			this.guiPaneId = guiPaneId;
			this.desktopId = desktopId;

			if (iObject != null)
			{
				this.iObjectUniqueId = iObject.getUniqueId ();
			}
		}
	}

	/** Used to temporarily store displays. */
	private List savedDisplays;

	/** All active displays. */
	private IDisplay activeDisplay;

	/**
	 * Create a new IDesktopManager.
	 */
	public IDesktopManager ()
	{
		displays = new LinkedList();
		displayById = new HashMap();
	}

	/**
	 * Add an IDisplay to the IDesktopManager.
	 *
	 * @param display The IDisplay to add.
	 */
	public void addDisplay (IDisplay display)
	{
		displays.add (display);
		displayById.put (display.getTypeId (), display);
		addImpl (display);
	}

	/**
	 * Get a Iterator over all Displays
	 *
	 * @return Iterator Iterator for all Displays
	 */
	public Iterator getDisplayIterator ()
	{
		return displays.iterator ();
	}

	/**
	 * Remove a display.
	 *
	 * @param display The display to remove.
	 */
	public void removeDisplay (IDisplay display)
	{
		if (display != null)
		{
			displays.remove (display);
			displayById.remove (display.getTypeId ());
		}
	}

	/**
	 * Get a display by id.
	 *
	 * @param guiPaneId The id of the display to retrieve.
	 * @return The display or null if no display was found.
	 */
	public IDisplay getDisplay (String guiPaneId)
	{
		return (IDisplay) displayById.get (guiPaneId);
	}

	/**
	 * Get a display by id.
	 *
	 * @param guiPaneId The id of the display to retrieve.
	 * @return The display or null if no display was found.
	 */
	public IDisplay waitForDisplay (String guiPaneId)
	{
		IDisplay display = null;

		while (display == null)
		{
			display =
				(IDisplay) Client.instance ().getClientGUI ().getDesktopManager ().getDisplay (
					guiPaneId);

			if (display == null)
			{
				try
				{
					Thread.sleep (100);
				}
				catch (InterruptedException x)
				{
				}
			}
		}

		return display;
	}

	/**
	 * Close all displays.
	 */
	public void closeAllDisplays ()
	{
		List tmpDisplays = new LinkedList(displays);

		for (Iterator i = tmpDisplays.iterator (); i.hasNext ();)
		{
			((IDisplay) i.next ()).close ();
		}
	}

	/**
	 * Temporarily save all currently visible displays.
	 */
	public void saveVisibleDisplays ()
	{
		savedDisplays = new LinkedList();

		for (Iterator i = displays.iterator (); i.hasNext ();)
		{
			IDisplay display = (IDisplay) i.next ();

			if (display.getDesktopId () == null)
			{
				continue;
			}

			IObject businessObject = display.getGUIPane ().getObject ();

			savedDisplays.add (
				new DisplayItem(
					display.getGUIPane ().getTypeId (), display.getDesktopId (), businessObject));
		}
	}

	/**
	 * Redisplay all saved displays.
	 */
	public void showSavedDisplays ()
	{
		ShowWindow show = null;

		for (Iterator i = savedDisplays.iterator (); i.hasNext ();)
		{
			DisplayItem entry = (DisplayItem) i.next ();

			GUIPane guiPane = GUIPaneRegistry.instance ().create (entry.guiPaneId);

			if ((guiPane != null) && (entry.iObjectUniqueId != 0))
			{
				show = new ShowWindow(entry.guiPaneId, entry.iObjectUniqueId, entry.desktopId);
			}
			else
			{
				show = new ShowWindow(entry.guiPaneId, null, null, entry.desktopId);
			}

			IritgoEngine.instance ().getAsyncCommandProcessor ().perform (show);
		}
	}

	/**
	 * Set the desktop frame.
	 *
	 * @param desktopFrame The desktop frame.
	 */
	public void setDesktopFrame (IDesktopFrame desktopFrame)
	{
		this.desktopFrame = desktopFrame;
	}

	/**
	 * Get the desktop frame.
	 *
	 * @return The desktop frame.
	 */
	public IDesktopFrame getDesktopFrame ()
	{
		return desktopFrame;
	}

	/**
	 * Get the currently active display.
	 *
	 * @return The active display or null if no display is currently shown.
	 */
	public IDisplay getActiveDisplay ()
	{
		return activeDisplay;
	}

	/**
	 * Set the currently active display.
	 *
	 * @param activeDisplay The active display.
	 */
	public void setActiveDisplay (IDisplay activeDisplay)
	{
		this.activeDisplay = activeDisplay;
	}

	/**
	 * Add an IDisplay.
	 *
	 * @param display The IDisplay to add.
	 */
	public abstract void addImpl (IDisplay display);
}