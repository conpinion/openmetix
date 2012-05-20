/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.event;


import de.iritgo.openmetix.core.logger.Log;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * The EventRegistry is the central point used to register event
 * listeners and fire events.
 *
 * Take for example an event class PhoneRingEvent with an
 * assciated PhoneRingListener:
 *
 * <pre>
 *     public class PhoneRingEvent extends Event
 *     {
 *         public String phoneNumber;
 *
 *         ...
 *     }
 *
 *     public interface PhoneRingListener implements EventListener
 *     {
 *         public void phoneIsRinging (PhoneRingEvent event);
 *     }
 * </pre>
 *
 * Lets assume that you want to listen for PhoneRingEvents in a class
 * called CallCenter:
 *
 * <pre>
 *     public class CallCenter implements PhoneRingListener
 *     {
 *         public void phoneIsRinging (PhoneRingEvent event)
 *         {
 *             System.out.println ("Call from " + event.phoneNumber);
 *         }
 *
 *         ...
 *     }
 * </pre>
 *
 * At last you need to register your CallCenter instance with
 * the EventRegistry:
 *
 * <pre>
 *     Engine.getEngine ().getEventRegistry ().addListener ("phone", myCallCenter);
 * </pre>
 *
 * @version $Id: EventRegistry.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class EventRegistry
{
	/**
	 * Events are organized by categories. For each category exists a list of
	 * event listeners.
	 */
	private HashMap categoryList;

	/**
	 * Create a new EventRegistry
	 */
	public EventRegistry ()
	{
		categoryList = new HashMap();
	}

	/**
	 * Add an event lister to an event category.
	 *
	 * @param category The event category.
	 * @param listener The event listener to add.
	 */
	public void addListener (String category, EventListener listener)
	{
		if (! categoryList.containsKey (category))
		{
			categoryList.put (category, new LinkedList());
		}

		List listeners = (List) categoryList.get (category);

		if (! listeners.contains (listener))
		{
			listeners.add (listener);
		}
	}

	/**
	 * Remove an event listener from an event category.
	 *
	 * @param category The event category.
	 * @param listener The event listener to remove.
	 */
	public void removeListener (String category, EventListener listener)
	{
		List listeners = (List) categoryList.get (category);

		if (listeners != null)
		{
			listeners.remove (listener);
		}
	}

	/**
	 * Remove all from the registry
	 */
	public void clear ()
	{
		categoryList.clear ();
	}

	/**
	 * Fire an event.
	 *
	 * @param category The event category which listener should be notified.
	 * @param event The event object that is send to the listener.
	 */
	public void fire (String category, Event event)
	{
		List listeners = (List) categoryList.get (category);

		if (listeners == null)
		{
			return;
		}

		for (Iterator i = listeners.iterator (); i.hasNext ();)
		{
			EventListener listener = (EventListener) i.next ();

			Class klass = listener.getClass ();
			boolean fired = false;

			while (klass != null && ! fired)
			{
				Class[] interfaces = klass.getInterfaces ();

				for (int j = 0; j < interfaces.length; ++j)
				{
					if (
						de.iritgo.openmetix.core.event.EventListener.class.isAssignableFrom (
							interfaces[j]))
					{
						try
						{
							interfaces[j].getDeclaredMethods ()[0].invoke (
								listener, new Object[]
								{
									event
								});
							fired = true;

							break;
						}
						catch (IllegalArgumentException x)
						{
						}
						catch (SecurityException x)
						{
						}
						catch (IllegalAccessException x)
						{
						}
						catch (InvocationTargetException x)
						{
							Log.logError (
								"system", "EventRegistry.fire",
								"Called listener method has a InvocationTargetException in Class: " +
								klass + ": " + interfaces[j]);

							ByteArrayOutputStream trace = new ByteArrayOutputStream();
							PrintWriter traceOut = new PrintWriter(trace);

							x.getCause ().printStackTrace (traceOut);
							traceOut.close ();

							Log.logError (
								"system", "EventRegistry.fire", "Root cause was: " + x.getCause ());

							Log.logError (
								"system", "EventRegistry.fire",
								"Root cause stack trace: " + trace.toString ());
						}
					}
				}

				klass = klass.getSuperclass ();
			}
		}
	}
}