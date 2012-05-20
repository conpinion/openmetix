/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.iobject.IObjectProxyRegistry;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import javax.swing.Icon;
import java.awt.Rectangle;
import java.util.Properties;


/**
 * IDialogs display IObjects in a dialog.
 *
 * @version $Id: IDialog.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IDialog
	extends BaseObject
	implements IDisplay
{
	/** The dialog properties. */
	private Properties properties;

	/** The gui pane. */
	private GUIPane guiPane;

	/** Our dialog frame. */
	private IDialogFrame dialogFrame;

	/** Our desktop manager. */
	private IDesktopManager desktopManager;

	/**
	 * Create a new IDialog.
	 */
	public IDialog ()
	{
		this("IWindow");
	}

	/**
	 * Create a new IDialog.
	 *
	 * @param dialogId The dialog id.
	 */
	public IDialog (String dialogId)
	{
		super(dialogId);
		properties = new Properties();
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 */
	public void initGUI (String guiPaneId)
	{
		initGUI (guiPaneId, null, null);
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 * @param sessionContext The session context.
	 */
	public void initGUI (String guiPaneId, SessionContext sessionContext)
	{
		initGUI (guiPaneId, null, sessionContext);
	}

	/**
	 * Initialize the dialog's gui.
	 *
	 * @param guiPaneId The id of the gui pane to be displayed in this dialog.
	 * @param object The data object to be displayed in this dialog.
	 * @param sessionContext The session context.
	 */
	public void initGUI (String guiPaneId, IObject object, SessionContext sessionContext)
	{
		dialogFrame = Engine.instance ().getGUIFactory ().createDialogFrame (this, getTypeId ());

		guiPane = (GUIPane) GUIPaneRegistry.instance ().create (guiPaneId);
		guiPane.setObject (object);
		guiPane.setSessionContext (sessionContext);
		guiPane.setIDisplay (this);

		if (properties.get ("bounds") != null)
		{
			dialogFrame.setBounds ((Rectangle) properties.get ("bounds"));
		}

		if (properties.get ("title") != null)
		{
			dialogFrame.setTitle ((String) properties.get ("title"));
		}

		guiPane.initGUI ();

		if (object != null)
		{
			guiPane.setObject (object);
			guiPane.registerProxyEventListener ();

			IObjectProxyRegistry proxyRegistry = Engine.instance ().getProxyRegistry ();
			IObjectProxy prototypeProxy =
				(IObjectProxy) proxyRegistry.getProxy (object.getUniqueId ());

			IObject prototypeable = prototypeProxy.getRealObject ();

			guiPane.setObject (prototypeable);

			if (prototypeable != null)
			{
				loadFromObject ();
			}
		}
	}

	/**
	 *  Get the GUIPane of this dialog.
	 *
	 * @return The gui pane.
	 */
	public GUIPane getGUIPane ()
	{
		return guiPane;
	}

	/**
	 * Load the object attributes into the gui.
	 */
	public void loadFromObject ()
	{
		guiPane.loadFromObject ();
	}

	/**
	 * Store the gui values to the object's attributes.
	 */
	public void storeToObject ()
	{
		guiPane.storeToObject ();
	}

	/**
	 * Close the dialog.
	 */
	public void close ()
	{
		if (guiPane != null)
		{
			guiPane.close ();
		}

		if (dialogFrame != null)
		{
			dialogFrame.close ();
		}

		desktopManager.removeDisplay (this);
	}

	/**
	 * Called from swing, the dialog will closed.
	 */
	public void systemClose ()
	{
		if (guiPane != null)
		{
			guiPane.systemClose ();
		}

		if (dialogFrame != null)
		{
			dialogFrame.systemClose ();
		}

		desktopManager.removeDisplay (this);
	}

	/**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
	public void setDesktopManager (IDesktopManager desktopManager)
	{
		this.desktopManager = desktopManager;
	}

	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager ()
	{
		return desktopManager;
	}

	/**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
	public String getDesktopId ()
	{
		return null;
	}

	/**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
	public void setDesktopId (String desktopId)
	{
	}

	/**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
	public IObject getDataObject ()
	{
		return guiPane.getObject ();
	}

	/**
	 * Set the dialog title. This title will be displayed on the
	 * dialog frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title)
	{
		dialogFrame.setTitle (title);
	}

	/**
	 * Get the dialog title.
	 *
	 * @return The gui pane title.
	 */
	public String getTitle ()
	{
		return dialogFrame.getTitle ();
	}

	/**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
	public void putProperty (String key, Object value)
	{
		properties.put (key, value);
	}

	/**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
	public Object getProperty (String key)
	{
		return properties.get (key);
	}

	/**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
	public void setProperties (Properties properties)
	{
		if (properties != null)
		{
			this.properties = properties;
		}
	}

	/**
	 * Get the display properties.
	 *
	 * @return The display properties.
	 */
	public Properties getProperties ()
	{
		return properties;
	}

	/**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
	public void removeProperty (String key)
	{
		properties.remove (key);
	}

	/**
	 * Set the window icon. This icon will be displayed on the
	 * window frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setIcon (Icon icon)
	{
		dialogFrame.setIcon (icon);
	}

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon ()
	{
		return dialogFrame.getIcon ();
	}

	/**
	 * Get the dialog frame of this IDialog
	 *
	 * @return The dialog frame.
	 */
	public IDialogFrame getDialogFrame ()
	{
		return dialogFrame;
	}

	/**
	 * Show the Dialog.
	 */
	public void show ()
	{
		dialogFrame.showDialog ();
	}

	/**
	 * Enable/disable the dialog.
	 *
	 * @param enabled If true the dialog is enabled.
	 */
	public void setEnabled (boolean enabled)
	{
		dialogFrame.setEnabled (enabled);
	}
}