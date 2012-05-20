/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.system.infocenter.infocenter.InfoCenterDisplay;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DiskWriterDisplay
	implements InfoCenterDisplay
{
	private String infoStoreFile;
	private File file;
	private FileWriter writer;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMd");
	private static SimpleDateFormat diskDateFormat = new SimpleDateFormat("yyyy:MM:d-H:m:s");
	private String day = simpleDateFormat.format (new Date());

	public DiskWriterDisplay ()
	{
	}

	public void setInfoStoreFile (String infoStoreFile)
	{
		this.infoStoreFile = infoStoreFile;
	}

	public String getId ()
	{
		return "diskwriter.display";
	}

	public void init (String category, int context, User user)
	{
		try
		{
			file = new File(infoStoreFile + "-" + context + "-" + day + "." + category);

			if (! file.exists ())
			{
				file.createNewFile ();
			}

			writer = new FileWriter(file, true);
		}
		catch (IOException x)
		{
			Log.log (
				"system", "DiskWriterDisplay.init",
				"Cannot create or read infostorefile" + x.getMessage (), Log.FATAL);
		}
	}

	public void release ()
	{
		try
		{
			writer.flush ();
			writer.close ();
		}
		catch (IOException x)
		{
			Log.log (
				"system", "DiskWriterDisplay.release",
				"Cannot close infostorefile" + x.getMessage (), Log.FATAL);
		}
	}

	public void info (
		User user, int context, String category, String icon, String message, String guiPaneId,
		long uniqueId, int level)
	{
		String currentMonth = simpleDateFormat.format (new Date());

		if (! currentMonth.equals (day))
		{
			release ();
			init (category, context, null);
		}

		try
		{
			writer.write (
				diskDateFormat.format (new Date()) + "|" + category + "|" + icon + "|" + message +
				"|" + guiPaneId + "|" + uniqueId + "|" + level + "\r\n");

			writer.flush ();
		}
		catch (IOException x)
		{
			Log.log (
				"system", "DiskWriterDisplay.info",
				"Cannot write to infostorefile" + x.getMessage (), Log.FATAL);
		}
	}
}