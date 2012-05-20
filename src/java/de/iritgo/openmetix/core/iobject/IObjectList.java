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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * A list of iObjects.
 *
 * @version $Id: IObjectList.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
 */
public class IObjectList
	extends LinkedList
	implements IObject
{
	/** The object proxy. */
	private IObjectProxy proxy;

	/** The prototype map. */
	private Map proxyPrototypeMapping;

	/** The object that owns this list. */
	private IObject owner;

	/** The name of the list attribute in the enclosing object. */
	private String attributeName;

	/** The uniqueId of the object. */
	protected long uniqueId;

	/**
	 * Create a new object list.
	 *
	 * @param attributeName The name of the list attribute in the enclosing
	 *   object.
	 * @param proxy
	 * @param owner The owner of this list.
	 */
	public IObjectList (String attributeName, IObjectProxy proxy, IObject owner)
	{
		this.attributeName = attributeName;
		this.proxy = proxy;
		proxyPrototypeMapping = new HashMap();
		this.owner = owner;
	}

	/**
	 * Get the type id of the iritgo object.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return "IObjectList";
	}

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param typeId The new type
	 */
	public void setTypeId (String id)
	{
	}

	/**
	 * Get the id of the iritgo object.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId ()
	{
		return uniqueId;
	}

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param typeId The new type
	 */
	public void setUniqueId (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the the owner of this list.
	 *
	 * @return The list owner.
	 */
	public IObject getOwner ()
	{
		return owner;
	}

	/**
	 * Get the attribute name of this list (as specified in the
	 * owning object).
	 *
	 * @return attributeName The name of the list attribute in the enclosing
	 *   object.
	 */
	public String getAttributeName ()
	{
		return attributeName;
	}

	private IObjectProxy createListProxy (Object object)
	{
		IObjectProxy tmpProxy = null;

		IObject prototype =
			(IObject) Engine.instance ().getBaseRegistry ().get (((IObject) object).getUniqueId ());

		IObjectProxy existsProxy =
			(IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (
				((IObject) object).getUniqueId ());

		if (existsProxy == null)
		{
			prototype = (IObject) object;

			if (prototype.getUniqueId () == 0)
			{
				prototype.setUniqueId (
					(Engine.instance ().getPersistentIDGenerator ().createId () * -1));
			}

			IObjectProxy clonedProxy = (IObjectProxy) proxy.createProxy ();

			clonedProxy.setSampleRealObject ((IObject) prototype);
			Engine.instance ().getBaseRegistry ().add ((BaseObject) prototype);
			Engine.instance ().getProxyRegistry ().addProxy (clonedProxy);
			tmpProxy = clonedProxy;

			Engine.instance ().getEventRegistry ().fire (
				"proxylinkedlistupdate",
				new IObjectListEvent(prototype, owner, attributeName, IObjectListEvent.ADD));
		}
		else
		{
			tmpProxy = existsProxy;
		}

		proxyPrototypeMapping.put (prototype, tmpProxy);

		return tmpProxy;
	}

	/**
	 * Add an element to this list.
	 *
	 * @param object The object to add.
	 */
	synchronized public boolean add (Object object)
	{
		IObject prototype = (IObject) object;

		boolean createdByClient = prototype.getUniqueId () <= 0;

		if (createdByClient)
		{
			createListProxy (object);

			return true;
		}

		return super.add (createListProxy (object));
	}

	/**
	 * Remove an element from this list.
	 *
	 * @param object The object to remove.
	 */
	public boolean remove (Object object)
	{
		if (object == null)
		{
			return false;
		}

		Engine.instance ().getEventRegistry ().fire (
			"proxylinkedlistupdate",
			new IObjectListEvent((IObject) object, owner, attributeName, IObjectListEvent.REMOVE));

		return true;
	}

	/**
	 * Remove an iobject from this list. It will only use form a action, don't use it from the client.
	 *
	 * @param IObject The iObject to remove.
	 */
	public boolean removeIObject (IObject iObject)
	{
		IObjectProxy proxy = (IObjectProxy) proxyPrototypeMapping.get (iObject);

		if (proxy == null)
		{
			Log.logFatal (
				"system", "ProxyLinkedList.removeIObject",
				"That object '" + iObject +
				" is to remove, but no mapping exists in this iObjectList!");

			return false;
		}

		proxyPrototypeMapping.remove (proxy);

		return super.remove (proxy);
	}

	/**
	 * Retrieve an element of this list at a specific position.
	 *
	 * @param index The index of the element to retrieve.
	 * @return The element at the specified index.
	 */
	public Object get (int index)
	{
		IObjectProxy proxy = (IObjectProxy) super.get (index);

		return proxy.getRealObject ();
	}

	/**
	 * Get an iterator over all real elements in this list.
	 *
	 * @return An element iterator.
	 */
	synchronized public Iterator iterator ()
	{
		return new IObjectIterator(this);
	}

	/**
	 * Get an iterator over all elements in this list.
	 *
	 * @return An element iterator.
	 */
	public Iterator getListIterator ()
	{
		return super.iterator ();
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		int size = dataStream.readInt ();

		int curSize = super.size ();

		if (size >= curSize)
		{
			for (int i = 0; i < curSize; ++i)
			{
				String id = dataStream.readUTF ();
				long uniqueId = dataStream.readLong ();
			}

			int newRecords = size - curSize;

			for (int i = 0; i < newRecords; ++i)
			{
				String id = dataStream.readUTF ();
				long uniqueId = dataStream.readLong ();
				IObject prototype = null;

				try
				{
					prototype = Engine.instance ().getIObjectFactory ().newInstance (id);
				}
				catch (NoSuchIObjectException x)
				{
					Log.log (
						"system", "ProxyLinkedList.readObject",
						"ProxyLinkedList - Prototype not found! DataObject not in plugin registed? : " +
						id, Log.WARN);
				}

				if (prototype == null)
				{
					prototype = proxy.getRealObject ().create ();
				}

				prototype.setUniqueId (uniqueId);

				super.add (createListProxy (prototype));
			}
		}
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	synchronized public void writeObject (OutputStream stream)
		throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		int size = super.size ();

		dataStream.writeInt (size);

		for (Iterator i = super.iterator (); i.hasNext ();)
		{
			IObjectProxy proxy = (IObjectProxy) i.next ();
			IObject prot = proxy.getSampleRealObject ();

			dataStream.writeUTF (prot.getTypeId ());
			dataStream.writeLong (proxy.getUniqueId ());
		}
	}

	/**
	 * Remove all elements from this list.
	 */
	public void clear ()
	{
		LinkedList tmpList = new LinkedList(this);

		for (Iterator i = tmpList.iterator (); i.hasNext ();)
		{
			remove (((IObjectProxy) i.next ()).getRealObject ());
		}
	}

	/**
	 * Create a instance of the iritgo object.
	 */
	public IObject create ()
	{
		return new IObjectList(attributeName, proxy, owner);
	}
}