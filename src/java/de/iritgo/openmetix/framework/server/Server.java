/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.server;


import bsh.Interpreter;
import bsh.util.JConsole;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.core.action.FilterActionProcessor;
import de.iritgo.openmetix.core.action.ReceiveEntryNetworkActionProcessor;
import de.iritgo.openmetix.core.action.SendEntryNetworkActionProcessor;
import de.iritgo.openmetix.core.action.SimpleActionProcessor;
import de.iritgo.openmetix.core.action.ThreadNetworkActionProcessor;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.network.NetworkService;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.core.uid.IDGenerator;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.action.ConcurrencyNetworkActionProcessor;
import de.iritgo.openmetix.framework.action.ReceiveNetworkActionProcessor;
import de.iritgo.openmetix.framework.appcontext.ServerAppContext;
import de.iritgo.openmetix.framework.base.InitIritgoException;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownManager;
import de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver;
import de.iritgo.openmetix.framework.console.CommandNotFoundException;
import de.iritgo.openmetix.framework.console.ConsoleCommand;
import de.iritgo.openmetix.framework.console.ConsoleCommandRegistry;
import de.iritgo.openmetix.framework.console.ConsoleManager;
import de.iritgo.openmetix.framework.console.UnknownClassException;
import de.iritgo.openmetix.framework.console.UnknownConstructorException;
import de.iritgo.openmetix.framework.console.UnknownErrorException;
import de.iritgo.openmetix.framework.console.WrongParameterException;
import de.iritgo.openmetix.framework.server.command.ConsoleHelp;
import de.iritgo.openmetix.framework.server.command.ReloadPlugins;
import de.iritgo.openmetix.framework.server.command.ShowThreads;
import de.iritgo.openmetix.framework.server.command.ShowUsers;
import de.iritgo.openmetix.framework.server.network.NetworkSystemListenerImpl;
import de.iritgo.openmetix.framework.server.network.SendNetworkActionProcessor;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import org.apache.avalon.framework.configuration.Configuration;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Server.
 *
 * @version $Id: Server.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class Server
	extends BaseObject
	implements ShutdownObserver
{
	/** The singleton server. */
	static private Server server;

	/** The system engine. */
	private Engine engine;

	/** The network services. */
	private NetworkService networkService;

	/** The client transceiver. */
	private ClientTransceiver clientTransceiver;

	/** All action processors. */
	private ActionProcessorRegistry actionProcessorRegistry;

	/** The simple action processor. */
	private SimpleActionProcessor simpleActionProcessor;

	/** The application context. */
	private ServerAppContext serverAppContext;

	/** The user registry. */
	private UserRegistry userRegistry;

	/** The unique id generator. */
	private IDGenerator appIdGenerator;

	/**
	 * Create a new Server.
	 */
	public Server ()
	{
	}

	/**
	 * Return the Server.
	 *
	 * @return The Server
	 */
	static public Server instance ()
	{
		if (server == null)
		{
			server = new Server();
		}

		return server;
	}

	/**
	 * Return the system engine.
	 *
	 * @return The system engine.
	 */
	public Engine getEngine ()
	{
		return engine;
	}

	/**
	 * Get the user registry.
	 *
	 * @return The user registry.
	 */
	public UserRegistry getUserRegistry ()
	{
		return userRegistry;
	}

	/**
	 * Get the id generator.
	 *
	 * @return The id generator.
	 */
	public IDGenerator getApplicationIdGenerator ()
	{
		return appIdGenerator;
	}

	/**
	 * Initialize all server functions.
	 */
	public void init ()
		throws InitIritgoException
	{
		engine = Engine.instance ();

		serverAppContext = (ServerAppContext) ServerAppContext.serverInstance ();

		initBasics ();
		registerActionProcessors ();
		initApplication ();
		registerConsoleCommands ();

		((ShutdownManager) engine.getManagerRegistry ().getManager ("shutdown")).addObserver (this);

		Log.logInfo ("system", "Server.init", "Server successfully initialized");
	}

	/**
	 * Initialize the application.
	 */
	private void initApplication ()
	{
		userRegistry = new UserRegistry();
		appIdGenerator = Engine.instance ().getPersistentIDGenerator ();
	}

	/**
	 * Initialization.
	 *
	 * @throws InitIritgoException
	 */
	private void initBasics ()
		throws InitIritgoException
	{
		IObjectProxy.initState = true;
	}

	/**
	 * Initialize the action processors.
	 */
	private void registerActionProcessors ()
	{
		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor =
			new ReceiveEntryNetworkActionProcessor(
				"Server.ReceiveEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (receiveEntryNetworkActionProcessor);

		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor =
			new SendEntryNetworkActionProcessor(
				"Server.SendEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (sendEntryNetworkActionProcessor);

		networkService =
			new NetworkService(
				engine.getThreadService (), receiveEntryNetworkActionProcessor,
				sendEntryNetworkActionProcessor);
	}

	/**
	 * This method creates a default network action processor pipeline.
	 */
	public void createDefaultNetworkProcessingSystem ()
	{
		createReceive ();
		createSend ();
	}

	/**
	 * Create the receive processors.
	 */
	private void createReceive ()
	{
		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor =
			(ReceiveEntryNetworkActionProcessor) actionProcessorRegistry.get (
				"Server.ReceiveEntryNetworkActionProcessor");

		ReceiveNetworkActionProcessor receiveNetworkActionProcessor =
			new ReceiveNetworkActionProcessor(null, receiveEntryNetworkActionProcessor);

		receiveEntryNetworkActionProcessor.addOutput (receiveNetworkActionProcessor);

		ConcurrencyNetworkActionProcessor concurrencyNetworkActionProcessor =
			new ConcurrencyNetworkActionProcessor(null, receiveNetworkActionProcessor);

		ThreadNetworkActionProcessor threadActionProcessor =
			new ThreadNetworkActionProcessor(
				"ThreadNetworkActionProcessor", null, concurrencyNetworkActionProcessor);

		SimpleActionProcessor simpleActionProcessor = new SimpleActionProcessor();

		threadActionProcessor.addOutput (simpleActionProcessor);

		concurrencyNetworkActionProcessor.setThreadNetworkActionProcessor (threadActionProcessor);

		receiveNetworkActionProcessor.addOutput (concurrencyNetworkActionProcessor);
	}

	/**
	 * Create the send processors.
	 */
	private void createSend ()
	{
		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor =
			(SendEntryNetworkActionProcessor) actionProcessorRegistry.get (
				"Server.SendEntryNetworkActionProcessor");

		FilterActionProcessor filterActionProcessor =
			new FilterActionProcessor(
				"Server.FilterActionProcessor", null, sendEntryNetworkActionProcessor);

		sendEntryNetworkActionProcessor.addOutput (filterActionProcessor);

		actionProcessorRegistry.put (filterActionProcessor);

		filterActionProcessor.addOutput (
			new SendNetworkActionProcessor(networkService, null, filterActionProcessor));
	}

	/**
	 * Get the network services.
	 *
	 * @return The network services.
	 */
	public NetworkService getNetworkService ()
	{
		return networkService;
	}

	/**
	 * Register all console commands.
	 *
	 * @throws InitIritgoException
	 */
	private void registerConsoleCommands ()
		throws InitIritgoException
	{
		ConsoleCommandRegistry consoleCommandRegistry =
			((ConsoleManager) engine.getManagerRegistry ().getManager ("console")).getConsoleCommandRegistry ();

		consoleCommandRegistry.add (
			new ConsoleCommand("help", new ConsoleHelp(), "system.help.help"));

		consoleCommandRegistry.add (
			new ConsoleCommand(
				"reloadplugins", new ReloadPlugins(), "system.help.reloadPlugins", 0));

		consoleCommandRegistry.add (
			new ConsoleCommand("showthreads", new ShowThreads(), "system.help.showThreads", 0));

		consoleCommandRegistry.add (
			new ConsoleCommand("showusers", new ShowUsers(), "system.help.showUsers", 0));
	}

	/**
	 * Initialize the network system.
	 */
	private void initNetwork ()
	{
		Configuration config = IritgoEngine.instance ().getConfiguration ();
		Configuration socketConfig = config.getChild ("network").getChild ("socket");

		int port = socketConfig.getAttributeAsInteger ("port", 3000);
		int acceptTimeout = socketConfig.getAttributeAsInteger ("acceptTimeout", 60000);

		Configuration threadpoolConfig = config.getChild ("threadpool");

		int minThreads = NumberTools.toInt (threadpoolConfig.getAttribute ("minThreads", "8"), 8);

		for (int i = 0; i < minThreads; ++i)
		{
			engine.getThreadService ().addThreadSlot ();
		}

		networkService.listen (serverAppContext.getServerIP (), port, acceptTimeout);

		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		networkService.addNetworkSystemListener (new NetworkSystemListenerImpl());
	}

	/**
	 * Start the server.
	 */
	public void start ()
	{
		initNetwork ();

		if (! IritgoEngine.instance ().getCommandLine ().hasOption ("q"))
		{
			System.out.println (
				"Iritgo Client/Server-Framework (Build " + engine.getApplicationBuildNumber () +
				"/" + engine.getBuildNumber () + "). Copyright (C) 2004-2007 BueroByte GbR");
		}

		if (IritgoEngine.instance ().getCommandLine ().hasOption ("c"))
		{
			commandLoop ();
		}
		else
		{
			try
			{
				synchronized (this)
				{
					wait ();
				}
			}
			catch (InterruptedException x)
			{
			}
		}

		shutdown ();
	}

	public void stop ()
	{
	}

	/**
	 * Run the console command loop.
	 */
	private void commandLoop ()
	{
		ResourceService resourceService = engine.getResourceService ();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			while (true)
			{
				System.out.print ("> ");
				System.out.flush ();

				String line = in.readLine ();

				if (line.equals ("quit"))
				{
					break;
				}

				if (line.equals (""))
				{
					continue;
				}

				if (line.equals ("beanshell"))
				{
					JFrame frame = new JFrame("Console");

					JConsole console = new JConsole();

					frame.setContentPane (console);
					frame.setVisible (true);

					Interpreter i = new Interpreter(console);

					new Thread(i).start ();

					continue;
				}

				ConsoleManager consoleManager =
					(ConsoleManager) Engine.instance ().getManagerRegistry ().getManager (
						"console");

				try
				{
					consoleManager.doConsoleCommand (line);
				}
				catch (CommandNotFoundException x)
				{
					System.out.println (
						resourceService.getStringWithoutException ("system.unknownCommand"));
				}
				catch (WrongParameterException x)
				{
					System.out.println ("Wrong parameter(s) for the command.\n");
				}
				catch (UnknownClassException x)
				{
					System.out.println ("Unknown class for this command. (Plugin failure?)\n");
				}
				catch (UnknownConstructorException x)
				{
					System.out.println ("Unknown constructor for this command.\n");
				}
				catch (UnknownErrorException x)
				{
					System.out.println ("Unknown error for this command.\n");
				}
			}
		}
		catch (Exception x)
		{
		}

		Log.logInfo ("system", "Server", "Shutting down the server");
	}

	/**
	 * Terminate the server.
	 */
	public void shutdown ()
	{
		if (! IritgoEngine.instance ().shutdown ())
		{
			System.exit (0);
		}
	}

	/**
	 * @see de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver#onUserLogoff(de.iritgo.openmetix.framework.user.User)
	 */
	public void onUserLogoff (User user)
	{
		if (IritgoEngine.instance ().getCommandLine ().hasOption ("e"))
		{
			shutdown ();
		}
	}

	/**
	 * @see de.iritgo.openmetix.framework.base.shutdown.ShutdownObserver#onShutdown()
	 */
	public void onShutdown ()
	{
	}
}