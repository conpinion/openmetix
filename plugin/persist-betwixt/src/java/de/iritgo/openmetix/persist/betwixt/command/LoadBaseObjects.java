/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseRegistry;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectList;
import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.framework.base.DataObject;
import de.iritgo.openmetix.framework.base.FrameworkProxy;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserRegistry;
import de.iritgo.openmetix.persist.betwixt.baseobjectsave.BaseObjectMapping;
import de.iritgo.openmetix.persist.betwixt.baseobjectsave.Context;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


public class LoadBaseObjects
	extends Command
{
	private String path;

	public LoadBaseObjects ()
	{
		super("loadbaseobjects");
		path =
			Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator () + "data" +
			Engine.instance ().getFileSeparator ();
	}

	public LoadBaseObjects (String path)
	{
		this();
		this.path = path;
	}

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

		final File dataDir = new File(path);

		File[] dataFiles =
			dataDir.listFiles (
				new FilenameFilter()
				{
					public boolean accept (File dir, String name)
					{
						return true;
					}
				});

		if (dataFiles == null)
		{
			Log.log ("persist", "LoadBasicObjects", "No files to load in: " + path, Log.WARN);

			return;
		}

		for (int i = 0; i < dataFiles.length; ++i)
		{
			String filename = dataFiles[i].getName ();

			BufferedReader xmlReader = null;

			try
			{
				xmlReader =
					new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));

				BeanReader beanReader = new BeanReader();

				beanReader.getXMLIntrospector ().setAttributesForPrimitives (true);
				beanReader.getBindingConfiguration ().setMapIDs (false);

				beanReader.registerBeanClass (BaseObjectMapping.class);
				beanReader.registerBeanClass (User.class);

				BaseObjectMapping baseObjectMapping = null;

				Object object = (Object) beanReader.parse (xmlReader);

				if (object == null)
				{
					continue;
				}

				if (object instanceof User)
				{
					User user = (User) object;

					Server.instance ().getUserRegistry ().addUser ((User) object);
					Engine.instance ().getProxyRegistry ().addProxy (new FrameworkProxy(user));
					baseRegistry.add (user);
					xmlReader.close ();

					continue;
				}

				baseObjectMapping = (BaseObjectMapping) object;

				DataObject dataObject = null;

				try
				{
					dataObject =
						(DataObject) Engine.instance ().getIObjectFactory ().newInstance (
							baseObjectMapping.getId ());
					dataObject.setUniqueId (baseObjectMapping.getUniqueId ());
				}
				catch (NoSuchIObjectException x)
				{
					Log.log (
						"persist", "LoadBasicObects.perform",
						"DataObject not registred: " + baseObjectMapping.getId (), Log.FATAL);

					continue;
				}

				for (Iterator j = baseObjectMapping.getIntegers ().iterator (); j.hasNext ();)
				{
					Context context = (Context) j.next ();

					dataObject.putAttribute (context.getId (), new Integer(context.getString ()));
				}

				for (Iterator j = baseObjectMapping.getLongs ().iterator (); j.hasNext ();)
				{
					Context context = (Context) j.next ();

					dataObject.putAttribute (context.getId (), new Long(context.getString ()));
				}

				for (Iterator j = baseObjectMapping.getDoubles ().iterator (); j.hasNext ();)
				{
					Context context = (Context) j.next ();

					dataObject.putAttribute (context.getId (), new Double(context.getString ()));
				}

				for (Iterator j = baseObjectMapping.getStrings ().iterator (); j.hasNext ();)
				{
					Context context = (Context) j.next ();

					dataObject.putAttribute (context.getId (), context.getString ());
				}

				baseRegistry.add (dataObject);
				Engine.instance ().getProxyRegistry ().addProxy (new FrameworkProxy(dataObject));
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}

			try
			{
				xmlReader.close ();
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}
		}

		for (int i = 0; i < dataFiles.length; ++i)
		{
			String filename = dataFiles[i].getName ();

			BufferedReader xmlReader = null;

			try
			{
				xmlReader =
					new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));

				BeanReader beanReader = new BeanReader();

				beanReader.getXMLIntrospector ().setAttributesForPrimitives (true);
				beanReader.getBindingConfiguration ().setMapIDs (false);

				beanReader.registerBeanClass ("BaseObjectMapping", BaseObjectMapping.class);
				beanReader.registerBeanClass ("User", User.class);

				BaseObjectMapping baseObjectMapping = null;

				Object object = (Object) beanReader.parse (xmlReader);

				if (object == null)
				{
					continue;
				}

				if (object instanceof User)
				{
					continue;
				}

				baseObjectMapping = (BaseObjectMapping) object;

				DataObject dataObject =
					(DataObject) baseRegistry.get (
						new Long(baseObjectMapping.getUniqueId ()).longValue ());

				for (Iterator j = baseObjectMapping.getProxyLinkedLists ().iterator ();
					j.hasNext ();)
				{
					Context context = (Context) j.next ();
					IObject owner =
						(IObject) baseRegistry.get (
							new Long(baseObjectMapping.getUniqueId ()).longValue ());

					List list = (List) context.getChilds ();

					if (list.size () == 0)
					{
						xmlReader.close ();

						continue;
					}

					Context childContext = (Context) list.get (0);

					if (childContext == null)
					{
						continue;
					}

					IObjectList proxyLinkedList =
						dataObject.getIObjectListAttribute (context.getProxyLinkedListKey ());

					for (Iterator k = context.getChilds ().iterator (); k.hasNext ();)
					{
						childContext = (Context) k.next ();
						proxyLinkedList.add (
							baseRegistry.get (new Long(childContext.getString ()).longValue ()));
					}
				}
			}
			catch (Exception x)
			{
				x.printStackTrace ();
			}

			try
			{
				xmlReader.close ();
			}
			catch (Exception x)
			{
			}
		}
	}
}