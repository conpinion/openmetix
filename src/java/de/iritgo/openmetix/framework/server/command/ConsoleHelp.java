/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.framework.console.ConsoleCommand;
import de.iritgo.openmetix.framework.console.ConsoleManager;
import java.util.Iterator;


/**
 * ConsoleHelp.
 *
 * @version $Id: ConsoleHelp.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class ConsoleHelp
	extends Command
{
	private String command = null;

	public ConsoleHelp ()
	{
	}

	public ConsoleHelp (String command)
	{
		this.command = command;
	}

	public void perform ()
	{
		Engine engine = Engine.instance ();
		ResourceService resourceService = engine.getResourceService ();
		ConsoleManager consoleManager =
			(ConsoleManager) engine.getManagerRegistry ().getManager ("console");

		if (command == null)
		{
			for (Iterator i = consoleManager.getConsoleCommandIterator (); i.hasNext ();)
			{
				System.out.println (
					resourceService.getStringWithoutException (
						((ConsoleCommand) i.next ()).getHelpId () + ".short"));
			}
		}
		else
		{
			ConsoleCommand consoleCommand = consoleManager.getConsoleCommand (command);

			if (consoleCommand == null)
			{
				System.out.println ("Command unknown.");

				return;
			}

			System.out.println (
				resourceService.getStringWithoutException (consoleCommand.getHelpId () + ".long"));
		}
	}
}