/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.io;


import java.io.BufferedInputStream;
import java.io.InputStream;


/**
 * IBufferedInputStream.
 *
 * @version $Id: IBufferedInputStream.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class IBufferedInputStream
	extends BufferedInputStream
{
	/**
	 * Creates a IBufferedInputStream and saves its argument, the input stream in, for later use.
	 */
	public IBufferedInputStream (InputStream in)
	{
		super(in);
	}

	/**
	 * Creates a BufferedInputStream with the specified buffer size, and saves
	 * its argument, the input stream in, for later use.
	 */
	public IBufferedInputStream (InputStream in, int size)
	{
		super(in, size);
	}
}