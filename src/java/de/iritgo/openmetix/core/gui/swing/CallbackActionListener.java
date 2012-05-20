/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.gui.swing;


import de.iritgo.openmetix.core.logger.Log;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * CallbackActionListener is a utility class that makes it easier to
 * register action listeners. Instead of subclassing ActionListener, you
 * only need to specify a (public) method, that will be calles by this
 * action listener through reflection.
 *
 * @version $Id: CallbackActionListener.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class CallbackActionListener
	implements ActionListener
{
	/** The object to call. */
	protected Object object;

	/** The method to call. */
	protected Method method;

	/**
	 * Create a new CallbackActionListener.
	 *
	 * @param callbackObject The object to invoke.
	 * @param methodName The name of the method to invoke.
	 */
	public CallbackActionListener (Object callbackObject, String methodName)
	{
		this.object = callbackObject;

		try
		{
			method = object.getClass ().getMethod (methodName, new Class[]
					{
						ActionEvent.class
					});
		}
		catch (NoSuchMethodException e)
		{
			Log.logError ("resource", "CallbackActionListener", "NoSuchMethodException");
		}
	}

	/**
	 * Called from the swing framework when an action event
	 * occurred
	 *
	 * @param event The action event.
	 */
	public void actionPerformed (ActionEvent event)
	{
		try
		{
			method.invoke (object, new Object[]
				{
					event
				});
		}
		catch (IllegalAccessException x)
		{
			Log.logError ("system", "CallbackActionListener", x.toString ());
		}
		catch (InvocationTargetException x)
		{
			Log.logError ("resource", "CallbackActionListener", x.toString ());
		}
	}
}