/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.command;


import de.iritgo.openmetix.client.gui.MetixClientGUI;
import de.iritgo.openmetix.client.manager.MetixClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import javax.swing.Icon;


/**
 * This command displays an icon on a the tab that belongs to the
 * specified desktop pane.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>desktopPaneId</td><td>String</td><td>The id of the desktop pane</td></tr>
 *   <tr><td>icon</td><td>Icon</td><td>The icon to display on the desktop pane's tab</td></tr>
 * </table>
 *
 * @version $Id: SetTabIcon.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class SetTabIcon
	extends Command
{
	/**
	 * Create a new command object.
	 */
	public SetTabIcon ()
	{
		super("SetTabIcon");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		MetixClientManager clientManager =
			(MetixClientManager) Engine.instance ().getManagerRegistry ().getManager ("client");
		MetixClientGUI clientGui = (MetixClientGUI) clientManager.getClientGUI ();

		clientGui.setDesktopPaneIcon (
			(String) properties.get ("desktopPaneId"), ((Icon) properties.get ("icon")));
	}
}