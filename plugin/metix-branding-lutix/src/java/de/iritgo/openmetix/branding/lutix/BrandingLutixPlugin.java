/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.branding.lutix;


import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;


/**
 * The interfacing plugin contains branding specific extensions.
 *
 * @version $Id: BrandingLutixPlugin.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class BrandingLutixPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new AboutPane());
	}
}