/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.key;



/**
 * @version $Id: UserCheckKeyLicense.java,v 1.1 2005/04/24 18:10:48 grappendorf Exp $
 */
public class UserCheckKeyLicense
{
	private String licensingName = new String();
	private String key = new String();
	private int onePlace;
	private int decade;
	private int hundredPlace;
	private int thousandPlace;
	private boolean check = false;
	public static int LICENSE = 0;

	public UserCheckKeyLicense ()
	{
	}

	public void setLicensingName (String name)
	{
		this.licensingName = name;
	}

	public void setKey (String key)
	{
		this.key = key;
	}

	public boolean getCheck ()
	{
		return check;
	}

	public String getKey ()
	{
		return this.key;
	}

	private int getCrossSum (int value)
	{
		int y = 0;

		while (value != 0)
		{
			y += value % 10;
			value = value / 10;
		}

		return y;
	}

	private String trimSpace (String name)
	{
		char[] chars = name.toCharArray ();
		StringBuffer after = new StringBuffer(name.length ());

		for (int i = 0; i < chars.length; i++)
		{
			if (! Character.isWhitespace (chars[i]))
			{
				after.append (chars[i]);
			}
		}

		return after.toString ();
	}

	public void setKeyClear ()
	{
		this.key = "";
		this.licensingName = "";
	}

	public int giveNumberOfLicense (String name, String key)
	{
		int result = 0;
		int crossSum = 0;
		String trimName = name;

		if (trimSpace (name).length () < 10)
		{
			for (int i = 0; i < (10 - trimSpace (trimName).length ()); i++)
			{
				name = name.concat ("d");
				System.out.println (name);
				System.out.println (name);
			}
		}

		name = trimSpace (name);

		String tempName = new String();

		tempName = tempName.concat (key.substring (6, 8) + name.substring (2, 3));
		tempName = tempName.concat (key.substring (15) + name.substring (4, 5));
		tempName = tempName.concat (key.substring (14, 15) + name.substring (6, 8));
		tempName = tempName.concat (key.substring (1, 2) + key.substring (0, 1));

		int tempNumber1 = 0;
		int tempNumber2 = 0;
		String tempKey1 = new String();
		String tempKey2 = new String();

		tempKey1 = key.substring (2, 3);
		tempKey2 = key.substring (3, 4);

		try
		{
			tempNumber1 = Integer.parseInt (tempKey1);
			tempNumber2 = Integer.parseInt (tempKey2);
		}
		catch (Exception e)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		crossSum += tempNumber1 + tempNumber2;

		if ((tempNumber1 + tempNumber2) > 9)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		result = result + ((tempNumber1 + tempNumber2) * 1000);

		tempKey1 = key.substring (4, 5);
		tempKey2 = key.substring (5, 6);

		try
		{
			tempNumber1 = Integer.parseInt (tempKey1);
			tempNumber2 = Integer.parseInt (tempKey2);
		}
		catch (Exception e)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		crossSum += tempNumber1 + tempNumber2;

		if ((tempNumber1 + tempNumber2) > 9)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		result = result + ((tempNumber1 + tempNumber2) * 100);

		tempKey1 = key.substring (12, 13);
		tempKey2 = key.substring (13, 14);

		try
		{
			tempNumber1 = Integer.parseInt (tempKey1);
			tempNumber2 = Integer.parseInt (tempKey2);
		}
		catch (Exception e)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		crossSum += tempNumber1 + tempNumber2;

		if ((tempNumber1 + tempNumber2) > 9)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		result = result + ((tempNumber1 + tempNumber2) * 10);

		tempKey1 = key.substring (10, 11);
		tempKey2 = key.substring (11, 12);

		try
		{
			tempNumber1 = Integer.parseInt (tempKey1);
			tempNumber2 = Integer.parseInt (tempKey2);
		}
		catch (Exception e)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		crossSum += tempNumber1 + tempNumber2;

		if ((tempNumber1 + tempNumber2) > 9)
		{
			tempNumber1 = 0;
			tempNumber2 = 0;
		}

		result = result + (tempNumber1 + tempNumber2);

		int crossSumEntry = 0;

		try
		{
			crossSumEntry = Integer.parseInt (key.substring (8, 10));
		}
		catch (Exception e)
		{
			crossSumEntry = 712;
		}

		if (tempName.equals (name.substring (0, 10)) && (crossSum == crossSumEntry))
		{
			check = true;
		}
		else
		{
			check = false;
			result = 0;
		}

		return result;
	}

	public void setLicense (int number)
	{
		LICENSE = number;
	}
}