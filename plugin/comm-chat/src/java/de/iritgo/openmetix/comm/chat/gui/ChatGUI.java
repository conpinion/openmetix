/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.comm.chat.gui;


public interface ChatGUI
{
	public void joinChannel (String string, String string_0_);

	public void leaveChannel (Integer channelId, String string_1_);

	public void addMessage (String string, int channelId, String string_3_);

	public Integer getCurrentChannel ();
}