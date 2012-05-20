/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.resource.resourcexmlparser.creator;


import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.ResourceNode;
import de.iritgo.openmetix.core.resource.ResourceService;
import de.iritgo.openmetix.core.resource.resourcexmlparser.BaseCreator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementContainer;
import de.iritgo.openmetix.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.openmetix.core.resource.resourcexmlparser.NodeContainer;
import org.jdom.Element;


/**
 * CreateDirNode.
 *
 * @version $Id: CreateDirNode.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public class CreateDirNode
	extends BaseCreator
{
	public CreateDirNode ()
	{
	}

	public void work (NodeContainer nodeContainer, ElementIterator i)
		throws ContinueException
	{
		ResourceNode node = (ResourceNode) nodeContainer.getNode ();

		Element element = ((ElementContainer) i.current ()).getElement ();
		String path = ((ElementContainer) i.current ()).getPath ();

		String tagName = element.getName ();
		String body = element.getTextTrim ();

		if (tagName.equals ("node"))
		{
			ResourceService resourceService = new ResourceService((ResourceNode) node);

			node = new ResourceNode("directory", element.getAttribute ("treename").getValue ());
			resourceService.addNode ("", (ResourceNode) node);
			Log.log (
				"resource", "[XMLParser] CreateDirNode.work",
				"create directory node '" + element.getAttribute ("treename").getValue () + "'.",
				Log.INFO);
			nodeContainer.setNode (node);

			throw new ContinueException();
		}
	}
}