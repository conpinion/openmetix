/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.event.Event;
import de.iritgo.openmetix.core.event.EventListener;
import de.iritgo.openmetix.core.logger.Log;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * IObjectProxyEventRegistry.
 *
 * @version $Id: IObjectProxyEventRegistry.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectProxyEventRegistry
{
	/** All proxy event listeners. */
	private HashMap eventListeners;

	/** All unfired listeners. */
	private LinkedList unfiredListeners;

	/**
	 * Create a new IObjectProxyEventRegistry.
	 */
	public IObjectProxyEventRegistry ()
	{
		eventListeners = new HashMap();
		unfiredListeners = new LinkedList();
	}

	/**
	 * Add an event listener.
	 */
	public void addEventListener (IObject prototypeable, EventListener eventListener)
	{
		List listeners = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);
		}

		if (listeners == null)
		{
			listeners = new LinkedList();
			eventListeners.put (prototypeable, listeners);
		}

		if (! listeners.contains (eventListener))
		{
			listeners.add (eventListener);
		}
	}

	/**
	 * Remove an event listener.
	 */
	public void removeEventListener (IObject prototypeable, EventListener eventListener)
	{
		List listeners = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);
		}

		if (listeners != null)
		{
			listeners.remove (eventListener);
		}
	}

	/**
	 * Remove an event listener.
	 */
	public void removeEventListener (EventListener eventListener)
	{
		for (Iterator i = eventListeners.values ().iterator (); i.hasNext ();)
		{
			((List) i.next ()).remove (eventListener);
		}
	}

	/**
	 * Remove all event listeners.
	 */
	public void clear ()
	{
		eventListeners.clear ();
	}

	/**
	 * Fire an event.
	 */
	public void fire (IObject prototypeable, Event event)
	{
		List listeners = null;
		List tmpList = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);

			if ((listeners == null) || (listeners.size () == 0))
			{
				return;
			}

			tmpList = new LinkedList(listeners);
		}

		for (Iterator i = tmpList.iterator (); i.hasNext ();)
		{
			EventListener listener = (EventListener) i.next ();

			Class klass = listener.getClass ();
			boolean fired = false;

			while (klass != null && ! fired)
			{
				Class[] interfaces = klass.getInterfaces ();

				for (int j = 0; j < interfaces.length; ++j)
				{
					if (EventListener.class.isAssignableFrom (interfaces[j]))
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
								"system", "IObjectProxyEventRegistry.fire",
								"Called listener method has a InvocationTargetException in Class: " +
								klass + ": " + interfaces[j]);

							ByteArrayOutputStream trace = new ByteArrayOutputStream();
							PrintWriter traceOut = new PrintWriter(trace);

							x.getCause ().printStackTrace (traceOut);
							traceOut.close ();

							Log.logError (
								"system", "IObjectProxyEventRegistry.fire",
								"Root cause was: " + x.getCause ());

							Log.logError (
								"system", "IObjectProxyEventRegistry.fire",
								"Root cause stack trace: " + trace.toString ());
						}
					}
				}

				klass = klass.getSuperclass ();
			}
		}
	}
}