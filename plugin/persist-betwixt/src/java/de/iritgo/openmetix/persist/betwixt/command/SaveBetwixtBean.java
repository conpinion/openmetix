/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import de.iritgo.openmetix.framework.base.DataObject;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * This is a simple command to save a bean with betwixt.
 */
public class SaveBetwixtBean
	extends Command
{
	private Object bean;
	private String path;
	private String filename;

	public SaveBetwixtBean ()
	{
		super("savebetwixtbean");
	}

	public SaveBetwixtBean (Object bean)
	{
		this(
			bean, Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator (),
			bean.getClass ().getName ());
	}

	public SaveBetwixtBean (Object bean, String path)
	{
		this(bean, path, bean.getClass ().getName ());
	}

	public SaveBetwixtBean (Object bean, String path, String filename)
	{
		this.bean = bean;
		this.path = path;
		this.filename = filename;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
		bean = (Object) properties.get ("bean");

		path = (String) properties.get ("path");

		if (path == null)
		{
			path = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator ();
		}

		filename = (String) properties.get ("filename");

		if (filename == null)
		{
			filename = bean.getClass ().getName ();
		}
	}

	public void perform ()
	{
		BeanWriter writer = null;

		try
		{
			writer =
				new BeanWriter(new BufferedOutputStream(new FileOutputStream(path + filename)));
			writer.getXMLIntrospector ().setAttributesForPrimitives (true);
			writer.enablePrettyPrint ();
			writer.getBindingConfiguration ().setMapIDs (false);
			writer.write ((DataObject) bean);
		}
		catch (Exception x)
		{
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