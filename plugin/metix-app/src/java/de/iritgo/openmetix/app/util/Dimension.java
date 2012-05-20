/*
    This file is part of the OpenMetix Application.

    Copyright (C) 2004 lutix GmbH
    Copyright (C) 2005-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.app.util;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.logger.Log;
import de.iritgo.openmetix.core.resource.locale.TextResource;
import javax.swing.DefaultComboBoxModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;


/**
* This class models the concept of dimensions and units, e.g. a temperature
* measured in kelvin. Each dimension can contain a list of units which can
* represent the given dimension.
*
* Following convention is used for the unit and dimension keys:
*
* Dimension key:          metix.<<dimensionKey>>
* Dimension and unit key: metix.<<dimensionKey>>.<<unitKey>>
*
* You need to provide translation resources for every dimension and unit.
*
* @version $Id: Dimension.java,v 1.1 2005/04/24 18:10:42 grappendorf Exp $
*/
public class Dimension
{
	/** Map of all available Dimensions. */
	protected static Map dimensionMap = new HashMap();

	/** Unknown / Unbekannt. */
	public static Dimension UNKNOWN;
	public static Engine engine;

	/**
	 * Initialize all available dimensions.
	 */
	static
	{
		try
		{
			engine = Engine.instance ();

			String fileName =
				(engine.getSystemDir () + engine.getFileSeparator () + "dimension.txt");

			RandomAccessFile f = new RandomAccessFile(fileName, "r");
			String line;

			while ((line = f.readLine ()) != null)
			{
				String[] data = line.split ("#");
				Unit[] unitList = new Unit[10];

				for (int i = 0; i < data.length; i++)
				{
				}

				for (int i = 1; i < data.length; i++)
				{
					String[] subData = data[i].split (",");
					double value1 = Double.parseDouble (subData[1]);
					double value2 = Double.parseDouble (subData[2]);

					unitList[i - 1] = new Unit(subData[0], value1, value2);
				}

				new Dimension(data[0], unitList);
			}

			f.close ();
		}
		catch (Exception r)
		{
			Log.logError ("Dimension", "Error while reading dimension-file *******");
		}

		UNKNOWN = new Dimension("unknown", new Unit[]
				{
					new Unit("unknown", 0.00, 0.00),
				});
	}

	/**
	 * A Unit contains information about a specific unit of a
	 * dimension.
	 */
	public static class Unit
	{
		/** The resourcekey for the unit's name. */
		private String unitKey;

		/** Typical min value of the unit. */
		private double typicalMinValue;

		/** Typical max value of the unit. */
		private double typicalMaxValue;

		/** The dimension of this unit. */
		private Dimension dimension;

		/**
		 * Create a new Unit.
		 *
		 * @param unitKey The resource key of the unit name
		 * @param typicalMinValue The typical minimum value.
		 * @param typicalMaxValue The typical maximum value.
		 */
		public Unit (String unitKey, double typicalMinValue, double typicalMaxValue)
		{
			this.unitKey = unitKey;
			this.typicalMinValue = typicalMinValue;
			this.typicalMaxValue = typicalMaxValue;
		}

		/**
		 * Get the translated name of the unit.
		 *
		 * @return The translated unit name.
		 */
		public String getName ()
		{
			String resKey = "metix." + getDimension ().getDimensionKey () + "." + getUnitKey ();

			engine = Engine.instance ();

			String directory = (engine.getSystemDir () + engine.getFileSeparator ());

			TextResource textResource =
				(TextResource) Engine.instance ().getResourceService ().getResourceBundle ();

			textResource.loadFromFile (
				directory,
				"dimension" + "_" + textResource.getLocale ().getLanguage () + ".properties");

			try
			{
				return textResource.getString (resKey);
			}
			catch (MissingResourceException x)
			{
			}

			return "!" + resKey + "!";
		}

		/**
		 * Get the key of the unit.
		 *
		 * @return The unit key.
		 */
		public String getUnitKey ()
		{
			return unitKey;
		}

		/**
		 * Get the typical minimum value.
		 *
		 * @return The minimum value.
		 */
		public double getTypicalMinValue ()
		{
			return typicalMinValue;
		}

		/**
		 * Get the typical maximum value.
		 *
		 * @return maximum value.
		 */
		public double getTypicalMaxValue ()
		{
			return typicalMaxValue;
		}

		/**
		 * Set the dimension.
		 *
		 * @param dimension The new dimension.
		 */
		public void setDimension (Dimension dimension)
		{
			this.dimension = dimension;
		}

		/**
		 * Get the dimension.
		 *
		 * @return The dimension.
		 */
		public Dimension getDimension ()
		{
			return dimension;
		}

		/**
		 * Create a string representation of the Unit.
		 *
		 * @return The string representation.
		 */
		public String toString ()
		{
			return getDimension ().getName () + " [" + getName () + "]";
		}
	}

	/** The name of the dimension specified as a resource key. */
	private String dimensionKey;

	/** The possible units of the dimension. */
	private Unit[] units;

	/**
	 * Create a new Dimension.
	 *
	 * @param dimensionKey The name of the dimension as resource key.
	 * @param units Array of units.
	 */
	public Dimension (String dimensionKey, Unit[] units)
	{
		this.dimensionKey = dimensionKey;
		this.units = units;

		for (int i = 0; i < this.units.length; ++i)
		{
			if (units[i] != null)
			{
				this.units[i].setDimension (this);
			}
		}

		this.dimensionMap.put (dimensionKey, this);
	}

	/**
	 * Get the translated name of the dimension.
	 *
	 * @return The translated name.
	 */
	public String getName ()
	{
		String resKey = "metix." + getDimensionKey ();

		engine = Engine.instance ();

		String directory = (engine.getSystemDir () + engine.getFileSeparator ());

		TextResource textResource =
			(TextResource) Engine.instance ().getResourceService ().getResourceBundle ();

		textResource.loadFromFile (
			directory, "dimension" + "_" + textResource.getLocale ().getLanguage () +
			".properties");

		try
		{
			return textResource.getString (resKey);
		}
		catch (MissingResourceException x)
		{
		}

		return "!" + resKey + "!";
	}

	/**
	 * Get the key of the dimension.
	 *
	 * @return The dimension key.
	 */
	public String getDimensionKey ()
	{
		return dimensionKey;
	}

	/**
	 * Find a unit of this dimension from the given key.
	 *
	 * @param unitkey The unit key.
	 * @return The unit or UNKNOWN if no unit was found.
	 */
	public Dimension.Unit findUnit (String unitkey)
	{
		for (int i = 0; i < units.length; ++i)
		{
			if (units[i].getUnitKey ().equals (unitkey))
			{
				return units[i];
			}
		}

		return UNKNOWN.first ();
	}

	/**
	 * Get the first unit of the dimension.
	 *
	 * @return The first unit.
	 */
	public Dimension.Unit first ()
	{
		if (units.length == 0)
		{
			return UNKNOWN.first ();
		}

		return units[0];
	}

	/**
	 * Get all units of the dimension.
	 *
	 * @return The units.
	 */
	public Dimension.Unit[] getUnits ()
	{
		return units;
	}

	/**
	 * Find the dimension of the given key.
	 *
	 * @param key The key of the dimension.
	 * @return The dimension of the given key, or UNKNOWN.
	 */
	public static Dimension findDimension (String key)
	{
		Object o = dimensionMap.get (key);

		if (o == null)
		{
			return UNKNOWN;
		}

		return (Dimension) o;
	}

	/**
	 * Create a combobox model, filled with all available units.
	 *
	 * @return The combobox model.
	 */
	public static DefaultComboBoxModel createComboBoxModel ()
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel();

		reloadDimension ();

		for (Iterator dIter = dimensionMap.values ().iterator (); dIter.hasNext ();)
		{
			Dimension dimension = (Dimension) dIter.next ();

			for (int i = 0; i < dimension.getUnits ().length; ++i)
			{
				if (dimension.getUnits ()[i] != null)
				{
					model.addElement (dimension.getUnits ()[i]);
				}
			}
		}

		return model;
	}

	/**
	 * Create a combobox model, filled with all available units.
	 *
	 * @return The combobox model.
	 */
	public static DefaultComboBoxModel createComboBoxModelKey ()
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel();

		reloadDimension ();

		model.addElement ("----------");

		for (Iterator dIter = dimensionMap.values ().iterator (); dIter.hasNext ();)
		{
			Dimension dimension = (Dimension) dIter.next ();

			if (! dimension.getName ().equals ("?"))
			{
				model.addElement (dimension.getDimensionKey ());
			}
		}

		return model;
	}

	public static void reloadDimension ()
	{
		try
		{
			engine = Engine.instance ();

			String fileName =
				(engine.getSystemDir () + engine.getFileSeparator () + "dimension.txt");

			RandomAccessFile f = new RandomAccessFile(fileName, "r");
			String line;

			while ((line = f.readLine ()) != null)
			{
				String[] data = line.split ("#");
				Unit[] unitList = new Unit[10];

				for (int i = 1; i < data.length; i++)
				{
					String[] subData = data[i].split (",");
					double value1 = Double.parseDouble (subData[1]);
					double value2 = Double.parseDouble (subData[2]);

					unitList[i - 1] = new Unit(subData[0], value1, value2);
				}

				new Dimension(data[0], unitList);
			}

			f.close ();
		}
		catch (Exception r)
		{
			Log.logError ("Dimension", "Error while reading dimension-file *******");
		}
	}

	/**
	 * Add a new dimension and a new Unit.
	 *
	 * @param dimensionKey
	 * @param unitKey
	 * @param unitMin
	 * @param unitMax
	 */
	public static void addDimensionUnit (
		String dimensionKey, String unitKey, double unitMin, double unitMax)
	{
		try
		{
			engine = Engine.instance ();

			String fileName =
				(engine.getSystemDir () + engine.getFileSeparator () + "dimension.txt");

			RandomAccessFile f = new RandomAccessFile(fileName, "rw");
			String line;

			f.seek (f.length ());
			f.writeBytes (dimensionKey + "#" + unitKey + ", " + unitMin + ", " + unitMax + "\n");
			f.close ();
		}
		catch (Exception r)
		{
			Log.logError ("Dimension", "Error while reading dimension-file *******");
		}
	}

	/**
	 * Add a new Unit to an existed dimension.
	 *
	 * @param dimensionKey
	 * @param unitKey
	 * @param unitMin
	 * @param unitMax
	 */
	public static void addUnit (
		String dimensionKey, String unitKey, double unitMin, double unitMax)
	{
		try
		{
			engine = Engine.instance ();

			String fileName =
				(engine.getSystemDir () + engine.getFileSeparator () + "dimension.txt");

			RandomAccessFile f = new RandomAccessFile(fileName, "rw");
			String line;
			String newDataString = new String();
			long dataPosition = f.length ();

			while ((line = f.readLine ()) != null)
			{
				String[] data = line.split ("#");

				if (dimensionKey.equals (data[0]))
				{
					;
					dataPosition = f.getFilePointer () - 1;
					newDataString =
						newDataString.concat (
							line + "#" + unitKey + ", " + unitMin + ", " + unitMax + "\n");
				}
				else
				{
					newDataString = newDataString.concat (line + "\n");
				}
			}

			f.seek (0);
			f.writeBytes (newDataString);
			f.close ();
		}
		catch (Exception r)
		{
			Log.logError ("Dimension", "Error while reading dimension-file *******");
		}
	}

	/**
	 * Write the names of the new units and dimension in the resource properties.
	 *
	 * @param dimensionKey
	 * @param dimensionDe
	 * @param dimensionEn
	 * @param unitKey
	 * @param unitDe
	 * @param unitEn
	 */
	public static void writeDimensionUnitRessource (
		String dimensionKey, String dimensionDe, String dimensionEn, String unitKey, String unitName)
	{
		engine = Engine.instance ();

		String directory = (engine.getSystemDir () + engine.getFileSeparator ());

		TextResource textResource =
			(TextResource) Engine.instance ().getResourceService ().getResourceBundle ();

		textResource.loadFromFile (
			directory, "dimension" + "_" + textResource.getLocale ().getLanguage () +
			".properties");

		Properties prop = new Properties();

		try
		{
			prop.load (new FileInputStream(directory + "dimension" + "_de" + ".properties"));
			prop.setProperty ("metix." + dimensionKey, dimensionDe);
			prop.setProperty ("metix." + dimensionKey + "." + unitKey, unitName);
			prop.store (new FileOutputStream(directory + "dimension" + "_de" + ".properties"), "");
			prop.load (new FileInputStream(directory + "dimension" + "_en" + ".properties"));
			prop.setProperty ("metix." + dimensionKey, dimensionEn);
			prop.setProperty ("metix." + dimensionKey + "." + unitKey, unitName);
			prop.store (new FileOutputStream(directory + "dimension" + "_en" + ".properties"), "");
		}
		catch (Exception e)
		{
			Log.logError ("Dimension", "Error while reading properties *******");
		}
	}

	/**
	 * Wirte the name of the new units in the resource properties.
	 *
	 * @param unitKey
	 * @param unitDe
	 * @param unitEn
	 */
	public static void writeUnitRessource (String dimensionKey, String unitKey, String unitName)
	{
		engine = Engine.instance ();

		String directory = (engine.getSystemDir () + engine.getFileSeparator ());

		TextResource textResource =
			(TextResource) Engine.instance ().getResourceService ().getResourceBundle ();

		textResource.loadFromFile (
			directory, "dimension" + "_" + textResource.getLocale ().getLanguage () +
			".properties");

		Properties prop = new Properties();

		try
		{
			prop.load (new FileInputStream(directory + "dimension" + "_de" + ".properties"));
			prop.setProperty ("metix." + dimensionKey + "." + unitKey, unitName);
			prop.store (new FileOutputStream(directory + "dimension" + "_de" + ".properties"), "");
			prop.load (new FileInputStream(directory + "dimension" + "_en" + ".properties"));
			prop.setProperty ("metix." + dimensionKey + "." + unitKey, unitName);
			prop.store (new FileOutputStream(directory + "dimension" + "_en" + ".properties"), "");
		}
		catch (Exception e)
		{
			Log.logError ("Dimension", "Error while reading properties *******");
		}
	}
}