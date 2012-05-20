/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.reduction;


import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.datareduction.gui.DataReductionDisplay;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import java.io.IOException;
import java.util.Iterator;


/**
 * @version $Id: DataReductionResponse.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DataReductionResponse
	extends FrameworkAction
{
	private long instrumentUniqueId;
	private boolean result;

	public DataReductionResponse ()
	{
		setTypeId ("DataReductionResponse");
	}

	public DataReductionResponse (long instrumentUniqueId, boolean result)
	{
		this();
		this.instrumentUniqueId = instrumentUniqueId;
		this.result = result;
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
		result = stream.readBoolean ();
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
		stream.writeBoolean (result);
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		IDesktopManager desktopManager = Client.instance ().getClientGUI ().getDesktopManager ();

		for (Iterator i = desktopManager.getDisplayIterator (); i.hasNext ();)
		{
			try
			{
				GUIPane guiPane = ((IDisplay) i.next ()).getGUIPane ();

				if (guiPane.getTypeId ().equals ("DataReductionDisplay"))
				{
					DataReductionDisplay drd = (DataReductionDisplay) guiPane;

					drd.setResult (result);
				}
			}
			catch (ClassCastException noInstrumentDisplayIgnored)
			{
			}
		}
	}
}