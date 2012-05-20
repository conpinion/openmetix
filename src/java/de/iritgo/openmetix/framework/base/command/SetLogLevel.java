/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base.command;


import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.logger.Log;
import java.util.Properties;


/**
 * SetLogLevel.
 *
 * @version $Id: SetLogLevel.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class SetLogLevel
	extends Command
{
	private int level;

	public SetLogLevel ()
	{
		super("setloglevel");
	}

	public SetLogLevel (String level)
	{
		this.level = new Integer(level).intValue ();
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		Log.setLevel (level);
	}
}