/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.framework.base;


import java.util.Map;


/**
 * DataObjectInterface.
 *
 * @version $Id: DataObjectInterface.java,v 1.1 2005/04/24 18:10:46 grappendorf Exp $
 */
public interface DataObjectInterface
{
	public Map getAttributes ();

	public void setAttributes (Map attributes);
}