/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.iobject;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Proxy for <code>IObjects</code>.
 *
 * @version $Id: IObjectProxy.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectProxy
	extends BaseObject
	implements IObject
{
	/** The real object. */
	public IObject object;

	/** Initialization status. */
	public static boolean initState = false;

	/** True if the object is up to date. */
	private boolean upToDate = initState;

	/** True if an unpdate is currently running. */
	private boolean updateRunning = false;

	/**
	 * Create a new IObjectProxy.
	 */
	public IObjectProxy ()
	{
		super("prototypeproxy");
	}

	/**
	 * Create a new IObjectProxy.
	 *
	 * @param object The real object.
	 */
	public IObjectProxy (IObject object)
	{
		this();
		this.object = object;
		this.uniqueId = object.getUniqueId ();
	}

	/**
	 * Create a new IObjectProxy.
	 *
	 * @return A new IObjectProxy.
	 */
	public IObject create ()
	{
		return new IObjectProxy();
	}

	/**
	 * Create a new IObjectProxy.
	 *
	 * @return A new IObjectProxy.
	 */
	public IObjectProxy createProxy ()
	{
		return new IObjectProxy();
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *   instantiated.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		readObject (new DataInputStream(stream));
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream to write to.
	 * @throws IOException In case of a write error.
	 */
	public void writeObject (OutputStream stream)
		throws IOException
	{
		writeObject (new DataOutputStream(stream));
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The data input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *   instantiated.
	 */
	public void readObject (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The odata utput stream to write to.
	 * @throws IOException In case of a write error.
	 */
	public void writeObject (DataOutputStream stream)
		throws IOException
	{
	}

	/**
	 * Update the object attributes from a data input stream.
	 *
	 * @param stream The data input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *   instantiated.
	 */
	public void update (DataInputStream stream)
		throws IOException, ClassNotFoundException
	{
		setUpToDate (true);
		updateRunning = false;
		object.readObject (stream);

		Engine.instance ().getProxyEventRegistry ().fire (
			object, new IObjectProxyEvent(object, uniqueId, false));

		Engine.instance ().getEventRegistry ().fire (
			"proxyisuptodate", new IObjectProxyEvent(object, uniqueId, false));
	}

	/**
	 * Set the up-to-date flag.
	 *
	 * @param upToDate If true, the proxy object is up-to-date.
	 */
	public void setUpToDate (boolean upToDate)
	{
		this.upToDate = upToDate;
	}

	/**
	 * Check wether the proxy object is up to date or not.
	 *
	 * @param return True if the proxy object is up-to-date.
	 */
	public boolean isUpToDate ()
	{
		return upToDate;
	}

	/**
	 * Set the real object.
	 *
	 * @param object The real object.
	 */
	public void setRealObject (IObject object)
	{
		this.object = object;
		setUniqueId (object.getUniqueId ());
		setUpToDate (true);
	}

	/**
	 * Set an example real object.
	 *
	 * @param object The object example.
	 */
	public void setSampleRealObject (IObject object)
	{
		if (object.getUniqueId () == 0)
		{
			Log.logFatal (
				"system", "IObjectProxy", "Tried to set a sample object with uniqueId = 0");

			return;
		}

		this.object = object;
		setUniqueId (object.getUniqueId ());
		setUpToDate (false);
	}

	/**
	 * Get the real object.
	 *
	 * @return The real object.
	 */
	public IObject getRealObject ()
	{
		if ((! isUpToDate ()) && (getUniqueId () > 0))
		{
			reset ();
		}

		return object;
	}

	/**
	 * Get the sample real object.
	 *
	 * @return The sample real object.
	 */
	public IObject getSampleRealObject ()
	{
		return object;
	}

	/**
	 * Reset the proxy and try to get a new real object.
	 */
	public void reset ()
	{
		if (updateRunning)
		{
			return;
		}

		setUpToDate (false);
		updateRunning = true;

		Engine.instance ().getEventRegistry ().fire (
			"proxyupdate", new IObjectProxyEvent(object, uniqueId, true));
		Engine.instance ().getProxyEventRegistry ().fire (
			object, new IObjectProxyEvent(object, uniqueId, true));
	}
}