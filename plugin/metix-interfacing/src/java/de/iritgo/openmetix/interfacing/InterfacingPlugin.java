/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.interfacing;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.plugin.Plugin;
import de.iritgo.openmetix.framework.base.FrameworkPlugin;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutput;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingOutputEditor;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystem;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystemEditor;
import de.iritgo.openmetix.interfacing.gagingsystem.GagingSystemListEditor;
import de.iritgo.openmetix.interfacing.gagingsystemdriver.GagingSystemDriverDescriptor;
import de.iritgo.openmetix.interfacing.interfaceregistry.AddInterfaceRegistryToUser;
import de.iritgo.openmetix.interfacing.interfaceregistry.InterfaceRegistry;
import de.iritgo.openmetix.interfacing.link.Interface;
import de.iritgo.openmetix.interfacing.link.InterfaceEditor;
import de.iritgo.openmetix.interfacing.link.InterfaceListEditor;
import de.iritgo.openmetix.interfacing.link.SerialLink;


/**
 * The interfacing plugin contains the gaging system functions.
 *
 * @version $Id: InterfacingPlugin.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class InterfacingPlugin
	extends FrameworkPlugin
{
	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new GagingSystem());
		registerDataObject (new GagingOutput());
		registerDataObject (new InterfaceRegistry());
		registerDataObject (new Interface());
		registerDataObject (new SerialLink());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new GagingSystemListEditor());
		registerGUIPane (Plugin.CLIENT, new GagingSystemEditor());
		registerGUIPane (Plugin.CLIENT, new GagingOutputEditor());
		registerGUIPane (Plugin.CLIENT, new InterfaceListEditor());
		registerGUIPane (Plugin.CLIENT, new InterfaceEditor());
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new InterfacingManager());
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (Plugin.SERVER, new AddInterfaceRegistryToUser());
	}

	/**
	 * Initiaize the plugin.
	 *
	 * @param engine The system engine.
	 */
	public void init (Engine engine)
	{
		super.init (engine);

		GagingSystemDriverDescriptor.add (
			new GagingSystemDriverDescriptor(
				"Null", "metix-interfacing.nullSystemDriver",
				"de.iritgo.openmetix.interfacing.gagingsystemdriver.NullDriver",
				"de.iritgo.openmetix.interfacing.gagingsystemdriver.NullDriverEditor",
				"GagingOutputEditor", InterfacingPlugin.class.getClassLoader ()));
	}
}