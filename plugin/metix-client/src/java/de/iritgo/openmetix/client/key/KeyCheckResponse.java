/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.key;


import de.iritgo.openmetix.app.key.UserCheckKeyLicense;
import de.iritgo.openmetix.client.gui.KeyCheckResultDisplay;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import java.io.IOException;
import java.util.Iterator;


/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: KeyCheckResponse.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class KeyCheckResponse
	extends FrameworkAction
{
	/** Instrument id. */
	private long instrumentUniqueId;
	private boolean result;
	private boolean entry;
	private int number;
	private boolean checkStart;

	/**
	 * Create a new MinMidMaxQueryResponse
	 */
	public KeyCheckResponse ()
	{
		setTypeId ("KeyCheckResponse");
	}

	/**
	 * Create a new KeyCheckResponse
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param result Result of the key checking.
	 * @param number Number of license.
	 */
	public KeyCheckResponse (
		long instrumentUniqueId, boolean entry, int number, boolean checkStart)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.entry = entry;
		this.number = number;
		this.checkStart = checkStart;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (FrameworkInputStream stream)
		throws IOException
	{
		instrumentUniqueId = stream.readLong ();
		entry = stream.readBoolean ();
		number = stream.readInt ();
		checkStart = stream.readBoolean ();
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeLong (instrumentUniqueId);
		stream.writeBoolean (entry);
		stream.writeInt (number);
		stream.writeBoolean (checkStart);
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		IDesktopManager desktopManager = Client.instance ().getClientGUI ().getDesktopManager ();

		UserCheckKeyLicense uckl = new UserCheckKeyLicense();

		uckl.setLicense (number);

		for (Iterator i = desktopManager.getDisplayIterator (); i.hasNext ();)
		{
			try
			{
				GUIPane guiPane = ((IDisplay) i.next ()).getGUIPane ();

				if (guiPane.getTypeId ().equals ("KeyCheckResultDisplay"))
				{
					KeyCheckResultDisplay kcrd = (KeyCheckResultDisplay) guiPane;

					kcrd.viewKeyEntry (entry, number, this.checkStart);
				}
			}
			catch (ClassCastException noInstrumentDisplayIgnored)
			{
			}
		}
	}
}