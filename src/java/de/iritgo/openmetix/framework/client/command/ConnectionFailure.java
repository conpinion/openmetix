/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.Client;
import java.util.Properties;


/**
 * ConnectionFailure.
 *
 * @version $Id: ConnectionFailure.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ConnectionFailure
	extends Command
{
	private String textId;
	private AppContext appContext;

	public ConnectionFailure ()
	{
		super("connectionfailure");
		appContext = AppContext.instance ();
		textId = "common.connectionfailure";
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		IDesktopManager iDesktopManager = Client.instance ().getClientGUI ().getDesktopManager ();

		iDesktopManager.closeAllDisplays ();

		ShowDialog showDialog = new ShowDialog("common.connectionfailure");

		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (showDialog);
	}

	public boolean canPerform ()
	{
		return ! appContext.isConnectedWithServer ();
	}
}