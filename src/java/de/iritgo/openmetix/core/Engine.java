/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core;


import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.core.base.BaseRegistry;
import de.iritgo.openmetix.core.base.SystemProperties;
import de.iritgo.openmetix.core.command.CommandProcessorRegistry;
import de.iritgo.openmetix.core.command.CommandRegistry;
import de.iritgo.openmetix.core.event.EventRegistry;
import de.iritgo.openmetix.core.flowcontrol.FlowControl;
import de.iritgo.openmetix.core.gui.IGUIFactory;
import de.iritgo.openmetix.core.iobject.IObjectFactory;
import de.iritgo.openmetix.core.iobject.IObjectProxyEventRegistry;
import de.iritgo.openmetix.core.iobject.IObjectProxyRegistry;
import de.iritgo.openmetix.core.logger.ConsoleLogger;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.logger.LoggerRegistry;
import de.iritgo.openmetix.core.logger.OutputStreamLogger;
import de.iritgo.openmetix.core.manager.Manager;
import de.iritgo.openmetix.core.manager.ManagerRegistry;
import de.iritgo.openmetix.core.plugin.PluginManager;
import de.iritgo.openmetix.core.resource.ResourceNode;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import de.iritgo.openmetix.core.thread.ThreadService;
import de.iritgo.openmetix.core.uid.DefaultIDGenerator;
import de.iritgo.openmetix.core.uid.IDGenerator;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.cli.CommandLine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * This is the engine of the Iritgo client/server-framework.
 *
 * All system initialization is performed in this class and most of the system
 * components can be accessed through the sinlgeton Engine instance.
 *
 * @version $Id: Engine.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class Engine
{
	/** Singleton Engine instance. */
	private static Engine engine;

	/** Engine name. */
	private String name;

	/** System directory. */
	private String systemDir;

	/** File path part separator. */
	private String fileSeparator;

	/** System properties. */
	private SystemProperties systemProperties;

	/** The system configuration. */
	private Configuration configuration;

	/** Unique id generator for persistent objects. */
	private IDGenerator persistentIdGenerator;

	/** Unique id generator for transient objects. */
	private IDGenerator transientIdGenerator;

	/** Resource subsystem. */
	private ResourceService resourceService;

	/** Threading subsystem. */
	private ThreadService threadService;

	/** Iritgo object factory. */
	private IObjectFactory iObjectFactory;

	/** Base object registry. */
	private BaseRegistry baseRegistry;

	/** Registry of action processors. */
	private ActionProcessorRegistry actionProcessorRegistry;

	/** Plugin manager. */
	private PluginManager pluginManager;

	/** Event registry. */
	private EventRegistry eventRegistry;

	/** Proxy event registry. */
	private IObjectProxyEventRegistry proxyEventRegistry;

	/** Flow controller. */
	private FlowControl flowControl;

	/** Proxy registry. */
	private IObjectProxyRegistry proxyRegistry;

	/** Manager registry. */
	private ManagerRegistry managerRegistry;

	/** Command registry. */
	private CommandRegistry commandRegistry;

	/** Command processor registry. */
	private CommandProcessorRegistry commandProcessorRegistry;

	/** Session context. */
	private SessionContext sessionContext;

	/** The command line options. */
	private CommandLine commandLine;

	/** The irigo build number. */
	private String buildNumber;

	/** The application build number. */
	private String applicationBuildNumber;

	/** The factory that creates gui elements. */
	private IGUIFactory guiFactory;

	/**
	 * Since Engine is a singleton, this constructor is private.
	 *
	 * @param commandLine Command line options.
	 */
	private Engine (String name, CommandLine commandLine)
	{
		this.name = name;
		this.commandLine = commandLine;
		systemDir = System.getProperty ("user.dir");
		fileSeparator = System.getProperty ("file.separator");

		if (commandLine.hasOption ("s"))
		{
			File dir = new File(commandLine.getOptionValue ("s").trim ());

			if (dir.isAbsolute ())
			{
				systemDir = dir.getPath ();
			}
			else
			{
				systemDir += fileSeparator + dir.getPath ();
			}
		}
	}

	/**
	 * Singleton constructor.
	 *
	 * @param commandLine Command line options.
	 */
	public static void create (String name, CommandLine commandLine)
	{
		engine = new Engine(name, commandLine);
		engine.init ();
	}

	/**
	 * Singleton accessor.
	 *
	 * @return The engine instance.
	 */
	static public Engine instance ()
	{
		return engine;
	}

	/**
	 * Initialize the engine.
	 */
	private void init ()
	{
		initLoggingEngine ();

		systemProperties = new SystemProperties();
		systemProperties.load (name + ".properties");

		iObjectFactory = new IObjectFactory();
		actionProcessorRegistry = new ActionProcessorRegistry();

		eventRegistry = new EventRegistry();
		proxyEventRegistry = new IObjectProxyEventRegistry();
		persistentIdGenerator = new DefaultIDGenerator();

		flowControl = new FlowControl();

		proxyRegistry = new IObjectProxyRegistry();
		baseRegistry = new BaseRegistry();

		managerRegistry = new ManagerRegistry();

		sessionContext = new SessionContext("root");

		commandRegistry = new CommandRegistry();

		commandProcessorRegistry = new CommandProcessorRegistry();

		initThreadPooling ();
		initResourceEngine ();
		initPluginManager ();
		readBuildNumbers ();
	}

	/**
	 * Read the build number from the ant generated files.
	 */
	private void readBuildNumbers ()
	{
		Properties props = new Properties();

		try
		{
			props.load (Engine.class.getResource ("/resources/build.number").openStream ());
			buildNumber = props.getProperty ("build.number");

			props.load (new FileInputStream(systemDir + fileSeparator + "build.number"));
			applicationBuildNumber = props.getProperty ("build.number");
		}
		catch (Exception x)
		{
			buildNumber = "Unknown build version.";
			applicationBuildNumber = "Unknown build version.";
		}
	}

	/**
	 * Initialize the logging subsystem.
	 */
	private void initLoggingEngine ()
	{
		LoggerRegistry loggerRegistry = new LoggerRegistry();

		if (commandLine.hasOption ("l"))
		{
			try
			{
				File logFile = new File(commandLine.getOptionValue ("l"));

				logFile.createNewFile ();
				loggerRegistry.addLogger (new OutputStreamLogger(new FileOutputStream(logFile)));
				loggerRegistry.addLogger ("system", "OutputStream");
				loggerRegistry.addLogger ("resource", "OutputStream");
				loggerRegistry.addLogger ("thread", "OutputStream");
				loggerRegistry.addLogger ("network", "OutputStream");
				loggerRegistry.addLogger ("plugin", "OutputStream");
				loggerRegistry.addLogger ("client", "OutputStream");
				loggerRegistry.addLogger ("server", "OutputStream");
				loggerRegistry.addLogger ("persist", "OutputStream");
			}
			catch (IOException x)
			{
				System.err.println (
					"Unable to create log file '" + commandLine.getOptionValue ("l") + "': " +
					x.toString ());
			}
		}
		else
		{
			loggerRegistry.addLogger (new ConsoleLogger());
			loggerRegistry.addLogger ("system", "Console");
			loggerRegistry.addLogger ("resource", "Console");
			loggerRegistry.addLogger ("thread", "Console");
			loggerRegistry.addLogger ("network", "Console");
			loggerRegistry.addLogger ("plugin", "Console");
			loggerRegistry.addLogger ("client", "Console");
			loggerRegistry.addLogger ("server", "Console");
			loggerRegistry.addLogger ("persist", "Console");
		}

		Log.logInfo ("system", "Engine.initLoggingEngine", "Logging subsystem initialized");
	}

	/**
	 * Initialize the resource subsystem.
	 */
	private void initResourceEngine ()
	{
		resourceService = new ResourceService(new ResourceNode("resource.engine.root", "root"));
		Log.logInfo ("system", "Engine.initResourceEngine", "Resource subsystem initialized");
	}

	/**
	 * Initialize the threading subsystem.
	 */
	private void initThreadPooling ()
	{
		threadService = new ThreadService();
		threadService.addThreadSlot ();
		threadService.addThreadSlot ();
		Log.logInfo ("system", "Engine.initThreadPooling", "Threading subsystem initialized");
	}

	/**
	 * Initialize the plugin subsystem.
	 */
	private void initPluginManager ()
	{
		pluginManager = new PluginManager(this);
	}

	/**
	 * Store the system properties.
	 */
	public void storeSystemProperties ()
	{
		systemProperties.store (name + ".properties");
	}

	/**
	 * Start the engine.
	 */
	public void start ()
	{
	}

	/**
	 * Stop the engine.
	 *
	 * @return True if all resources are successfully released.
	 */
	public boolean stop ()
	{
		storeSystemProperties ();

		return getThreadService ().stopThreadEngine ();
	}

	/**
	 * Retrieve the registry containing the action processors.
	 *
	 * @return The action processor registry.
	 */
	public ActionProcessorRegistry getActionProcessorRegistry ()
	{
		return actionProcessorRegistry;
	}

	/**
	 * Retrieve the iritgo object factory.
	 *
	 * @return The iritgo object factory.
	 */
	public IObjectFactory getIObjectFactory ()
	{
		return iObjectFactory;
	}

	/**
	 * Retrieve the resource service.
	 *
	 * @return The resource service.
	 */
	public ResourceService getResourceService ()
	{
		return resourceService;
	}

	/**
	 * Retrieve the threading service.
	 *
	 * @return The threading service.
	 */
	public ThreadService getThreadService ()
	{
		return threadService;
	}

	/**
	 * Retrieve the plugin manager.
	 *
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager ()
	{
		return pluginManager;
	}

	/**
	 * Retrieve the directory from which the Iritgo system was started.
	 *
	 * @return The system directory.
	 */
	public String getSystemDir ()
	{
		return systemDir;
	}

	/**
	 * Retrieve the (os-dependent) string used to separate file path parts.
	 *
	 * @return The file separator.
	 */
	public String getFileSeparator ()
	{
		return fileSeparator;
	}

	/**
	 * Retrieve the system properties.
	 *
	 * @return The system properties.
	 */
	public SystemProperties getSystemProperties ()
	{
		return systemProperties;
	}

	/**
	 * Retrieve the event registry.
	 *
	 * @return The event registry.
	 */
	public EventRegistry getEventRegistry ()
	{
		return eventRegistry;
	}

	/**
	 * Retrieve the proxy event registry.
	 *
	 * @return The proxy event registry.
	 */
	public IObjectProxyEventRegistry getProxyEventRegistry ()
	{
		return proxyEventRegistry;
	}

	/**
	 * Retrieve the unique id generator for persistent objects.
	 *
	 * @return The unique id generator.
	 */
	public IDGenerator getPersistentIDGenerator ()
	{
		return persistentIdGenerator;
	}

	/**
	 * Install a new unique id generator for persistent objects.
	 *
	 * @return The new unique id generator.
	 */
	public void installPersistentIDGenerator (IDGenerator idGenerator)
	{
		this.persistentIdGenerator = idGenerator;
	}

	/**
	 * Retrieve the unique id generator for transient objects.
	 *
	 * @return The unique id generator.
	 */
	public IDGenerator getTransientIDGenerator ()
	{
		return transientIdGenerator;
	}

	/**
	 * Install a new unique id generator for transient objects.
	 *
	 * @return The new unique id generator.
	 */
	public void installTransientIDGenerator (IDGenerator idGenerator)
	{
		this.transientIdGenerator = idGenerator;
	}

	/**
	 * Retrieve the flow controller.
	 *
	 * @return The flow controller.
	 */
	public FlowControl getFlowControl ()
	{
		return flowControl;
	}

	/**
	 * Retrieve the base registry.
	 *
	 * @return The base registry.
	 */
	public BaseRegistry getBaseRegistry ()
	{
		return baseRegistry;
	}

	/**
	 * Retrieve the proxy registry.
	 *
	 * @return The proxy registry.
	 */
	public IObjectProxyRegistry getProxyRegistry ()
	{
		return proxyRegistry;
	}

	/**
	 * Retrieve the manager registry.
	 *
	 * @return The manager registry.
	 */
	public ManagerRegistry getManagerRegistry ()
	{
		return managerRegistry;
	}

	/**
	 * Static Helper method for easier manager access.
	 *
	 * @param id The id of the manager to retrieve.
	 * @return The specified manager or null if none was found.
	 */
	public static Manager getManager (String id)
	{
		return instance ().getManagerRegistry ().getManager (id);
	}

	/**
	 * Retrieve the session context.
	 *
	 * @return The session context.
	 */
	public SessionContext getSessionContext ()
	{
		return sessionContext;
	}

	/**
	 * Retrieve the command registry.
	 *
	 * @return The session context.
	 */
	public CommandRegistry getCommandRegistry ()
	{
		return commandRegistry;
	}

	/**
	 * Retrieve the command processor registry.
	 *
	 * @return The command processor registry.
	 */
	public CommandProcessorRegistry getCommandProcessorRegistry ()
	{
		return commandProcessorRegistry;
	}

	/**
	 * Return the build number of the framework.
	 *
	 * @return The build number.
	 */
	public String getBuildNumber ()
	{
		return buildNumber;
	}

	/**
	 * Return the build number of the application.
	 *
	 * @return The build number.
	 */
	public String getApplicationBuildNumber ()
	{
		return applicationBuildNumber;
	}

	/**
	 * Retrieve the gui factory.
	 *
	 * @return The gui factory.
	 */
	public IGUIFactory getGUIFactory ()
	{
		if (guiFactory == null)
		{
			Log.logError ("system", "Engine.getGUIFactory", "No gui factory set");
		}

		return guiFactory;
	}

	/**
	 * Set the gui factory.
	 *
	 * @param guiFactory The new gui factory.
	 */
	public void setGUIFactory (IGUIFactory guiFactory)
	{
		this.guiFactory = guiFactory;
	}

	/**
	 * Get the system configuration.
	 *
	 * @return The configuration.
	 */
	public Configuration getConfiguration ()
	{
		return configuration;
	}

	/**
	 * Set the system configuration.
	 *
	 * @param configuration The new configuration.
	 */
	public void setConfiguration (Configuration configuration)
	{
		this.configuration = configuration;
	}
}