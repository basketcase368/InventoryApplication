package inventory;
/**
 * Panel that will display the inventory
 */

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

public class DisplayPanel extends JPanel
{
	//------------------------------------- FIELDS -------------------------------------
	private JTable displayTable;
	private DisplayTableModel tableModel;
	private JScrollPane displayPane;
	private ButtonGroup buttonGroup;
	private JRadioButton bookButton, cdButton, dvdButton, allButton;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	private InventoryView view;
	private MediaType currentType; 
	private int currentRow;
	private int currentItemID;
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public DisplayPanel(InventoryView view)
	{
		super();
		this.view = view;
		this.currentType = MediaType.BOOK; // default type selected.
		this.currentRow = -1; // no row selected at first;
		this.currentItemID = -1; //no item selected
		buildGUI();
		refreshTableData(currentType);
	}
	
	//------------------------------------- GUI METHODS -------------------------------------
	// method to format data so that it can be properly interpreted by the tableModel
	private String[][] formatRowData(ArrayList<MediaItem> list)
	{	
		MediaItem tempItem;
		int setLength = list.size();
		int setWidth = 5;
		
		String[][] formatedData = new String[setLength][setWidth];
		for(int i = 0; i < list.size(); i++)
		{
			tempItem = list.get(i);
			formatedData[i][0] = tempItem.getTitle();
			formatedData[i][1] = tempItem.getCreator();
			formatedData[i][2] = tempItem.getGenre();
			formatedData[i][3] = tempItem.getDuration();
			formatedData[i][4] = tempItem.getType();
		}
		
		return formatedData;
	}
	
	// Method to create GUI components and place them appropriately in the JPanel
	private void buildGUI()
	{
		// layout managers
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(layout);
		
		// create table model and create JTable
		tableModel = new DisplayTableModel();
		displayTable = new JTable(tableModel);
		displayTable.setPreferredScrollableViewportSize(new Dimension(500,200));
		displayTable.setFillsViewportHeight(false);
		displayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		displayTable.getSelectionModel().addListSelectionListener(new RowSelectionHandler()); //add listener to selection model
		displayPane = new JScrollPane(displayTable);
		displayPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// create buttons to control display of JTable
		bookButton = new JRadioButton("Display Books",true);
		cdButton = new JRadioButton("Display CDs",false);
		dvdButton = new JRadioButton("Display DVDs",false);
		allButton = new JRadioButton("Display All",false);
		
		// create button logic 
		buttonGroup = new ButtonGroup();
		buttonGroup.add(bookButton);
		buttonGroup.add(cdButton);
		buttonGroup.add(dvdButton);
		buttonGroup.add(allButton);
		
		// add button listeners
		bookButton.addItemListener(new RadioButtonHandler(MediaType.BOOK));
		cdButton.addItemListener(new RadioButtonHandler(MediaType.CD));
		dvdButton.addItemListener(new RadioButtonHandler(MediaType.DVD));
		allButton.addItemListener(new RadioButtonHandler(MediaType.ALL)); //book for now
		
		// add components to panel
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(bookButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(cdButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(dvdButton, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(allButton, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 4;
		constraints.gridheight = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		add(displayPane, constraints);
	}
	
	// method to refresh JTable data for selectedType
	public  void refreshTableData(MediaType type)
	{
		tableModel.updateRowData(this.view.getModel().getInventory(type));
	}
	
	// method to return current selected MediaType 
	public MediaType getCurrentType()
	{
		return this.currentType;
	}
	
	// method to get current selected row from JTable
	public int getCurrentRow()
	{
		return this.currentRow;
	}
	
	// method to get current itemID for selected row
	public int getCurrentItemID()
	{
		return this.currentItemID;
	}
	//------------------------------------- HANDLERS -------------------------------------
	// table model class that defines how to handle to handle display for view
	private class DisplayTableModel extends AbstractTableModel
	{
		//---------------------Fields----------------------
		private String[] colData = {"Title", "Creator", "Genre",
									"Duration","MediaType"};
		private Object[][] rowData = {{"","","","",""}};
		
		public DisplayTableModel(){ }

		@Override
		public int getColumnCount()
		{
			return colData.length;
		}
		
		@Override
		public String getColumnName(int col)
		{
			return colData[col];
		}
		
		@Override
		public int getRowCount() 
		{
			return rowData.length;
		}
		
		@Override
		public Object getValueAt(int row, int col)
		{
			return rowData[row][col];
		}
		
		@Override 
		public void setValueAt(Object value, int row, int col)
		{
			rowData[row][col] = value;
			fireTableCellUpdated(row,col);
		}
		
		public void updateRowData(ArrayList<MediaItem> rowList)
		{
			this.rowData = formatRowData(rowList);
			fireTableDataChanged();
		}
	}

	// Button Handler for Radio Buttons
	private class RadioButtonHandler implements ItemListener
	{
		private MediaType buttonSelection;
		public RadioButtonHandler(MediaType type)
		{
			buttonSelection = type; 
		}
		
		// handle ItemChanged
		@Override
		public void itemStateChanged(ItemEvent event)
		{
			if(event.getStateChange() == ItemEvent.SELECTED)
			{
				// update currentType and set row to -1 since no rows will be selected
				DisplayPanel.this.currentType = buttonSelection;
				DisplayPanel.this.currentRow = -1;
				
				// update edit panel
				DisplayPanel.this.view.getEditPanel().setCurrentType(DisplayPanel.this.getCurrentType());
				DisplayPanel.this.view.getEditPanel().clearFields();
				
				// update remove panel
				DisplayPanel.this.view.getRemovePanel().setCurrentType(DisplayPanel.this.getCurrentType());
				DisplayPanel.this.view.getRemovePanel().clearFields();
				
				//update data in JTable
				DisplayPanel.this.refreshTableData(getCurrentType());
			}
		}
	}
	
	// Selection handler for JTable to update remove/edit panels;
	private class RowSelectionHandler implements ListSelectionListener
	{
		@Override 
		public void valueChanged(ListSelectionEvent event)
		{
			MediaItem currentItem;
			try
			{
				if(event.getValueIsAdjusting())
				{
					return;
				}
				//update DisplayPanel data and get the current item selected to update edit and row panels
				DisplayPanel.this.currentRow = DisplayPanel.this.displayTable.getSelectedRow();
				currentItem = DisplayPanel.this.view.getModel().getInventorySingle(currentType,currentRow);
				DisplayPanel.this.currentItemID =  currentItem.getMediaID();
				
				//update edit panel
				DisplayPanel.this.view.getEditPanel().setFields(currentItem);
				DisplayPanel.this.view.getEditPanel().setCurrentItemID(DisplayPanel.this.currentItemID);
				
				//update remove panel
				DisplayPanel.this.view.getRemovePanel().setFields(currentItem);
				DisplayPanel.this.view.getRemovePanel().setCurrentItemID(DisplayPanel.this.currentItemID);
			}
			catch(ArrayIndexOutOfBoundsException exception) 
			{
				return;
			}
		}
	}
}