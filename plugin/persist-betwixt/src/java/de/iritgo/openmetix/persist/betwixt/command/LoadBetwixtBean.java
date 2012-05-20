/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.command;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.command.Command;
import org.apache.commons.betwixt.io.BeanReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class LoadBetwixtBean
	extends Command
{
	private Object bean;
	private String path;
	private String filename;

	public LoadBetwixtBean ()
	{
		super("loadbetwixtbean");
	}

	public LoadBetwixtBean (Object bean)
	{
		this(
			bean, Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator (),
			bean.getClass ().getName ());
	}

	public LoadBetwixtBean (Object bean, String path)
	{
		this(bean, path, bean.getClass ().getName ());
	}

	public LoadBetwixtBean (Object bean, String path, String filename)
	{
		this.bean = bean;
		this.path = path;
		this.filename = filename;
	}

	public void setProperties (Properties properties)
	{
		super.setProperties (properties);

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
		BufferedReader xmlReader = null;

		try
		{
			xmlReader =
				new BufferedReader(new InputStreamReader(new FileInputStream(path + filename)));

			BeanReader beanReader = new BeanReader();

			beanReader.getXMLIntrospector ().setAttributesForPrimitives (true);
			beanReader.getBindingConfiguration ().setMapIDs (false);

			beanReader.registerBeanClass (bean.getClass ());

			properties.put ("bean", beanReader.parse (xmlReader));
		}
		catch (Exception x)
		{
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