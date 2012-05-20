/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.command;


import de.iritgo.openmetix.client.gui.MetixClientGUI;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.client.Client;


/**
 * This command enables or disables the administration functions.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>enabled</td><td>Boolean</td><td>If true the admin functions are enabled.</td></tr>
 * </table>
 *
 * @version $Id: EnableAdminFunctions.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class EnableAdminFunctions
	extends Command
{
	/**
	 * Create a new command object.
	 */
	public EnableAdminFunctions ()
	{
		super("EnableAdminFunctions");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		MetixClientGUI gui = (MetixClientGUI) Client.instance ().getClientGUI ();

		boolean enable = false;

		if (properties.get ("enabled") != null)
		{
			enable = ((Boolean) properties.get ("enabled")).booleanValue ();
		}

		gui.setAdminMenuVisible (enable);
	}
}