/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.chatter;


import de.iritgo.openmetix.core.action.AbstractAction;
import de.iritgo.openmetix.framework.user.User;


public interface Processor
{
	public AbstractAction getAction (User user, User newUser);
}