/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui.GUIDisplay;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterClientManager;


public class AddInfoToGUIDisplay
	extends Command
{
	private int context;
	private String category;
	private String icon;
	private String message;
	private String guiPaneId;
	private long uniqueId;
	private int level;

	public AddInfoToGUIDisplay ()
	{
		super("addinfotoguidisplay");
	}

	public AddInfoToGUIDisplay (
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

	public void perform ()
	{
		InfoCenterClientManager infoCenterClientManager =
			(InfoCenterClientManager) Engine.instance ().getManagerRegistry ().getManager (
				"infocenterclient");

		GUIDisplay guiDisplay = infoCenterClientManager.getGUIDisplay ();

		if (guiDisplay != null)
		{
			guiDisplay.addInfo (context, category, icon, message, guiPaneId, uniqueId, level);
		}
	}
}