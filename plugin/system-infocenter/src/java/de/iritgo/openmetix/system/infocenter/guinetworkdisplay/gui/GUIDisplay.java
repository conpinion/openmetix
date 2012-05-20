/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui;


public interface GUIDisplay
{
	public void addInfo (
		int context, String category, String icon, String message, String guiPaneId, long uniqueId,
		int level);
}