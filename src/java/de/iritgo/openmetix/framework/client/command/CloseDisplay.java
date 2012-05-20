/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.framework.client.Client;
import java.util.Properties;


/**
 * CloseDisplay.
 *
 * @version $Id: CloseDisplay.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class CloseDisplay
	extends Command
{
	private String windowId;

	public CloseDisplay (String windowId)
	{
		this.windowId = windowId;
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		IDisplay display =
			Client.instance ().getClientGUI ().getDesktopManager ().getDisplay (windowId);

		if (display != null)
		{
			display.close ();
			Client.instance ().getClientGUI ().getDesktopManager ().removeDisplay (display);
		}
	}
}