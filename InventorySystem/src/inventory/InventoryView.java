package inventory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;


public class InventoryView extends JFrame
{
	//------------------------------------- FIELDS -------------------------------------
	private InventorySQLModel model;
	private final JPanel containerPanel;
	private final DisplayPanel displayPanel;
	private final SearchPanel searchPanel;
	private final AddPanel addPanel;
	private final EditPanel editPanel;
	private final RemovePanel removePanel;
	private final JTabbedPane dataPanes;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public InventoryView()
	{
		super("Borders Inventory System");
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		
		// create container panel and set layout
		containerPanel = new JPanel();
		containerPanel.setOpaque(true);
		containerPanel.setLayout(layout);
		containerPanel.setPreferredSize(new Dimension(900,350));
		
		// create other panels and model
		model = new InventorySQLModel(this);
		displayPanel = new DisplayPanel(this);
		searchPanel = new SearchPanel(this);
		addPanel = new AddPanel(this);
		editPanel = new EditPanel(this);
		removePanel = new RemovePanel(this);
		
		// add manipulation panels to JtabbedPane
		dataPanes = new JTabbedPane();
		dataPanes.addTab("Add Item", null, addPanel, "Add Panel");
		dataPanes.addTab("Remove Item", null, removePanel, "Remove Panel");
		dataPanes.addTab("Edit Item", null, editPanel, "Edit Panel");
		dataPanes.addTab("Search Item", null, searchPanel, "Search Panel");
		
		//layout and place components in frame
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		containerPanel.add(displayPanel);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		containerPanel.add(dataPanes);
		
		//defaults for JFrame
		addWindowListener(new WindowCloseAdapter(this));
		setContentPane(containerPanel);
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//------------------------------------- GUI METHODS -------------------------------------
	// method to return model. allows model access to lower level panels;
	public InventorySQLModel getModel()
	{
		return model;
	}
	
	//method to return displayPanel
	public DisplayPanel getDisplayPanel()
	{
		return this.displayPanel;
	}
	
	//method to return searchPanel
	public SearchPanel getSearchPanel()
	{
		return this.searchPanel;
	}
	
	//method to return addPanel
	public AddPanel getAddPanel()
	{
		return this.addPanel;
	}
	
	//method to return editPanel
	public EditPanel getEditPanel()
	{
		return this.editPanel;
	}
	
	//method to return removePanel
	public RemovePanel getRemovePanel()
	{
		return this.removePanel;
	}
	
	//------------------------------------- WINDOW HANDLERS -------------------------------------
	// handler for closing the window. Closes connection to database when user closes the JFrame
	private class WindowCloseAdapter extends WindowAdapter
	{
		InventoryView parentView;
		public WindowCloseAdapter(InventoryView view)
		{
			this.parentView = view;
		}
		
		@ Override
		public void windowClosing(WindowEvent evt)
		{
			parentView.getModel().closeConnection();
			System.exit(0);
		}
	}
}
// end view class
