/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.infocenter;


import de.iritgo.openmetix.framework.user.User;
import java.util.ArrayList;
import java.util.Hashtable;


public class InfoCenter
{
	static Hashtable categoryRegistry = null;
	static Hashtable displayRegistry = null;
	static public int INFORMATION = 1;
	static public int WARNING = 2;
	static public int FATAL = 10;

	public InfoCenter ()
	{
	}

	public static void setDisplayRegistrys (Hashtable h1, Hashtable h2)
	{
		categoryRegistry = h1;
		displayRegistry = h2;
	}

	public static void info (
		User user, int context, String category, String icon, String message, String guiPaneId,
		long uniqueId, int level)
	{
		if (categoryRegistry == null)
		{
			return;
		}

		if (! categoryRegistry.containsKey (category + context))
		{
			return;
		}

		ArrayList loggerlist = (ArrayList) categoryRegistry.get (category + context);

		for (int i = 0; i < loggerlist.size (); ++i)
		{
			((InfoCenterDisplay) displayRegistry.get (loggerlist.get (i))).info (
				user, context, category, icon, message, guiPaneId, uniqueId, level);
		}
	}

	public static void info (User user, int context, String category, String icon, String message)
	{
		info (user, context, category, icon, message, "", 0, 0);
	}

	public static void info (User user, int context, String category, String message)
	{
		info (user, context, category, "", message, "", 0, 0);
	}
}