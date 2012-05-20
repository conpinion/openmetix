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
 * @version $Id: KeyCheckDBRequest.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class KeyCheckDBRequest
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

	public KeyCheckDBRequest (long instrumentUniqueId, double receiver, boolean checkStart)
	{
		this.receiver = receiver;
		this.instrumentUniqueId = instrumentUniqueId;
		this.checkStart = checkStart;
	}

	public void run ()
	{
		setState (Threadable.CLOSING);

		Properties props = new Properties();
		Object[] params = new Object[1];

		params[0] = new Integer(1);

		String select;

		select = "SELECT kc_name, kc_key, kc_entry FROM Licensing where kc_id=?";

		props = new Properties();
		props.setProperty ("select", select);
		props.put ("handler", this);
		props.put ("params", params);

		CommandTools.performSimple ("persist.Select", props);

		ClientTransceiver tranciever = new ClientTransceiver(receiver);

		tranciever.addReceiver (receiver);

		boolean entryValue;

		if (entry == 0)
		{
			entryValue = false;
		}
		else
		{
			entryValue = true;
		}

		UserCheckKeyLicense licenseCom = new UserCheckKeyLicense();

		number = licenseCom.giveNumberOfLicense (licensingName, key);

		KeyCheckResponse kcresp =
			new KeyCheckResponse(instrumentUniqueId, entryValue, number, checkStart);

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
			licensingName = rs.getString ("kc_name");
			key = rs.getString ("kc_key");
			entry = rs.getInt ("kc_entry");
		}

		return null;
	}
}