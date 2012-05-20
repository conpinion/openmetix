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


/**
 * This command increments the current status bar progress value.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: StatusProgressStep.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class StatusProgressStep
	extends Command
{
	/**
	 * Create a new command object.
	 */
	public StatusProgressStep ()
	{
		super("StatusProgressStep");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		MetixClientManager clientManager =
			(MetixClientManager) Engine.instance ().getManagerRegistry ().getManager ("client");
		MetixClientGUI clientGui = (MetixClientGUI) clientManager.getClientGUI ();

		clientGui.setStatusProgressStep ();
	}
}