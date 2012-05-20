/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.persist.betwixt.baseobjectsave;


import java.util.LinkedList;
import java.util.List;


public class BaseObjectMapping
{
	private String id;
	private long uniqueId;
	private List integers;
	private List longs;
	private List doubles;
	private List strings;
	private List dataObjects;
	private List proxyLinkedLists;

	public BaseObjectMapping ()
	{
		integers = new LinkedList();
		longs = new LinkedList();
		doubles = new LinkedList();
		strings = new LinkedList();
		dataObjects = new LinkedList();
		proxyLinkedLists = new LinkedList();
	}

	public void setId (String id)
	{
		this.id = id;
	}

	public String getId ()
	{
		return id;
	}

	public void setUniqueId (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	public long getUniqueId ()
	{
		return uniqueId;
	}

	public List getIntegers ()
	{
		return integers;
	}

	public void addInteger (Context context)
	{
		integers.add (context);
	}

	public List getLongs ()
	{
		return longs;
	}

	public void addLong (Context context)
	{
		longs.add (context);
	}

	public List getDoubles ()
	{
		return doubles;
	}

	public void addDouble (Context context)
	{
		doubles.add (context);
	}

	public List getStrings ()
	{
		return strings;
	}

	public void addString (Context context)
	{
		strings.add (context);
	}

	public List getDataObjects ()
	{
		return dataObjects;
	}

	public void addDataObject (Context context)
	{
		dataObjects.add (context);
	}

	public List getProxyLinkedLists ()
	{
		return proxyLinkedLists;
	}

	public void addProxyLinkedList (Context context)
	{
		proxyLinkedLists.add (context);
	}
}