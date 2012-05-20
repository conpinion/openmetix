/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.chatter;


public class UserAllreadyJoindException
	extends Exception
{
	public UserAllreadyJoindException ()
	{
		super();
	}

	public UserAllreadyJoindException (String message)
	{
		super(message);
	}
}