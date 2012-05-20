/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework;


import org.apache.commons.cli.Options;


/**
 * Start this class to start an Iritgo server.
 *
 * @version $Id: IritgoServer.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class IritgoServer
{
	/**
	 * The server main method.
	 *
	 * @param args The program args.
	 */
	public static void main (String[] args)
	{
		Options options = new Options();

		IritgoEngine.create (IritgoEngine.START_SERVER, options, args);
	}
}