/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource;


import de.iritgo.openmetix.core.base.BaseObject;
import java.util.Iterator;
import java.util.TreeMap;


/**
 * ResourceNode.
 *
 * @version $Id: ResourceNode.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class ResourceNode
	extends BaseObject
{
	/** Node description. */
	private String description = "no.description";

	/** Node name. */
	private String name;

	/** Node map. */
	private TreeMap nodeMap;

	/**
	 * Create a new ResourceNode.
	 *
	 * @param description The description of the resource.
	 * @param nodeName The node name.
	 */
	public ResourceNode (String description, String nodeName)
	{
		this.description = description;
		this.name = nodeName;
	}

	/**
	 * Create a new ResourceNode.
	 *
	 * @param nodeName The node name.
	 */
	public ResourceNode (String nodeName)
	{
		this.name = nodeName;
	}

	/**
	 * Get the description of the resource.
	 *
	 * @return The description of the resource.
	 */
	public String getDescription ()
	{
		return description;
	}

	/**
	 * Get the tree node name.
	 *
	 * @return The tree node name.
	 */
	public String getName ()
	{
		return name;
	}

	/**
	 * Get the node by node name.
	 *
	 * @param nodeName
	 * @return The node.
	 */
	public ResourceNode getNodeByName (String nodeName)
	{
		if (nodeName.equals (this.name))
		{
			return this;
		}

		return nodeMap != null ? ((ResourceNode) nodeMap.get (nodeName)) : null;
	}

	/**
	 * Return the name of the resource.
	 *
	 * @return The node.
	 */
	public String getValue ()
	{
		return name;
	}

	/**
	 * Add a child to this node.
	 */
	public void addNode (ResourceNode node)
	{
		nodeMapOnDemand ();
		nodeMap.put (node.getName (), node);
	}

	/**
	 * Create the node map on demand.
	 */
	public void nodeMapOnDemand ()
	{
		if (nodeMap == null)
		{
			nodeMap = new TreeMap();
		}
	}

	/**
	 * Return an iterator over all node keys.
	 *
	 * @return The iterator.
	 */
	public Iterator getKeyIterator ()
	{
		return nodeMap.keySet ().iterator ();
	}

	/**
	 * Return an iterator over all nodes.
	 *
	 * @return The iterator.
	 */
	public Iterator getNodeIterator ()
	{
		return nodeMap.values ().iterator ();
	}
}