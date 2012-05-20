/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.baseobjectsave;


import java.util.LinkedList;
import java.util.List;


public class Context
{
	private String id;
	private String string;
	private String proxyLinkedListKey;
	private List childs;

	public Context ()
	{
		childs = new LinkedList();
	}

	public Context (String id, String string)
	{
		this();
		this.id = id;
		this.string = string;
	}

	public Context (String proxyLinkedListKey, String id, String string)
	{
		this();
		this.proxyLinkedListKey = proxyLinkedListKey;
		this.id = id;
		this.string = string;
	}

	public String getId ()
	{
		return id;
	}

	public void setProxyLinkedListKey (String proxyLinkedListKey)
	{
		this.proxyLinkedListKey = proxyLinkedListKey;
	}

	public String getProxyLinkedListKey ()
	{
		return proxyLinkedListKey;
	}

	public void setId (String id)
	{
		this.id = id;
	}

	public String getString ()
	{
		if (string == null)
		{
			return "";
		}

		return string;
	}

	public void setString (String string)
	{
		this.string = string;
	}

	public List getChilds ()
	{
		return childs;
	}

	public void addChild (Context context)
	{
		childs.add (context);
	}
}