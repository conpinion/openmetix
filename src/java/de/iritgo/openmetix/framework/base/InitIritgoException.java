/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;



/**
 * An instance of <code>InitIritgoException</code> is thrown if an error occurred during
 * the initialization of the framework system.
 *
 * @version $Id: InitIritgoException.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class InitIritgoException
	extends Exception
{
	/**
	 * Create a new <code>InitIritgoException</code>.
	 */
	public InitIritgoException ()
	{
		super();
	}

	/**
	 * Create a new <code>InitIritgoException</code>.
	 *
	 * @param cause The original error cause.
	 */
	public InitIritgoException (Throwable cause)
	{
		super(cause);
	}

	/**
	 * Create a new <code>InitIritgoException</code>.
	 *
	 * @param message The exception message.
	 */
	public InitIritgoException (String message)
	{
		super(message);
	}

	/**
	 * Create a new <code>InitIritgoException</code>.
	 *
	 * @param message The exception message.
	 * @param cause The original error cause.
	 */
	public InitIritgoException (String message, Throwable cause)
	{
		super(message, cause);
	}
}