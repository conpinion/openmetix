/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.console;


import java.util.HashMap;
import java.util.Iterator;


/**
 * ConsoleCommandRegistry.
 *
 * @version $Id: ConsoleCommandRegistry.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class ConsoleCommandRegistry
{
	private HashMap consoleCommands;

	public ConsoleCommandRegistry ()
	{
		consoleCommands = new HashMap();
	}

	public void add (ConsoleCommand consoleCommand)
	{
		consoleCommands.put (consoleCommand.getCommandId (), consoleCommand);
	}

	public ConsoleCommand get (String id)
	{
		return (ConsoleCommand) consoleCommands.get (id);
	}

	public void remove (ConsoleCommand consoleCommand)
	{
		consoleCommands.remove (consoleCommand.getCommandId ());
	}

	public Iterator getCommandIterator ()
	{
		return consoleCommands.values ().iterator ();
	}
}