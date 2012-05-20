/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.network.ConnectObserver;
import de.iritgo.openmetix.core.network.NetworkService;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.Client;
import org.apache.avalon.framework.configuration.Configuration;
import java.util.Properties;


/**
 * ConnectToServer.
 *
 * @version $Id: ConnectToServer.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ConnectToServer
	extends Command
{
	private String observerGuiPaneId;

	public ConnectToServer (String guiPaneId)
	{
		this.observerGuiPaneId = guiPaneId;
	}

	public ConnectToServer ()
	{
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		Engine engine = Engine.instance ();
		AppContext appContext = AppContext.instance ();

		ConnectObserver observer = null;

		if (observerGuiPaneId != null)
		{
			IDisplay display =
				Client.instance ().getClientGUI ().getDesktopManager ().waitForDisplay (
					observerGuiPaneId);

			observer = (ConnectObserver) display.getGUIPane ();
		}

		NetworkService networkService = Client.instance ().getNetworkService ();

		Configuration config = IritgoEngine.instance ().getConfiguration ();
		Configuration socketConfig = config.getChild ("network").getChild ("socket");

		int port = socketConfig.getAttributeAsInteger ("port", 3000);

		double channelNumber = networkService.connect (appContext.getServerIP (), port, observer);

		if (channelNumber < 0)
		{
			appContext.setConnectionState (false);

			return;
		}

		appContext.setConnectionState (true);
		appContext.setChannelNumber (channelNumber);

		return;
	}
}