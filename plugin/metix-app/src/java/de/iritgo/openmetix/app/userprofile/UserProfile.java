/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.userprofile;


import de.iritgo.openmetix.app.configurationsensor.ConfigurationSensor;
import de.iritgo.openmetix.app.instrument.Instrument;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * The UserProfile contains the list of instruments, active displays and
 * the user's preferences.
 *
 * @version $Id: UserProfile.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class UserProfile
	extends DataObject
{
	/**
	 * Create a new UserProfile.
	 */
	public UserProfile ()
	{
		super("UserProfile");

		putAttribute ("desktopList", "empty");
		putAttribute ("activeDesktopId", "unknown");

		putAttribute (
			"instruments",
			new IObjectList(
				"instruments",
				new FrameworkProxy(
					new Instrument("dummy")
			{
				public ConfigurationSensor getTmpSensor ()
				{
					return null;
				}
			}), this));

		putAttribute (
			"activeDisplays",
			new IObjectList("activeDisplays", new FrameworkProxy(new ActiveDisplay()), this));

		putAttribute (
			"preferences",
			new IObjectList("preferences", new FrameworkProxy(new Preferences()), this));
	}

	/**
	 * Create a new UserProfile.
	 *
	 * @param uniqueId The unique id.
	 */
	public UserProfile (long uniqueId)
	{
		this();
		setUniqueId (uniqueId);
	}

	/**
	 * Get the stringified list of all desktops.
	 *
	 * @return The desktop list.
	 */
	public String getDesktopList ()
	{
		return getStringAttribute ("desktopList");
	}

	/**
	 * Set the stringified list of all desktops.
	 *
	 * @param desktopList The desktop list.
	 */
	public void setDesktopList (String desktopList)
	{
		putAttribute ("desktopList", desktopList);
	}

	/**
	 * Get the id of the active desktop.
	 *
	 * @return The id of the active desktop.
	 */
	public String getActiveDesktopId ()
	{
		return getStringAttribute ("activeDesktopId");
	}

	/**
	 * Set the id of the active desktop.
	 *
	 * @param activeDesktopId The id of the active desktop.
	 */
	public void setActiveDesktopId (String activeDesktopId)
	{
		putAttribute ("activeDesktopId", activeDesktopId);
	}

	/**
	 * Add an instrument to the profile.
	 *
	 * @param instrument The instrument to add.
	 */
	public void addInstrument (Instrument instrument)
	{
		getIObjectListAttribute ("instruments").add (instrument);
	}

	/**
	 * Remove an instrument from the profile.
	 *
	 * @param instrument The instrument to remove.
	 */
	public void removeInstrument (Instrument instrument)
	{
		getIObjectListAttribute ("instruments").remove (instrument);
	}

	/**
	 * Get an iterator over all instruments.
	 *
	 * @return An instrument iterator.
	 */
	public Iterator instrumentIterator ()
	{
		return getIObjectListAttribute ("instruments").iterator ();
	}

	/**
	 * Add an active display to the profile.
	 *
	 * @param activeDisplay The active display to add.
	 */
	public void addActiveDisplay (ActiveDisplay activeDisplay)
	{
		getIObjectListAttribute ("activeDisplays").add (activeDisplay);
	}

	/**
	 * Get an iterator over all active display.
	 *
	 * @return An active display iterator.
	 */
	public Iterator activeDisplayIterator ()
	{
		return getIObjectListAttribute ("activeDisplays").iterator ();
	}

	/**
	 * Get number of active display.
	 *
	 * @return The number of active display.
	 */
	public int getActiveDisplayCount ()
	{
		return getIObjectListAttribute ("activeDisplays").size ();
	}

	/**
	 * Remove all active displays from the profile.
	 */
	public void removeAllActiveDisplays ()
	{
		getIObjectListAttribute ("activeDisplays").clear ();
	}

	/**
	 * Add a preferences object to the profile.
	 *
	 * @param preferences The preferences to add.
	 */
	public void addPreferences (Preferences preferences)
	{
		getIObjectListAttribute ("preferences").add (preferences);
	}

	/**
	 * Get the preferences object.
	 *
	 * @return The preferences object.
	 */
	public Preferences getPreferences ()
	{
		Iterator i = getIObjectListAttribute ("preferences").iterator ();

		if (i.hasNext ())
		{
			return (Preferences) i.next ();
		}
		else
		{
			return null;
		}
	}
}