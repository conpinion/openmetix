/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.action;


import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command.AddInfoToGUIDisplay;
import java.io.IOException;


public class InfoCenterAction
	extends FrameworkAction
{
	private int context;
	private String category;
	private String icon;
	private String message;
	private String guiPaneId;
	private long uniqueId;
	private int level;

	public InfoCenterAction ()
	{
	}

	public InfoCenterAction (
		int context, String category, String icon, String message, String guiPaneId, long uniqueId,
		int level)
	{
		this.context = context;
		this.category = category;
		this.icon = icon;
		this.message = message;
		this.guiPaneId = guiPaneId;
		this.uniqueId = uniqueId;
		this.level = level;
	}

	public void readObject (FrameworkInputStream stream)
		throws IOException
	{
		context = stream.readInt ();
		category = stream.readUTF ();
		icon = stream.readUTF ();
		message = stream.readUTF ();
		guiPaneId = stream.readUTF ();
		uniqueId = stream.readLong ();
		level = stream.readInt ();
	}

	public void writeObject (FrameworkOutputStream stream)
		throws IOException
	{
		stream.writeInt (context);
		stream.writeUTF (category);
		stream.writeUTF (icon);
		stream.writeUTF (message);
		stream.writeUTF (guiPaneId);
		stream.writeLong (uniqueId);
		stream.writeInt (level);
	}

	public void perform ()
	{
		AddInfoToGUIDisplay displayCmd =
			new AddInfoToGUIDisplay(context, category, icon, message, guiPaneId, uniqueId, level);

		CommandTools.performSimple (displayCmd);
	}
}