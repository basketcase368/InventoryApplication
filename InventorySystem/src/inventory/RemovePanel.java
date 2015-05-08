package inventory;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemovePanel extends JPanel
{
	//------------------------------------- FIELDS -------------------------------------
	private JLabel descriptionLabel, titleLabel, creatorLabel, genreLabel, durationLabel;
	private JTextField titleField, creatorField, genreField, durationField;
	private JButton removeItemButton;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	private InventoryView view;
	private MediaType currentType; //retrieved from displayPanel;
	private int currentItemID; // retrieved from displayPanel;
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public RemovePanel(InventoryView view)
	{
		super();
		this.view = view;
		this.currentType = view.getDisplayPanel().getCurrentType();
		this.currentItemID = view.getDisplayPanel().getCurrentItemID();
		buildGUI();
	}
	
	//------------------------------------- GUI METHODS -------------------------------------
	// method to create and add basic gui components
	private void buildGUI()
	{
		// layout defaults
		layout  = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(layout);
			
		// create labels
		descriptionLabel = new JLabel("The following item will be removed:");
		titleLabel = new JLabel("Selected Title:");
		creatorLabel = new JLabel("Selected Creator:");
		genreLabel = new JLabel("Selected Genre:");
		durationLabel = new JLabel("Selected Duration:");
		
		// create fields
		titleField = new JTextField("",20);
		titleField.setEditable(false);
		creatorField = new JTextField("",20);
		creatorField.setEditable(false);
		genreField = new JTextField("",20);
		genreField.setEditable(false);
		durationField = new JTextField("",20);
		durationField.setEditable(false);
		
		// create editButton
		removeItemButton = new JButton("Remove Item");
		removeItemButton.addActionListener(new RemoveItemHandler());
		
		//add components via gridbag layout
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 2;
		add(descriptionLabel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(titleLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(titleField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(creatorLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(creatorField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(genreLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(genreField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(durationLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(durationField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 2;
		add(removeItemButton, constraints);
	}	

	// method to set fields with corresponding information from MediaItem object selected in displayPanels JTable. 
	public void setFields(MediaItem selectedItem)
	{
		titleField.setText(selectedItem.getTitle());
		creatorField.setText(selectedItem.getCreator());
		genreField.setText(selectedItem.getGenre());
		durationField.setText(selectedItem.getDuration());
	}
	
	// method to clear fields. done after displayPanel JTable is changed to avoid editing not existing item 
	public void clearFields()
	{
		titleField.setText(null);
		creatorField.setText(null);
		genreField.setText(null);
		durationField.setText(null);
	}
	
	//method to set current mediaType from outside of this class
	public void setCurrentType(MediaType type)
	{
		this.currentType = type;
	}
	
	//method to set current Item ID based on row selected from outside of this class
	public void setCurrentItemID(int id)
	{
		this.currentItemID = id;
	}

	//------------------------------------- HANDLERS -------------------------------------
	private class RemoveItemHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			int success  = 0;
			//attempt to remove item
			try
			{
				// check to make sure we are not editing in ALL panel
				if(RemovePanel.this.currentType == MediaType.ALL) {
					JOptionPane.showMessageDialog(RemovePanel.this.view, 
							"Cannot remove items when 'ALL' is selected as display. Select different display option and try again",
							"ERROR", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				
				// ask the user if they are sure they want to remove the item via JOptionPane
				int value = JOptionPane.showConfirmDialog(RemovePanel.this.view,"Are you sure you want to remove the item?", 
						"Warning", JOptionPane.YES_NO_CANCEL_OPTION);
				if(value == 0)
				{
					// remove item by title
					success = RemovePanel.this.view.getModel().removeItem(RemovePanel.this.currentType, RemovePanel.this.currentItemID);
					
					// detrermine if successfully added to database.
					if(success > 0)
					JOptionPane.showMessageDialog(RemovePanel.this.view, "Succesfully Removed Item",
							"Item Removed", JOptionPane.PLAIN_MESSAGE);
					else
					JOptionPane.showMessageDialog(RemovePanel.this.view, "Failed to remove item",
								"ERROR", JOptionPane.PLAIN_MESSAGE);
				}		
			}
			catch(NullPointerException npex)
			{
				System.out.println("Something went wrong");
			}
			
			// refresh table and display new list;
			RemovePanel.this.view.getDisplayPanel().refreshTableData(RemovePanel.this.currentType);
			
			// clear fields
			RemovePanel.this.clearFields();
		}
	}

}
