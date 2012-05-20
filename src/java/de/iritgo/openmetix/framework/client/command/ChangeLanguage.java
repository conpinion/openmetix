/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.plugin.PluginManager;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.client.Client;
import java.util.Locale;
import java.util.Properties;


/**
 * ChangeLanguage.
 *
 * @version $Id: ChangeLanguage.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class ChangeLanguage
	extends Command
{
	private Locale locale = null;

	/**
	 * @deprecated Use the ChangeLanguage (Locale locale) constructor.
	 */
	public ChangeLanguage (String nativeLanguage)
	{
		if (nativeLanguage.equals ("Deutsch"))
		{
			locale = new Locale("de");
		}
		else if (nativeLanguage.equals ("English"))
		{
			locale = new Locale("en");
		}
	}

	public ChangeLanguage (Locale locale)
	{
		this.locale = locale;
	}

	public void setProperties (Properties properties)
	{
	}

	public void perform ()
	{
		Client.instance ().getClientGUI ().getDesktopManager ().saveVisibleDisplays ();
		Client.instance ().getClientGUI ().getDesktopManager ().closeAllDisplays ();

		Engine engine = Engine.instance ();

		String resourceDir =
			engine.getSystemDir () + engine.getFileSeparator () + "resources" +
			engine.getFileSeparator ();

		PluginManager pluginManager = engine.getPluginManager ();
		ResourceService resourceService = engine.getResourceService ();

		resourceService.unloadTranslationsFromJarFile (
			engine.getSystemDir () + engine.getFileSeparator (), "iritgo.jar", "resources/system");
		pluginManager.unloadTranslationResources ();

		resourceService.updateResourceBundle (locale);

		resourceService.unloadTranslationsFromJarFile (
			engine.getSystemDir () + engine.getFileSeparator (), "iritgo.jar", "resources/system");
		pluginManager.loadTranslationResources ();

		Client.instance ().getClientGUI ().getDesktopManager ().showSavedDisplays ();

		AppContext.instance ().setLocale (locale);
	}
}