/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.datareduction.reduction;


import de.iritgo.openmetix.app.gagingstation.GagingSensor;
import de.iritgo.openmetix.app.gagingstation.GagingStation;
import de.iritgo.openmetix.app.gagingstation.manager.GagingStationManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.thread.Threadable;
import de.iritgo.openmetix.framework.action.ActionTools;
import java.util.Iterator;


/**
 * @version $Id: DataReduction.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DataReduction
	extends Threadable
{
	private double receiver;
	private long instrumentUniqueId;
	private String timeNumber = new String();
	private int timeIndex;
	private long stationID;
	private long sensorID;
	private boolean deleteAllParameters;

	public DataReduction (
		long instrumentUniqueId, double receiver, String timeNumber, int timeIndex, long stationID,
		long sensorID, boolean deleteAllParameters)
	{
		this.receiver = receiver;
		this.instrumentUniqueId = instrumentUniqueId;
		this.timeNumber = timeNumber;
		this.timeIndex = timeIndex;
		this.stationID = stationID;
		this.sensorID = sensorID;
		this.deleteAllParameters = deleteAllParameters;
	}

	public void run ()
	{
		setState (Threadable.CLOSING);

		int time = Integer.valueOf (timeNumber).intValue ();
		DataReductionValue drv = new DataReductionValue(time, timeIndex, deleteAllParameters);

		drv.setStartTime ();

		GagingStationManager gsm =
			(GagingStationManager) Engine.instance ().getManagerRegistry ().getManager (
				"GagingStationManager");

		if (deleteAllParameters)
		{
			for (Iterator i = gsm.stationIterator (); i.hasNext ();)
			{
				GagingStation gs = (GagingStation) i.next ();

				if (stationID == gs.getUniqueId ())
				{
					for (Iterator e = gs.sensorIterator (); e.hasNext ();)
					{
						GagingSensor sensor = (GagingSensor) e.next ();

						drv.setSationSensor (stationID, sensor.getUniqueId ());
						drv.setFirstTime ();

						while (drv.getMeasurementList ().size () != 0)
						{
							double average = drv.getAverageOfMeasurements ();

							if (average != -1111.11)
							{
								drv.deleteMeasurements ();
								drv.insertMeasurement (average);

								int jumper = 10;

								while (drv.getMeasurementList ().size () == 0 && jumper != 0)
								{
									drv.setNewStartTime ();
									jumper--;
								}
							}
						}
					}
				}
			}
		}
		else
		{
			drv.setSationSensor (stationID, sensorID);

			while (drv.getMeasurementList ().size () != 0)
			{
				double average = drv.getAverageOfMeasurements ();

				if (average != -1111.11)
				{
					drv.deleteMeasurements ();
					drv.insertMeasurement (average);

					int jumper = 10;

					while (drv.getMeasurementList ().size () == 0 && jumper != 0)
					{
						drv.setNewStartTime ();
						jumper--;
					}
				}
			}
		}

		ClientTransceiver tranciever = new ClientTransceiver(receiver);

		tranciever.addReceiver (receiver);

		DataReductionResponse drres = new DataReductionResponse(instrumentUniqueId, true);

		drres.setTransceiver (tranciever);
		ActionTools.sendToClient (drres);
	}
}