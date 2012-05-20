/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.alarm;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.client.gui.ClientGUI;
import de.iritgo.openmetix.framework.command.CommandTools;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * The alarm manager coordinates the display of alarm events. It installs warning icons in
 * the instrument displays and tab headers. After a delay the manager removes these icons.
 *
 * @version $Id: AlarmManager.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class AlarmManager
	extends BaseObject
	implements Manager, SensorAlarmListener
{
	/** Minimum alarm duration. */
	private static long ALARM_COOLDOWN = 1000 * 10;

	/** All currently active alarms. */
	private TreeMap alarms;

	/** Termination flag for the alarm thread. */
	private boolean running = true;

	/** Icons used to display alarms. */
	private ImageIcon[] alarmIcons;

	/** Alarms on tab headers. */
	private Map tabAlarms;

	/**
	 * Create a new AlarmManager.
	 */
	public AlarmManager ()
	{
		super("AlarmManager");
		alarms = new TreeMap();
		tabAlarms = new HashMap();
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		alarmIcons =
			new ImageIcon[]
			{
				new ImageIcon(AlarmManager.class.getResource ("/resources/alarm_on.png")),
				new ImageIcon(AlarmManager.class.getResource ("/resources/alarm_off.png"))
			};

		Engine.instance ().getEventRegistry ().addListener ("sensoralarm", this);

		Thread alarmThread =
			new Thread()
			{
				public void run ()
				{
					int iconStep = 0;

					while (running)
					{
						synchronized (alarms)
						{
							if (alarms.size () != 0)
							{
								Long time = (Long) alarms.firstKey ();

								if (System.currentTimeMillis () >= time.longValue ())
								{
									SensorAlarm sensorAlarm = (SensorAlarm) alarms.get (time);

									((SwingGUIPane) sensorAlarm.getGUIPane ()).setIcon (
										sensorAlarm.getOldIcon ());

									alarms.remove (time);

									int alarmAnz =
										((Integer) tabAlarms.get (sensorAlarm.getDesktopId ())).intValue () -
										1;

									tabAlarms.put (
										sensorAlarm.getDesktopId (), new Integer(alarmAnz));

									if (alarmAnz <= 0)
									{
										setTabAlarm (sensorAlarm.getDesktopId (), null);
									}
								}

								for (Iterator i = alarms.values ().iterator (); i.hasNext ();)
								{
									SensorAlarm sensorAlarm = (SensorAlarm) i.next ();

									((SwingGUIPane) sensorAlarm.getGUIPane ()).setIcon (
										alarmIcons[iconStep]);
								}
							}

							iconStep = (iconStep + 1) % 2;
						}

						try
						{
							Thread.sleep (500);
						}
						catch (Exception x)
						{
						}
					}
				}
			};

		alarmThread.setDaemon (true);
		alarmThread.start ();
	}

	/**
	 * Free all manager resources.
	 */
	public void unload ()
	{
		Engine.instance ().getEventRegistry ().removeListener ("sensoralarm", this);
		running = false;
	}

	/**
	 * Called when a sensor has signalled an alarm.
	 *
	 * @param event The alarm event.
	 */
	public void sensorAlarm (SensorAlarmEvent event)
	{
		synchronized (alarms)
		{
			if (
				((SwingGUIPane) event.getGUIPane ()).getIcon () == alarmIcons[0] ||
				((SwingGUIPane) event.getGUIPane ()).getIcon () == alarmIcons[1])
			{
				return;
			}

			alarms.put (
				new Long(System.currentTimeMillis () + ALARM_COOLDOWN),
				new SensorAlarm(
					event.getGUIPane (), ((SwingGUIPane) event.getGUIPane ()).getIcon (),
					event.getGUIPane ().getDisplay ().getDesktopId ()));
			((SwingGUIPane) event.getGUIPane ()).setIcon (alarmIcons[0]);

			ClientGUI clientGUI = Client.instance ().getClientGUI ();
			IDesktopManager iDesktopManager = clientGUI.getDesktopManager ();

			IDisplay display = (IDisplay) event.getGUIPane ().getDisplay ();

			String desktopId = display.getDesktopId ();

			Integer tabAlarmAnz = (Integer) tabAlarms.get (desktopId);

			if (tabAlarmAnz == null)
			{
				tabAlarmAnz = new Integer(0);
			}

			int tmp = tabAlarmAnz.intValue () + 1;

			tabAlarms.put (display.getDesktopId (), new Integer(tmp));
			setTabAlarm (display.getDesktopId (), alarmIcons[0]);
		}
	}

	/**
	 * Show/hide a tab alarm icon.
	 *
	 * @param desktopPaneId The id of the desktop page for which to set the icon.
	 * @param icon The icon to set, which may be null.
	 */
	public void setTabAlarm (String desktopPaneId, Icon icon)
	{
		Properties props = new Properties();

		props.put ("desktopPaneId", desktopPaneId);

		if (icon != null)
		{
			props.put ("icon", icon);
		}
		else
		{
			props.put (
				"icon",
				new Icon()
				{
					public int getIconHeight ()
					{
						return 0;
					}

					public int getIconWidth ()
					{
						return 0;
					}

					public void paintIcon (Component c, Graphics g, int x, int y)
					{
					}
				});
		}

		CommandTools.performAsync ("SetTabIcon", props);
	}
}