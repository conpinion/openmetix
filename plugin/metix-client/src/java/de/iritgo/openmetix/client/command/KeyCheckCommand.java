/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.client.command;


import de.iritgo.openmetix.client.key.KeyCheckRequest;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.action.ActionTools;


public class KeyCheckCommand
	extends Command
	implements KeyCommand
{
	private String licensingName = new String();
	private String key = new String();
	private long instrumentUniqueId;
	private String performName = new String();
	private boolean checkStart = false;

	public KeyCheckCommand ()
	{
		super("KeyCheckCommand");
	}

	public void setLicensingName (String name)
	{
		this.licensingName = name;
	}

	public void setKey (String entryKey)
	{
		this.key = entryKey;
	}

	public void setPerformName (String name)
	{
		this.performName = name;
	}

	public void setCheckStart (boolean value)
	{
		this.checkStart = value;
	}

	public void setInstrumentUniqueId (long instrumentUniqueId)
	{
		this.instrumentUniqueId = instrumentUniqueId;
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you do not want to return a command result (The
	 * return value defaults to null).
	 */
	public void perform ()
	{
		KeyCheckRequest kcr =
			new KeyCheckRequest(
				instrumentUniqueId, performName, this.licensingName, this.key, this.checkStart);

		ActionTools.sendToServer (kcr);
	}
}