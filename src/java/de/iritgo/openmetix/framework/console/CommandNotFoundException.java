/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.console;



/**
 * CommandNotFoundException.
 *
 * @version $Id: CommandNotFoundException.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class CommandNotFoundException
	extends Exception
{
	public CommandNotFoundException (String message)
	{
		super(message);
	}
}