/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.console;


import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.framework.IritgoEngine;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ConsoleManager.
 *
 * @version $Id: ConsoleManager.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class ConsoleManager
	extends BaseObject
	implements Manager
{
	private ConsoleCommandRegistry consoleCommandRegistry;

	public ConsoleManager ()
	{
		super("console");
		init ();
	}

	public void init ()
	{
		consoleCommandRegistry = new ConsoleCommandRegistry();
	}

	public ConsoleCommandRegistry getConsoleCommandRegistry ()
	{
		return consoleCommandRegistry;
	}

	public void doConsoleCommand (String commandString)
		throws CommandNotFoundException, WrongParameterException, UnknownClassException, 
			UnknownConstructorException, UnknownErrorException
	{
		String[] params = commandString.split (" ");

		int paramLength = params.length;

		if (paramLength == 0)
		{
			return;
		}

		try
		{
			Object constructorParamDeclaration = null;

			constructorParamDeclaration =
				Array.newInstance (Class.forName ("java.lang.Class"), paramLength - 1);

			List constructorParams = new LinkedList();

			for (int i = 1; i < paramLength; ++i)
			{
				Array.set (constructorParamDeclaration, i - 1, "dummy".getClass ());

				constructorParams.add (params[i]);
			}

			ClassLoader loader = Thread.currentThread ().getContextClassLoader ();

			ConsoleCommand consoleCommand = consoleCommandRegistry.get (params[0]);

			if (consoleCommand == null)
			{
				Log.log (
					"system", "ConsoleManager.doConsoleCommand", "command not registerd!", Log.WARN);
				throw new CommandNotFoundException("Command not found: " + params[0]);
			}

			if (
				((paramLength - 1) != consoleCommand.getNumParam ()) &&
				(consoleCommand.getNumParam () >= 0))
			{
				Log.log ("system", "ConsoleManager.doConsoleCommand", "wrong parameter", Log.WARN);
				throw new WrongParameterException("Wrong parameter(s) for command: " + params[0]);
			}

			Class klass = consoleCommand.getCommand ().getClass ();

			if (klass == null)
			{
				Log.log ("system", "ConsoleManager.doConsoleCommand", "no class found!", Log.WARN);
				throw new UnknownClassException("Unknown Class for command: " + params[0]);
			}

			Constructor constructor = klass.getConstructor ((Class[]) constructorParamDeclaration);

			if (constructor == null)
			{
				Log.log (
					"system", "ConsoleManager.doConsoleCommand", "no constructor found!", Log.WARN);
				throw new UnknownConstructorException(
					"Unknown Constructor for command: " + params[0]);
			}

			Command command = (Command) constructor.newInstance (constructorParams.toArray ());

			IritgoEngine.instance ().getSimpleCommandProcessor ().perform (command);
		}
		catch (ClassNotFoundException x)
		{
			Log.log (
				"system", "ConsoleManager.doConsoleCommand", "command not registerd!", Log.WARN);
			throw new CommandNotFoundException("Command not found: " + params[0]);
		}
		catch (NoSuchMethodException x)
		{
			Log.log ("system", "ConsoleManager.doConsoleCommand", "wrong parameter", Log.WARN);
			throw new WrongParameterException("Wrong parameter(s) for command: " + params[0]);
		}
		catch (InstantiationException x)
		{
			Log.log (
				"system", "ConsoleManager.doConsoleCommand", "no constructor found!", Log.WARN);
			throw new UnknownConstructorException("Unknown Constructor for command: " + params[0]);
		}
		catch (IllegalAccessException x)
		{
			Log.log (
				"system", "ConsoleManager.doConsoleCommand", "command not registerd!", Log.WARN);
			throw new CommandNotFoundException("Command not found: " + params[0]);
		}
		catch (InvocationTargetException x)
		{
			Log.log (
				"system", "ConsoleManager.doConsoleCommand", "no constructor found!", Log.WARN);
			throw new UnknownConstructorException("Unknown Constructor for command: " + params[0]);
		}
	}

	public ConsoleCommand getConsoleCommand (String commandId)
	{
		return consoleCommandRegistry.get (commandId);
	}

	public Iterator getConsoleCommandIterator ()
	{
		return consoleCommandRegistry.getCommandIterator ();
	}

	public void unload ()
	{
	}
}