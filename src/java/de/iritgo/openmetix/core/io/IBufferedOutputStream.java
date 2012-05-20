/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.io;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * IBufferedOutputStream.
 *
 * @version $Id: IBufferedOutputStream.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class IBufferedOutputStream
	extends BufferedOutputStream
{
	/**
	 * Creates a IBufferedOutputStream and saves its argument, the output stream in, for later use.
	 */
	public IBufferedOutputStream (OutputStream in)
	{
		super(in);
	}

	/**
	 * Creates a BufferedOutputStream with the specified buffer size, and saves
	 * its argument, the output stream in, for later use.
	 */
	public IBufferedOutputStream (OutputStream in, int size)
	{
		super(in, size);
	}

	/**
	 * Close the stream and free all resources.
	 */
	public void close ()
		throws IOException
	{
		super.close ();

		buf = null;
	}
}