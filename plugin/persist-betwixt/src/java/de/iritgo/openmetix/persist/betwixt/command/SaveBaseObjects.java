/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.base.BaseRegistry;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import de.iritgo.openmetix.persist.betwixt.baseobjectsave.BaseObjectMapping;
import de.iritgo.openmetix.persist.betwixt.baseobjectsave.Context;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;


/**
 * This command save the user and all objects in the baseregistry, but this is a fast simple way to do this. Later we need
 * a refactoring of this part.
 */
public class SaveBaseObjects
	extends Command
{
	private String path;

	public SaveBaseObjects ()
	{
		super("savebaseobjects");
		path =
			Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator () + "data" +
			Engine.instance ().getFileSeparator ();
	}

	public SaveBaseObjects (String path)
	{
		this();
		this.path = path;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
		path = (String) properties.get ("path");

		if (path == null)
		{
			path = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator ();
		}
	}

	public void perform ()
	{
		BeanWriter writer = null;

		BaseRegistry baseRegistry = Engine.instance ().getBaseRegistry ();
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			try
			{
				User user = (User) i.next ();

				writer =
					new BeanWriter(
						new BufferedOutputStream(
							new FileOutputStream(
								path + user.getTypeId () + "." + user.getUniqueId ())));

				writer.getXMLIntrospector ().setAttributesForPrimitives (true);
				writer.enablePrettyPrint ();
				writer.getBindingConfiguration ().setMapIDs (false);

				writer.write (user);
				writer.close ();
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}
		}

		for (Iterator i = baseRegistry.iterator (); i.hasNext ();)
		{
			try
			{
				BaseObject baseObject = (BaseObject) i.next ();

				if (baseObject instanceof DataObject)
				{
					DataObject dataObject = (DataObject) baseObject;

					writer =
						new BeanWriter(
							new BufferedOutputStream(
								new FileOutputStream(
									path + baseObject.getTypeId () + "." +
									baseObject.getUniqueId ())));
					writer.getXMLIntrospector ().setAttributesForPrimitives (true);
					writer.enablePrettyPrint ();
					writer.getBindingConfiguration ().setMapIDs (false);

					BaseObjectMapping save = new BaseObjectMapping();

					for (Iterator j = dataObject.getAttributes ().keySet ().iterator ();
						j.hasNext ();)
					{
						String key = (String) j.next ();
						Object object = dataObject.getAttribute (key);

						if (object instanceof Integer)
						{
							save.addInteger (new Context(key, String.valueOf (object)));
						}

						if (object instanceof Long)
						{
							save.addLong (new Context(key, String.valueOf (object)));
						}

						if (object instanceof Double)
						{
							save.addDouble (new Context(key, String.valueOf (object)));
						}

						if (object instanceof String)
						{
							save.addString (new Context(key, (String) object));
						}

						if (object instanceof IObjectList)
						{
							IObjectList proxyLinkedList = (IObjectList) object;
							Context context =
								new Context(
									key, proxyLinkedList.getOwner ().getTypeId (),
									String.valueOf (proxyLinkedList.getOwner ().getUniqueId ()));

							for (Iterator l = proxyLinkedList.iterator (); l.hasNext ();)
							{
								BaseObject pObject = (BaseObject) l.next ();

								context.addChild (
									new Context(
										pObject.getTypeId (),
										String.valueOf (pObject.getUniqueId ())));
							}

							save.addProxyLinkedList (context);
						}

						if (object instanceof DataObject)
						{
							save.addDataObject (new Context(key, String.valueOf (object)));
						}
					}

					save.setId (dataObject.getTypeId ());
					save.setUniqueId (dataObject.getUniqueId ());

					writer.write (save);
				}
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}

			try
			{
				writer.close ();
			}
			catch (Exception x)
			{
			}
		}
	}
}