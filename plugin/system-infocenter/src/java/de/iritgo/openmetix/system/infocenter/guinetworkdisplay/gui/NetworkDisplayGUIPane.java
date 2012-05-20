/*
    This file is part of the Iritgo Framework.

    Copyright (C) 2004-2007 BueroByte GbR
    Copyright (C) 2008 Iritgo Technologies
*/


package de.iritgo.openmetix.system.infocenter.guinetworkdisplay.gui;


import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.swing.ILabel;
import de.iritgo.openmetix.core.gui.swing.SwingGUIPane;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.framework.client.command.ShowWindow;
import de.iritgo.openmetix.framework.command.CommandTools;
import de.iritgo.openmetix.system.infocenter.manager.InfoCenterClientManager;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;


public class NetworkDisplayGUIPane
	extends SwingGUIPane
	implements GUIDisplay
{
	private JTable infosList;
	private JScrollPane scrollpane;
	private final java.util.LinkedList infos = new LinkedList();
	private TableModel data;

	public NetworkDisplayGUIPane ()
	{
		super("infocenter.networkdisplay");
	}

	public void initGUI ()
	{
		InfoCenterClientManager infoCenterClientManager =
			(InfoCenterClientManager) Engine.instance ().getManagerRegistry ().getManager (
				"infocenterclient");

		infoCenterClientManager.setGUIDisplay (this);

		JPanel allPanel = new JPanel();

		allPanel.setLayout (new GridBagLayout());

		int row = 0;

		ILabel infosLabel = new ILabel("infocenter.infos");

		allPanel.add (
			infosLabel,
			createConstraints (
				0, row++, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets(5, 15, 5, 15)));

		data =
			new AbstractTableModel()
				{
					public int getColumnCount ()
					{
						return 1;
					}

					public int getRowCount ()
					{
						return infos.size ();
					}

					public boolean isCellEditable (int row, int col)
					{
						return false;
					}

					public Class getColumnClass (int c)
					{
						return new InfoItem("unknown").getPanel ().getClass ();
					}

					public Object getValueAt (int row, int col)
					{
						InfoItem item = (InfoItem) infos.get (row);

						return item;
					}
				};

		infosList = new JTable(data);

		MouseListener mouseListener =
			new MouseAdapter()
			{
				public void mouseClicked (MouseEvent e)
				{
					if (e.getClickCount () == 2)
					{
						int row = infosList.rowAtPoint (e.getPoint ());
						InfoItem item = (InfoItem) infosList.getValueAt (row, 0);

						CommandTools.performAsync (
							new ShowWindow(item.getGuiPaneId (), item.getUniqueId ()));
					}
				}
			};

		infosList.addMouseListener (mouseListener);
		infosList.setAutoResizeMode (JTable.AUTO_RESIZE_ALL_COLUMNS);

		infosList.setDefaultRenderer (infosList.getColumnClass (0), new InfoCellRenderer());

		infosList.setRowHeight (70);

		scrollpane = new JScrollPane(infosList);
		allPanel.add (
			scrollpane,
			createConstraints (
				0, row, 1, 1, GridBagConstraints.BOTH, 100, 100, new Insets(5, 15, 5, 15)));

		content.add (
			allPanel, createConstraints (0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));
	}

	public void loadFromObject ()
	{
	}

	public void storeToObject ()
	{
	}

	public void addInfo (
		final int context, final String category, final String icon, final String message,
		final String guiPaneId, final long uniqueId, final int level)
	{
		try
		{
			SwingUtilities.invokeAndWait (
				new Runnable()
				{
					public void run ()
					{
						InfoItem item =
							new InfoItem(
								category, icon, "M: " + message, guiPaneId, uniqueId, level);

						infos.addFirst (item);
						infosList.revalidate ();
					}
				});
		}
		catch (Exception x)
		{
		}
	}

	public void close ()
	{
		super.close ();
	}

	public IObject getSampleObject ()
	{
		return null;
	}

	public GUIPane cloneGUIPane ()
	{
		return new NetworkDisplayGUIPane();
	}
}