/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.SimpleCommandProcessor;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.plugin.PluginManager;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.thread.ThreadService;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.core.uid.PropertiesIDGenerator;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.appcontext.ServerAppContext;
import de.iritgo.openmetix.framework.base.AsyncCommandProcessor;
import de.iritgo.openmetix.framework.base.InitIritgoException;
import de.iritgo.openmetix.framework.base.action.AliveCheckAction;
import de.iritgo.openmetix.framework.base.action.AliveCheckServerAction;
import de.iritgo.openmetix.framework.base.action.EditIObjectAction;
import de.iritgo.openmetix.framework.base.action.EditIObjectServerAction;
import de.iritgo.openmetix.framework.base.action.ProxyAction;
import de.iritgo.openmetix.framework.base.action.ProxyLinkedListAddAction;
import de.iritgo.openmetix.framework.base.action.ProxyLinkedListAddServerAction;
import de.iritgo.openmetix.framework.base.action.ProxyLinkedListRemoveAction;
import de.iritgo.openmetix.framework.base.action.ProxyLinkedListRemoveServerAction;
import de.iritgo.openmetix.framework.base.action.ProxyServerAction;
import de.iritgo.openmetix.framework.base.action.WrongVersionAction;
import de.iritgo.openmetix.framework.base.command.SetLogLevel;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import de.iritgo.openmetix.framework.client.Client;
import de.iritgo.openmetix.framework.console.ConsoleCommand;
import de.iritgo.openmetix.framework.console.ConsoleCommandRegistry;
import de.iritgo.openmetix.framework.console.ConsoleManager;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.action.RegisterNewUserAction;
import de.iritgo.openmetix.framework.user.action.RegisterNewUserFailureAction;
import de.iritgo.openmetix.framework.user.action.RegisterNewUserServerAction;
import de.iritgo.openmetix.framework.user.action.UserLoginAction;
import de.iritgo.openmetix.framework.user.action.UserLoginFailureAction;
import de.iritgo.openmetix.framework.user.action.UserLoginServerAction;
import de.iritgo.openmetix.framework.user.action.UserLogoffAction;
import de.iritgo.openmetix.framework.user.action.UserLogoffServerAction;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;


/**
 * This is the main startup class of the Iritgo system.
 * The IritgoClient or IritgoServer classes create
 * a single instance of the IritgoEngine class which initializes the
 * whole system.
 *
 * @version $Id: IritgoEngine.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class IritgoEngine
{
	/** Singleton framework instance. */
	private static IritgoEngine framework;

	/** The system engine. */
	private Engine engine;

	/** The system resources. */
	private ResourceService resources;

	/** The standard asynchronous command processor. */
	private AsyncCommandProcessor asyncCommandProcessor;

	/** The standard simple command processor. */
	private SimpleCommandProcessor simpleCommandProcessor;

	/** If the framework is started in server mode, this is the server. */
	private Server server;

	/** If the framework is started in client mode, this is the client. */
	private Client client;

	/** Start the engine as a server instance. */
	public static final int START_SERVER = 1;

	/** Start the engine as a client instance. */
	public static final int START_CLIENT = 2;

	/** The engine mode (START_SERVER or START_CLIENT). */
	private int mode;

	/** The name of this engine. */
	private String engineName;

	/** The command line options. */
	private CommandLine commandLine;

	/**
	 * Since IritgoEngine is a singleton, this constructor is private.
	 *
	 * @param mode The framework startup mode (START_SERVER or START_CLIENT).
	 */
	private IritgoEngine (int mode)
	{
		this.mode = mode;
		engineName = "iritgo";

		if (mode == IritgoEngine.START_SERVER)
		{
			engineName = "server";
		}
		else if (mode == IritgoEngine.START_CLIENT)
		{
			engineName = "client";
		}
	}

	/**
	 * Singleton constructor.
	 *
	 * @param mode The framework startup mode (START_SERVER or START_CLIENT).
	 */
	public static void create (int mode, Options options, String[] args)
	{
		framework = new IritgoEngine(mode);

		CommandLine commandLine = framework.processOptions (options, args);

		if (commandLine != null)
		{
			framework.init (commandLine);
		}
	}

	/**
	 * Singleton accessor.
	 *
	 * @return The framework instance.
	 */
	static public IritgoEngine instance ()
	{
		return framework;
	}

	/**
	 * Process the command line options.
	 *
	 * @param options The option description.
	 * @param args The actual program arguments.
	 * @return The parsed command line or null if we should better not start
	 *   the engine.
	 */
	private CommandLine processOptions (Options options, String[] args)
	{
		options.addOption ("h", "help", false, "Print this message");
		options.addOption ("d", "debug", true, "Set the initial debugging level");
		options.addOption ("c", "server-cli", false, "Start the interactive server shell");
		options.addOption ("l", "log-file", true, "Print logging messages to the specified file");
		options.addOption ("q", "quiet", false, "Ommit informational startup messages");
		options.addOption (
			"n", "no-version-check", false,
			"Don't check the application and framework version at logon");
		options.addOption (
			"s", "system-dir", true, "Set the directory containing the system files");
		options.addOption (
			"e", "embedded", false,
			"Activate the embedded mode (one single server and client instance)");

		CommandLine line = null;

		try
		{
			line = new PosixParser().parse (options, args);

			if (line.hasOption ("h"))
			{
				printHelp (options);

				return null;
			}
		}
		catch (ParseException exp)
		{
			printHelp (options);

			return null;
		}

		return line;
	}

	/**
	 * Print the command line options help.
	 *
	 * @param options The command line options.
	 */
	private void printHelp (Options options)
	{
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp (
			80, engineName, "Iritgo Client/Server-Framework Copyright (C) 2004 BueroByte GbR",
			options, "");
	}

	/**
	 * Initialize the framework.
	 *
	 * @param options The command line options.
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void init (CommandLine options)
	{
		try
		{
			commandLine = options;

			AppContext appContext = AppContext.instance ();
			ServerAppContext serverAppContext = ServerAppContext.serverInstance ();

			Log.setLevel (NumberTools.toInt (options.getOptionValue ("d"), Log.ERROR));

			initEngine ();
			initIdGenerator ();
			registerDefaultActions ();
			registerDefaultManager ();
			registerConsoleCommands ();
			initCommandProcessor ();
			initConfiguration ();

			if (mode == IritgoEngine.START_SERVER)
			{
				server = Server.instance ();
				server.init ();
				serverAppContext.setServer (true);
				serverAppContext.setClient (false);
				appContext.setServer (true);
				appContext.setClient (false);
			}

			if (mode == IritgoEngine.START_CLIENT)
			{
				client = Client.instance ();
				client.init ();
				serverAppContext.setServer (false);
				serverAppContext.setClient (true);
				appContext.setServer (false);
				appContext.setClient (true);
			}

			initPlugins ();

			if (mode == IritgoEngine.START_SERVER)
			{
				server = Server.instance ();
				server.start ();
			}

			if (mode == IritgoEngine.START_CLIENT)
			{
				client = Client.instance ();
				client.initGUI ();
				client.startGUI ();
				client.startApplication ();
			}
		}
		catch (InitIritgoException x)
		{
			if (engine != null)
			{
				engine.stop ();
			}
		}
	}

	/**
	 * Initialize the system engine.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initEngine ()
		throws InitIritgoException
	{
		Engine.create (engineName, commandLine);
		engine = Engine.instance ();

		resources = engine.getResourceService ();
		resources.loadResources (IritgoEngine.class.getResource ("/resources/framework.xml"));
		resources.loadTranslationsFromJarFile (
			engine.getSystemDir () + engine.getFileSeparator (), "iritgo.jar", "resources/system");

		engine.start ();
	}

	/**
	 * Initialize the unique id generator.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initIdGenerator ()
	{
		PropertiesIDGenerator idGenerator =
			new PropertiesIDGenerator(
				Engine.instance ().getSystemProperties (), "system.lastUniqueId", 1, 1);

		idGenerator.load ();

		Engine.instance ().installPersistentIDGenerator (idGenerator);
		Engine.instance ().installTransientIDGenerator (idGenerator);
	}

	/**
	 * Initialize the command processors.
	 */
	private void initCommandProcessor ()
	{
		ThreadService threadService = engine.getThreadService ();

		asyncCommandProcessor = new AsyncCommandProcessor();
		threadService.addThreadSlot ();
		threadService.add (asyncCommandProcessor);
		engine.getCommandProcessorRegistry ().add (asyncCommandProcessor);

		simpleCommandProcessor = new SimpleCommandProcessor();
		engine.getCommandProcessorRegistry ().add (simpleCommandProcessor);
	}

	/**
	 * Initialize the framework configuration.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initConfiguration ()
		throws InitIritgoException
	{
		String configName = "iritgo";

		if (mode == IritgoEngine.START_SERVER)
		{
			configName = "server-config";
		}
		else if (mode == IritgoEngine.START_CLIENT)
		{
			configName = "client-config";
		}

		try
		{
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();

			Configuration configuration =
				builder.buildFromFile (
					new File(
						engine.getSystemDir () + engine.getFileSeparator () + configName + ".xml"));

			engine.setConfiguration (configuration);

			Log.logInfo (
				"system", "Framework.initConfiguration",
				"Configuration loaded from file '" + configName + ".xml'");
		}
		catch (ConfigurationException x)
		{
			Log.logFatal (
				"system", "Framework.initConfiguration",
				"Configuration error while loging the configuration file '" + configName +
				".xml':" + x);
			throw new InitIritgoException(x);
		}
		catch (SAXException x)
		{
			Log.logFatal (
				"system", "Framework.initConfiguration",
				"XML error while loging the configuration file '" + configName + ".xml':" + x);
			throw new InitIritgoException(x);
		}
		catch (IOException x)
		{
			Log.logFatal (
				"system", "Framework.initConfiguration",
				"IO error while loging the configuration file '" + configName + ".xml': " + x);
			throw new InitIritgoException(x);
		}
	}

	/**
	 * Create an register all system actions.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerDefaultActions ()
		throws InitIritgoException
	{
		engine.getIObjectFactory ().register (new EditIObjectAction());
		engine.getIObjectFactory ().register (new EditIObjectServerAction());

		engine.getIObjectFactory ().register (new WrongVersionAction());

		engine.getIObjectFactory ().register (new UserLoginAction());
		engine.getIObjectFactory ().register (new UserLoginFailureAction());
		engine.getIObjectFactory ().register (new UserLogoffAction());
		engine.getIObjectFactory ().register (new UserLoginServerAction());
		engine.getIObjectFactory ().register (new UserLogoffServerAction());

		engine.getIObjectFactory ().register (new RegisterNewUserAction());
		engine.getIObjectFactory ().register (new RegisterNewUserServerAction());
		engine.getIObjectFactory ().register (new RegisterNewUserFailureAction());
		engine.getIObjectFactory ().register (new RegisterNewUserServerAction());

		engine.getIObjectFactory ().register (new ProxyAction());
		engine.getIObjectFactory ().register (new ProxyServerAction());

		engine.getIObjectFactory ().register (new AliveCheckAction());
		engine.getIObjectFactory ().register (new AliveCheckServerAction());

		engine.getIObjectFactory ().register (new ProxyLinkedListAddAction());
		engine.getIObjectFactory ().register (new ProxyLinkedListAddServerAction());
		engine.getIObjectFactory ().register (new ProxyLinkedListRemoveAction());
		engine.getIObjectFactory ().register (new ProxyLinkedListRemoveServerAction());
	}

	/**
	 * Create and register the system managers.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerDefaultManager ()
		throws InitIritgoException
	{
		engine.getManagerRegistry ().addManager (new ConsoleManager());
		engine.getManagerRegistry ().addManager (new ShutdownManager());
	}

	/**
	 * Create and register the system console commands.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerConsoleCommands ()
		throws InitIritgoException
	{
		ConsoleCommandRegistry consoleCommandRegistry =
			((ConsoleManager) engine.getManagerRegistry ().getManager ("console")).getConsoleCommandRegistry ();

		consoleCommandRegistry.add (
			new ConsoleCommand("loglevel", new SetLogLevel(), "system.help.logLevel", 1));
	}

	/**
	 * Initialize the plugins.
	 */
	public void initPlugins ()
	{
		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.loadPlugins ();

		pluginManager.initPlugins ();
	}

	/**
	 * Retrieve the standard async command processor.
	 *
	 * @return The async command processor.
	 */
	public AsyncCommandProcessor getAsyncCommandProcessor ()
	{
		return asyncCommandProcessor;
	}

	/**
	 * Retrieve the standard simple command processor.
	 *
	 * @return The simple command processor.
	 */
	public SimpleCommandProcessor getSimpleCommandProcessor ()
	{
		return simpleCommandProcessor;
	}

	/**
	 * Shutdown the framework.
	 *
	 * @return True if all resources are successfully released.
	 */
	public boolean shutdown ()
	{
		new Thread(
			new Runnable()
			{
				public void run ()
				{
					((ShutdownManager) engine.getManagerRegistry ().getManager ("shutdown")).shutdown ();

					if (server != null)
					{
						server.stop ();
					}

					if (client != null)
					{
						client.stop ();
					}

					Engine.instance ().getPersistentIDGenerator ().save ();
					Engine.instance ().getTransientIDGenerator ().save ();

					PluginManager pluginManager = engine.getPluginManager ();

					pluginManager.unloadPlugins ();
					engine.stop ();
				}
			}).start ();

		return true;
	}

	/**
	 * Get the command line parameters.
	 *
	 * @return The command line paramters.
	 */
	public CommandLine getCommandLine ()
	{
		return commandLine;
	}

	/**
	 * Get the system configuration.
	 *
	 * @return The configuration.
	 */
	public Configuration getConfiguration ()
	{
		return engine.getConfiguration ();
	}
}