/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser;



/**
 * Node Contrainer
 *
 * @version $Id: NodeContainer.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class NodeContainer
{
	Object node;

	public NodeContainer ()
	{
	}

	public void setNode (Object node)
	{
		this.node = node;
	}

	public Object getNode ()
	{
		return node;
	}
}