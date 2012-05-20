/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.infocenter;


import de.iritgo.openmetix.framework.user.User;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class InfoCenterRegistry
{
	static Hashtable CategoryRegistry = new Hashtable();
	static Hashtable displayRegistry = new Hashtable();

	public InfoCenterRegistry ()
	{
		InfoCenter.setDisplayRegistrys (CategoryRegistry, displayRegistry);
	}

	public void initBaseDisplay ()
	{
	}

	public void addDisplay (String category, String displayId, int context, User user)
	{
		displayId = displayId + context;

		if (! CategoryRegistry.containsKey (category + context))
		{
			CategoryRegistry.put (category + context, new ArrayList());
		}

		ArrayList displayList = (ArrayList) CategoryRegistry.get (category + context);

		if (displayList.contains (displayId))
		{
			return;
		}

		displayList.add (displayId);

		((InfoCenterDisplay) displayRegistry.get (displayId)).init (category, context, user);
	}

	public void addDisplay (InfoCenterDisplay display, int context)
	{
		String displayId = display.getId () + context;

		if (! displayRegistry.contains (displayId))
		{
			displayRegistry.put (displayId, display);
		}
	}

	public void removeDisplay (String category, String displayId, int context)
	{
		displayId = displayId + context;
		category = category + context;

		if (! CategoryRegistry.containsKey (category))
		{
			return;
		}

		ArrayList displayList = (ArrayList) CategoryRegistry.get (category);

		for (int i = 0; i < displayList.size (); ++i)
		{
			if (displayList.get (i).equals (displayId))
			{
				displayList.remove (i);
			}
		}
	}

	public void removeDisplay (String displayId, int context)
	{
		displayId = displayId + context;

		Enumeration e = CategoryRegistry.elements ();

		while (e.hasMoreElements ())
		{
			ArrayList displayList = (ArrayList) e.nextElement ();

			for (int i = 0; i < displayList.size (); ++i)
			{
				if (displayList.get (i).equals (displayId))
				{
					displayList.remove (i);
				}
			}
		}

		displayRegistry.remove (displayId);
	}

	public void close ()
	{
		Enumeration e = CategoryRegistry.elements ();

		while (e.hasMoreElements ())
		{
			ArrayList displayList = (ArrayList) e.nextElement ();

			for (int i = 0; i < displayList.size (); ++i)
			{
				InfoCenterDisplay display =
					(InfoCenterDisplay) displayRegistry.get ((String) displayList.get (i));

				display.release ();
				displayList.remove (i);
				displayRegistry.remove (display);
			}
		}
	}
}