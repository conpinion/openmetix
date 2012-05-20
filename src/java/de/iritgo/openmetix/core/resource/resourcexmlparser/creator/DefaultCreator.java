/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser.creator;


import de.iritgo.openmetix.core.resource.resourcexmlparser.BaseCreator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementContainer;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.NodeContainer;


/**
 * DefaultCreator.
 *
 * @version $Id: DefaultCreator.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class DefaultCreator
	extends BaseCreator
{
	CreateNode createNode;
	CallMethod callMethod;
	CreateDirNode createDirNode;

	public DefaultCreator ()
	{
		createNode = new CreateNode();
		callMethod = new CallMethod();
		createDirNode = new CreateDirNode();
	}

	public void work (NodeContainer nodeContainer, ElementIterator i)
		throws ContinueException
	{
		ElementContainer e = (ElementContainer) i.current ();

		super.work (nodeContainer, i);

		createNode.work (nodeContainer, i);

		callMethod.work (nodeContainer, i);

		createDirNode.work (nodeContainer, i);
	}
}