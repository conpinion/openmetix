/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.command.CommandTools;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * DataObject.
 *
 * @version $Id: DataObject.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DataObject
	extends BaseObject
	implements IObject, DataObjectInterface
{
	private Map attributes;

	public DataObject (String typeId)
	{
		super(typeId);
		attributes = new LinkedHashMap();
	}

	public Map getAttributes ()
	{
		return attributes;
	}

	public void setAttributes (Map attributes)
	{
		this.attributes = attributes;
	}

	public void putAttribute (String name, Object object)
	{
		attributes.put (name, object);
	}

	public void putAttribute (String name, float value)
	{
		attributes.put (name, new Float(value));
	}

	public void putAttribute (String name, int value)
	{
		attributes.put (name, new Integer(value));
	}

	public void putAttribute (String name, boolean value)
	{
		attributes.put (name, new Boolean(value));
	}

	public void putAttribute (String name, double value)
	{
		attributes.put (name, new Double(value));
	}

	public void putAttribute (String name, long value)
	{
		attributes.put (name, new Long(value));
	}

	public void putAttribute (String name, DataObject object)
	{
		attributes.put (name, object);
	}

	public void putAttribute (String name, Date object)
	{
		putAttribute (name, object.getTime ());
	}

	public Object getAttribute (String name)
	{
		return attributes.get (name);
	}

	public Date getDateAttribute (String name)
	{
		return new Date(((Long) attributes.get (name)).longValue ());
	}

	public int getIntAttribute (String name)
	{
		return ((Integer) attributes.get (name)).intValue ();
	}

	public boolean getBooleanAttribute (String name)
	{
		return ((Boolean) attributes.get (name)).booleanValue ();
	}

	public String getStringAttribute (String name)
	{
		return (String) attributes.get (name);
	}

	public double getDoubleAttribute (String name)
	{
		return ((Double) attributes.get (name)).doubleValue ();
	}

	public long getLongAttribute (String name)
	{
		return ((Long) attributes.get (name)).longValue ();
	}

	public IObjectList getIObjectListAttribute (String name)
	{
		return ((IObjectList) attributes.get (name));
	}

	public DataObject getDataObjectAttribute (String name)
	{
		return (DataObject) attributes.get (name);
	}

	public void readObject (InputStream stream)
		throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		uniqueId = dataStream.readLong ();

		for (Iterator i = attributes.keySet ().iterator (); i.hasNext ();)
		{
			String key = (String) i.next ();

			Object object = attributes.get (key);

			if (object instanceof Integer)
			{
				putAttribute (key, new Integer(dataStream.readInt ()));
			}

			if (object instanceof String)
			{
				putAttribute (key, new String(dataStream.readUTF ()));
			}

			if (object instanceof Long)
			{
				putAttribute (key, new Long(dataStream.readLong ()));
			}

			if (object instanceof Float)
			{
				putAttribute (key, new Float(dataStream.readFloat ()));
			}

			if (object instanceof Double)
			{
				putAttribute (key, new Double(dataStream.readDouble ()));
			}

			if (object instanceof Boolean)
			{
				putAttribute (key, new Boolean(dataStream.readBoolean ()));
			}

			if (object instanceof IObjectList)
			{
				((IObjectList) object).readObject (stream);
			}

			if (object instanceof DataObject)
			{
				((DataObject) object).readObject (stream);
			}
		}
	}

	public void writeObject (OutputStream stream)
		throws IOException
	{
		int numAtt;

		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeLong (uniqueId);

		for (Iterator i = attributes.keySet ().iterator (); i.hasNext ();)
		{
			String key = (String) i.next ();

			Object object = attributes.get (key);

			if (object instanceof Integer)
			{
				writeAttribute (dataStream, (Integer) object);
			}

			if (object instanceof String)
			{
				writeAttribute (dataStream, (String) object);
			}

			if (object instanceof Long)
			{
				writeAttribute (dataStream, (Long) object);
			}

			if (object instanceof Float)
			{
				writeAttribute (dataStream, (Float) object);
			}

			if (object instanceof Double)
			{
				writeAttribute (dataStream, (Double) object);
			}

			if (object instanceof Boolean)
			{
				writeAttribute (dataStream, (Boolean) object);
			}

			if (object instanceof IObjectList)
			{
				writeAttribute (dataStream, (IObjectList) object);
			}

			if (object instanceof DataObject)
			{
				writeAttribute (dataStream, (DataObject) object);
			}
		}
	}

	private void writeAttribute (DataOutputStream stream, Integer integer)
		throws IOException
	{
		stream.writeInt (integer.intValue ());
	}

	private void writeAttribute (DataOutputStream stream, String string)
		throws IOException
	{
		stream.writeUTF (string);
	}

	private void writeAttribute (DataOutputStream stream, Long longAttribute)
		throws IOException
	{
		stream.writeLong (longAttribute.longValue ());
	}

	private void writeAttribute (DataOutputStream stream, Float floatAttribute)
		throws IOException
	{
		stream.writeFloat (floatAttribute.floatValue ());
	}

	private void writeAttribute (DataOutputStream stream, Double doubleAttribute)
		throws IOException
	{
		stream.writeDouble (doubleAttribute.doubleValue ());
	}

	private void writeAttribute (DataOutputStream stream, Boolean booleanAttribute)
		throws IOException
	{
		stream.writeBoolean (booleanAttribute.booleanValue ());
	}

	private void writeAttribute (OutputStream stream, IObjectList proxyLinkedList)
		throws IOException
	{
		proxyLinkedList.writeObject (stream);
	}

	private void writeAttribute (OutputStream stream, DataObject dataObject)
		throws IOException
	{
		dataObject.writeObject (stream);
	}

	public IObject create ()
	{
		try
		{
			IObject newProt = (IObject) this.getClass ().newInstance ();

			newProt.setTypeId (getTypeId ());

			return newProt;
		}
		catch (Exception x)
		{
			Log.log (
				"system", "DataObject:create", "Cannot create class: " + toString (), Log.FATAL);
		}

		return null;
	}

	public void update ()
	{
		CommandTools.performSimple (
			new de.iritgo.openmetix.framework.client.command.EditPrototype(this));
	}

	public boolean isValid ()
	{
		if (uniqueId == 0)
		{
			return false;
		}

		IObjectProxy iObjectProxy =
			Engine.instance ().getProxyRegistry ().getProxy (getUniqueId ());

		return iObjectProxy == null ? false : iObjectProxy.isUpToDate ();
	}

	public void waitForTransfer ()
	{
		while (true)
		{
			if (
				((IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (getUniqueId ())).isUpToDate ())
			{
				break;
			}

			try
			{
				Thread.sleep (100);
			}
			catch (InterruptedException x)
			{
			}
		}
	}
}