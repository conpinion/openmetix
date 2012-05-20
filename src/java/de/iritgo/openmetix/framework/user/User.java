/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.user;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * User.
 *
 * @version $Id: User.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class User
	extends BaseObject
	implements IObject
{
	/** User name. */
	private String userName;

	/** Network channel. */
	private double networkChannel;

	/** User email. */
	private String email;

	/** User password. */
	private String password;

	/** Online status. */
	private boolean online;

	/** Name objects. */
	private Map namedObjectIds;

	/** New named objects. */
	private Map newObjectsMapping;

	/** Log out time. */
	private Date loggedOutDate = new Date();

	/**
	 * Create a new user.
	 */
	public User ()
	{
		super("user");
		namedObjectIds = new HashMap();
		newObjectsMapping = new HashMap();
	}

	/**
	 * Create a new user.
	 */
	public User (String userName, String email, long id, String password, double networkChannel)
	{
		this();
		this.uniqueId = id;
		this.email = email;
		this.userName = userName;
		this.networkChannel = networkChannel;
		this.password = password;
	}

	/**
	 * Get the type id.
	 */
	public String getTypeId ()
	{
		return "user";
	}

	/**
	 * Set the online status.
	 */
	public void setOnline (boolean online)
	{
		this.online = online;
	}

	/**
	 * Get the online status.
	 */
	public boolean getOnline ()
	{
		return online;
	}

	/**
	 * Is the User Online?
	 *
	 * @return True for an online user.
	 */
	public boolean isOnline ()
	{
		return online;
	}

	/**
	 * Set the log out time.
	 */
	public void setLoggedOutDate (Date loggedOutDate)
	{
		this.loggedOutDate = loggedOutDate;
	}

	/**
	 * Get the log out time.
	 */
	public Date getLoggedOutDate ()
	{
		return loggedOutDate;
	}

	/**
	 * Get a timestamp.
	 */
	public double getTimeStamp ()
	{
		return 0.0;
	}

	/**
	 * Set the timestamp.
	 */
	public void setTimeStamp (double timeStamp)
	{
	}

	/**
	 * Create an instance of an iritgo object.
	 */
	public IObject create ()
	{
		return new User();
	}

	/**
	 * Get a named object id.
	 */
	public long getNamedIritgoObjectId (String name)
		throws NoSuchIObjectException
	{
		Long id = (Long) namedObjectIds.get (name);

		if (id == null)
		{
			String msg = "Unable to find object '" + name + "'";

			Log.logError ("system", "User.getNamedIritgoObjectId", msg);
			throw new NoSuchIObjectException(msg);
		}

		return id.longValue ();
	}

	/**
	 * Get a named object.
	 */
	public IObject getNamedIritgoObject (String name, Class klass)
		throws NoSuchIObjectException
	{
		long id = getNamedIritgoObjectId (name);
		IObject prototypeable = (IObject) Engine.instance ().getBaseRegistry ().get (id);

		if (prototypeable == null)
		{
			try
			{
				prototypeable = (IObject) klass.newInstance ();
				prototypeable.setUniqueId (id);
				Engine.instance ().getBaseRegistry ().add ((BaseObject) prototypeable);

				FrameworkProxy proxy = new FrameworkProxy(prototypeable);

				Engine.instance ().getProxyRegistry ().addProxy (proxy);
				proxy.reset ();
			}
			catch (Exception x)
			{
				String msg = "Unable to create instance for object '" + name + "'";

				Log.logError ("system", "User.getNamedIritgoObject", msg);
				throw new NoSuchIObjectException(msg);
			}
		}

		return prototypeable;
	}

	/**
	 * Add a named object.
	 */
	public void putNamedIritgoObject (String name, IObject object)
	{
		putNamedIritgoObject (name, object.getUniqueId ());
	}

	/**
	 * Add a named object.
	 */
	public void putNamedIritgoObject (String name, long uniqueId)
	{
		namedObjectIds.put (name, new Long(uniqueId));
	}

	/**
	 * Remove a named object.
	 */
	public void removeNamedIritgoObject (String name)
	{
		namedObjectIds.remove (name);
	}

	/**
	 * Put a an object id.
	 */
	public void putNewObjectsMapping (Long id, Long newId)
	{
		newObjectsMapping.put (id, newId);
	}

	/**
	 * Get an object id.
	 */
	public Long getNewObjectsMapping (Long id)
	{
		return (Long) newObjectsMapping.get (id);
	}

	/**
	 * Remove an onbject id.
	 */
	public void removeNewObjectsMapping (Long id)
	{
		newObjectsMapping.remove (id);
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		userName = dataStream.readUTF ();
		networkChannel = dataStream.readDouble ();
		email = dataStream.readUTF ();
		password = dataStream.readUTF ();
		online = dataStream.readBoolean ();
		uniqueId = dataStream.readLong ();

		int numNamedObjects = dataStream.readInt ();

		for (int i = 0; i < numNamedObjects; ++i)
		{
			putNamedIritgoObject (dataStream.readUTF (), dataStream.readLong ());
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject (OutputStream stream)
		throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeUTF (userName);
		dataStream.writeDouble (networkChannel);
		dataStream.writeUTF (email);
		dataStream.writeUTF (password);
		dataStream.writeBoolean (online);
		dataStream.writeLong (uniqueId);

		int size = namedObjectIds.size ();

		dataStream.writeInt (size);

		for (Iterator i = namedObjectIds.entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry entry = (Map.Entry) i.next ();

			dataStream.writeUTF ((String) entry.getKey ());
			dataStream.writeLong (((Long) entry.getValue ()).longValue ());
		}
	}

	/**
	 * Set the user name.
	 */
	public void setName (String userName)
	{
		this.userName = userName;
	}

	/**
	 * Get the user name.
	 */
	public String getName ()
	{
		return userName;
	}

	/**
	 * Get the user password.
	 */
	public String getPassword ()
	{
		return password;
	}

	/**
	 * Set the user password.
	 */
	public void setPassword (String password)
	{
		this.password = password;
	}

	/**
	 * Get the user email.
	 */
	public String getEmail ()
	{
		return email;
	}

	/**
	 * Get the user email.
	 */
	public void setEmail (String email)
	{
		this.email = email;
	}

	/**
	 * Set the network channel.
	 */
	public void setNetworkChannel (double networkChannel)
	{
		this.networkChannel = networkChannel;
	}

	/**
	 * Get the network channel.
	 */
	public double getNetworkChannel ()
	{
		return networkChannel;
	}

	/**
	 * Get an iterator over all named objects.
	 *
	 * @return A named object iterator.
	 */
	public Iterator getNamedObjectIterator ()
	{
		return namedObjectIds.entrySet ().iterator ();
	}
}