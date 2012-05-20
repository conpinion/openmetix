/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.key;


import de.iritgo.openmetix.app.key.UserCheckKeyLicense;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.thread.Threadable;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.command.CommandTools;
import org.apache.commons.dbutils.ResultSetHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: KeyCheckDBUpdate.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class KeyCheckDBUpdate
	extends Threadable
	implements ResultSetHandler
{
	private List sensors;

	/**
	 * Output def.:
	 * Result, Number
	 */
	private double receiver;
	private String licensingName;
	private String key;
	private long instrumentUniqueId;
	private boolean result;
	private int entry;
	private int number;
	private boolean checkStart;

	public KeyCheckDBUpdate (
		long instrumentUniqueId, double receiver, String licensingName, String key, boolean start)
	{
		this.receiver = receiver;
		this.instrumentUniqueId = instrumentUniqueId;
		this.licensingName = licensingName;
		this.key = key;
		this.checkStart = start;
	}

	public void run ()
	{
		setState (Threadable.CLOSING);

		UserCheckKeyLicense licenseCom = new UserCheckKeyLicense();

		number = number = licenseCom.giveNumberOfLicense (this.licensingName, this.key);

		if (licenseCom.getCheck ())
		{
			Properties props = new Properties();
			Object[] params = new Object[1];

			params[0] = new Integer(1);

			String update;

			update =
				"UPDATE Licensing SET kc_name = '" + licensingName + "', kc_key = '" + key +
				"', kc_entry ='1' WHERE kc_id = '1'";

			props = new Properties();
			props.setProperty ("update", update);
			props.put ("handler", this);

			CommandTools.performSimple ("persist.Update", props);
		}

		ClientTransceiver tranciever = new ClientTransceiver(receiver);

		tranciever.addReceiver (receiver);

		KeyCheckResponse kcresp =
			new KeyCheckResponse(instrumentUniqueId, licenseCom.getCheck (), number, checkStart);

		kcresp.setTransceiver (tranciever);
		ActionTools.sendToClient (kcresp);
	}

	/**
	 * Handler method of the ResultSetHandler interface.
	 *
	 * This method actually iterates over the result set and transfers
	 * the measurement values.
	 *
	 * @param rs The query result set.
	 */
	public Object handle (ResultSet rs)
		throws SQLException
	{
		while (rs.next ())
		{
		}

		return null;
	}
}