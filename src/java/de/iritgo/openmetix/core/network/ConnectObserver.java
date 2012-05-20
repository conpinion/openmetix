/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.core.network;



/**
 * ConnnectObserver.
 *
 * @version $Id: ConnectObserver.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public interface ConnectObserver
{
	/**
	 * Observer method.
	 *
	 * @throws Exception
	 */
	public void notice ()
		throws Exception;
}