/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser.creator;


import de.iritgo.openmetix.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.NodeContainer;


/**
 * NodeCreator.
 *
 * @version $Id: NodeCreator.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public interface NodeCreator
{
	public void work (NodeContainer node, ElementIterator i)
		throws ContinueException;
}