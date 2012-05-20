/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.client;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.action.ActionProcessorRegistry;
import de.iritgo.openmetix.core.action.ReceiveEntryNetworkActionProcessor;
import de.iritgo.openmetix.core.action.SendEntryNetworkActionProcessor;
import de.iritgo.openmetix.core.action.SimpleActionProcessor;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.core.network.NetworkService;
import de.iritgo.openmetix.core.tools.NumberTools;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.action.ReceiveNetworkActionProcessor;
import de.iritgo.openmetix.framework.appcontext.AppContext;
import de.iritgo.openmetix.framework.base.InitIritgoException;
import de.iritgo.openmetix.framework.base.NetworkProxyLinkedListManager;
import de.iritgo.openmetix.framework.base.NetworkProxyManager;
import de.iritgo.openmetix.framework.base.command.PingPong;
import de.iritgo.openmetix.framework.base.command.SetLogLevel;
import de.iritgo.openmetix.framework.client.command.ClientReloadPlugins;
import de.iritgo.openmetix.framework.client.gui.ClientGUI;
import de.iritgo.openmetix.framework.client.network.SendNetworkActionProcessor;
import de.iritgo.openmetix.framework.client.network.SimpleSyncNetworkActionProcessor;
import de.iritgo.openmetix.framework.console.ConsoleCommand;
import de.iritgo.openmetix.framework.console.ConsoleCommandRegistry;
import de.iritgo.openmetix.framework.console.ConsoleManager;
import de.iritgo.openmetix.framework.manager.ClientManager;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import org.apache.avalon.framework.configuration.Configuration;


/**
 * Client.
 *
 * @version $Id: Client.java,v 1.1 2005/04/24 18:10:47 grappendorf Exp $
 */
public class Client
	extends BaseObject
{
	/** The singleton client. */
	static private Client client;

	/** System engine. */
	private Engine engine;

	/** Network services. */
	private NetworkService networkService;

	/** Client gui. */
	private ClientGUI clientGUI;

	/** The client transceiver. */
	private ClientTransceiver clientTransceiver;

	/** Action processors. */
	private ActionProcessorRegistry actionProcessorRegistry;

	/** The simple action processor. */
	private SimpleActionProcessor simpleActionProcessor;

	/** The application context. */
	private AppContext appContext;

	/** The user registry. */
	private UserRegistry userRegistry;

	/** The proxy manager. */
	private NetworkProxyManager networkProxyManager;

	/** The proxy linked list manager. */
	private NetworkProxyLinkedListManager networkProxyLinkedListManager;

	/**
	 * Create a new Client.
	 */
	public Client ()
	{
	}

	/**
	 * Initialize all client functions.
	 */
	public void init ()
		throws InitIritgoException
	{
		engine = Engine.instance ();

		appContext = AppContext.instance ();

		loadUser ();
		registerActionProcessors ();
		initBasics ();
		initResources ();
		registerConsoleCommands ();
	}

	/**
	 * Get the client instance.
	 *
	 * @return The client.
	 */
	static public Client instance ()
	{
		if (client == null)
		{
			client = new Client();
		}

		return client;
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
	 * Get the client gui.
	 *
	 * @return The client gui.
	 */
	public ClientGUI getClientGUI ()
	{
		return clientGUI;
	}

	/**
	 * Get the network service.
	 *
	 * @return The network service.
	 */
	public NetworkService getNetworkService ()
	{
		return networkService;
	}

	/**
	 * Load the user data.
	 */
	private void loadUser ()
	{
		userRegistry = new UserRegistry();

		User user = new User();

		user.setUniqueId (-1);
		appContext.setUser (user);
	}

	/**
	 * Init all action processors.
	 */
	private void registerActionProcessors ()
	{
		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor =
			new ReceiveEntryNetworkActionProcessor(
				"Client.ReceiveEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (receiveEntryNetworkActionProcessor);

		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor =
			new SendEntryNetworkActionProcessor(
				"Client.SendEntryNetworkActionProcessor", null, null);

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
				"Client.ReceiveEntryNetworkActionProcessor");

		ReceiveNetworkActionProcessor receiveNetworkActionProcessor =
			new ReceiveNetworkActionProcessor(null, receiveEntryNetworkActionProcessor);

		receiveEntryNetworkActionProcessor.addOutput (receiveNetworkActionProcessor);

		SimpleSyncNetworkActionProcessor simpleSyncNetworkActionProcessor =
			new SimpleSyncNetworkActionProcessor(null, receiveNetworkActionProcessor);

		receiveNetworkActionProcessor.addOutput (simpleSyncNetworkActionProcessor);

		SimpleActionProcessor simpleActionProcessor = new SimpleActionProcessor();

		simpleSyncNetworkActionProcessor.addOutput (simpleActionProcessor);
	}

	/**
	 * Create the send processors.
	 */
	private void createSend ()
	{
		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor =
			(SendEntryNetworkActionProcessor) actionProcessorRegistry.get (
				"Client.SendEntryNetworkActionProcessor");

		sendEntryNetworkActionProcessor.addOutput (
			new SendNetworkActionProcessor(networkService, null, sendEntryNetworkActionProcessor));
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
			new ConsoleCommand(
				"reloadplugins", new ClientReloadPlugins(), "system.help.reloadplugin", 0));
		consoleCommandRegistry.add (
			new ConsoleCommand("loglevel", new SetLogLevel(), "system.help.loglevel", 1));
		consoleCommandRegistry.add (
			new ConsoleCommand("pingpong", new PingPong(), "system.help.pingpong", 0));
	}

	/**
	 * Initialize the client gui.
	 *
	 * @throws InitIritgoException
	 */
	public void initGUI ()
		throws InitIritgoException
	{
		clientGUI =
			((ClientManager) engine.getManagerRegistry ().getManager ("client")).getClientGUI ();
		clientGUI.init ();
	}

	/**
	 * Shutdown the client gui.
	 *
	 * @throws InitIritgoException
	 */
	public void stopGUI ()
		throws InitIritgoException
	{
		clientGUI.stopGUI ();
		clientGUI = null;
	}

	/**
	 * Initialization.
	 *
	 * @throws InitIritgoException
	 */
	private void initBasics ()
		throws InitIritgoException
	{
		Configuration config = IritgoEngine.instance ().getConfiguration ();
		Configuration threadpoolConfig = config.getChild ("threadpool");

		int minThreads = NumberTools.toInt (threadpoolConfig.getAttribute ("minThreads", "8"), 8);

		for (int i = 0; i < minThreads; ++i)
		{
			engine.getThreadService ().addThreadSlot ();
		}

		networkProxyManager = new NetworkProxyManager();
		networkProxyLinkedListManager = new NetworkProxyLinkedListManager();
	}

	/**
	 * Initialize the resources.
	 *
	 * @throws InitIritgoException
	 */
	private void initResources ()
		throws InitIritgoException
	{
		engine.getResourceService ().loadTranslationsFromJarFile (
			engine.getSystemDir (), "iritgo.jar", "resources/system");
	}

	/**
	 * Start the client gui.
	 *
	 * @throws InitIritgoException
	 */
	public void startGUI ()
		throws InitIritgoException
	{
		clientGUI.startGUI ();
	}

	/**
	 * Start the application.
	 *
	 * @throws InitIritgoException
	 */
	public void startApplication ()
		throws InitIritgoException
	{
		clientGUI.startApplication ();
	}

	/**
	 * Called on a lost network connection.
	 */
	public void lostNetworkConnection ()
	{
		if (clientGUI != null)
		{
			clientGUI.lostNetworkConnection ();
		}

		Engine.instance ().getProxyEventRegistry ().clear ();
		Engine.instance ().getProxyRegistry ().clear ();
		Engine.instance ().getBaseRegistry ().clear ();
		Client.instance ().getUserRegistry ().clear ();
	}

	/**
	 * Shutdown the client.
	 */
	public void stop ()
	{
		try
		{
			Thread.sleep (1000 * 1);
		}
		catch (Exception x)
		{
		}

		clientGUI.stopApplication ();
		clientGUI.stopGUI ();

		clientGUI = null;

		networkService.closeChannel (appContext.getUser ().getNetworkChannel ());
	}
}