/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.command;


import de.iritgo.openmetix.app.instrument.InstrumentDisplay;
import de.iritgo.openmetix.client.gui.MetixClientGUI;
import de.iritgo.openmetix.client.manager.MetixClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.sessioncontext.LongContext;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.framework.client.command.ShowDialog;
import de.iritgo.openmetix.framework.command.CommandTools;


/**
 * This command opens the configuration dialog of the currently selected instrument.
 * specified desktop pane.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 *
 * @version $Id: EditInstrument.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class EditInstrument
	extends Command
{
	/**
	 * Create a new command object.
	 */
	public EditInstrument ()
	{
		super("EditInstrument");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		MetixClientManager clientManager =
			(MetixClientManager) Engine.instance ().getManagerRegistry ().getManager ("client");
		MetixClientGUI clientGui = (MetixClientGUI) clientManager.getClientGUI ();

		IDisplay display = clientGui.getDesktopManager ().getActiveDisplay ();

		if (
			display != null && display.getGUIPane () instanceof InstrumentDisplay &&
			((InstrumentDisplay) display.getGUIPane ()).isEditable ())
		{
			SessionContext sessionContext = new SessionContext("instrument");

			sessionContext.add ("edit", new LongContext(1));

			CommandTools.performAsync (
				new ShowDialog(
					display.getGUIPane ().getSampleObject ().getTypeId () + "Configurator",
					display.getDataObject (), sessionContext));
		}
	}
}