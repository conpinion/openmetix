/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.appcontext;



/**
 * ServerAppContext.
 *
 * @version $Id: ServerAppContext.java,v 1.1 2005/04/24 18:10:44 grappendorf Exp $
 */
public class ServerAppContext
	extends AppContext
{
	/** Singleton application context. */
	static private ServerAppContext serverAppContext;

	/**
	 * Create a new ServerAppContext.
	 */
	public ServerAppContext ()
	{
		super();
	}

	/**
	 * Get the application context.
	 *
	 * @return The application context.
	 */
	static public ServerAppContext serverInstance ()
	{
		if (serverAppContext != null)
		{
			return serverAppContext;
		}

		serverAppContext = new ServerAppContext();

		return serverAppContext;
	}
}