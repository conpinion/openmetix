/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.command.CommandProcessor;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.thread.Threadable;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import java.util.LinkedList;
import java.util.List;


/**
 * AsyncCommandProcessor.
 *
 * @version $Id: AsyncCommandProcessor.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class AsyncCommandProcessor
	extends Threadable
	implements CommandProcessor
{
	/** All commands. */
	private List commands;

	/** Lock object. */
	private Object listLock;

	/**
	 * Create a new AsyncCommandProcessor.
	 */
	public AsyncCommandProcessor ()
	{
		super("AsyncCommandProcessor");
		commands = new LinkedList();
		listLock = new Object();
	}

	/**
	 * Checks the list and process the commands.
	 */
	public void run ()
	{
		Command command = null;

		synchronized (listLock)
		{
			if (commands.size () > 0)
			{
				command = (Command) commands.get (0);
				Log.log ("system", "AsyncCommandProcessor.run", "Command: " + command, Log.INFO);
				commands.remove (command);
			}
		}

		if (command != null)
		{
			performCommand (command);
		}

		synchronized (listLock)
		{
			if (commands.size () == 0)
			{
				try
				{
					listLock.wait ();
				}
				catch (InterruptedException x)
				{
				}
			}
		}
	}

	/**
	 * Add a command to the list.
	 *
	 * @param command The Command.
	 *
	 * @deprecated Use the perform() method.
	 */
	public void addCommand (Command command)
	{
		perform (command);
	}

	/**
	 * Perform a command.
	 */
	public Object perform (Command command)
	{
		synchronized (listLock)
		{
			commands.add (command);
			listLock.notify ();

			return null;
		}
	}

	/**
	 * Perform an command.
	 */
	private void performCommand (Command command)
	{
		Object lockObject = AppContext.instance ().getLockObject ();

		synchronized (lockObject)
		{
			if (command.canPerform ())
			{
				command.performWithResult ();
			}
		}
	}

	/**
	 * Check for processed commands.
	 *
	 * @return True if commands are processed.
	 */
	public boolean commandsInProcess ()
	{
		return commands.size () > 0;
	}

	/**
	 * Called from the ThreadController to close this Thread.
	 */
	public void dispose ()
	{
		synchronized (listLock)
		{
			setState (Threadable.CLOSING);
			listLock.notify ();
		}
	}
}