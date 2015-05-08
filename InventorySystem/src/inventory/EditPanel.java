package inventory;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditPanel extends JPanel 
{
	//------------------------------------- FIELDS -------------------------------------
	private JLabel descriptionLabel, titleLabel, creatorLabel, genreLabel, durationLabel;
	private JTextField titleField, creatorField, genreField, durationField;
	private JButton editItemButton;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	private InventoryView view;
	private MediaType currentType; //retrieved from displayPanel;
	private int currentItemID; //retrieved from display panel when row is selected;
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public EditPanel(InventoryView view)
	{
		super();
		this.view = view;
		this.currentType = view.getDisplayPanel().getCurrentType();
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
		descriptionLabel = new JLabel("Please make changes to the selected item as necessary.");
		titleLabel = new JLabel("Edit Title:");
		creatorLabel = new JLabel("Edit Creator:");
		genreLabel = new JLabel("Edit Genre:");
		durationLabel = new JLabel("Edit Duration:");
		
		// create fields
		titleField = new JTextField("",20);
		creatorField = new JTextField("",20);
		genreField = new JTextField("",20);
		durationField = new JTextField("",20);
		
		// create editButton
		editItemButton = new JButton("Edit Item");
		editItemButton.addActionListener(new EditItemHandler());
		
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
		add(editItemButton, constraints);
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
	private class EditItemHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			// create new item (wont create if any fields are empty)
			String[] format = new String[5];
			try
			{
				format[0] = String.valueOf(EditPanel.this.currentItemID);
				format[1] = EditPanel.this.titleField.getText();
				format[2] = EditPanel.this.creatorField.getText();
				format[3] = EditPanel.this.genreField.getText();
				format[4] = EditPanel.this.durationField.getText();
				
				// check for empty fields to prevent adding an empty object
				if(isAnyEmpty(format))
					throw new IllegalArgumentException();
				
				// check to make sure we are not editing in ALL panel
				if(EditPanel.this.currentType == MediaType.ALL) {
					JOptionPane.showMessageDialog(EditPanel.this.view, 
							"Cannot edit items when 'ALL' is selected as display. Select different display option and try again",
							"ERROR", JOptionPane.PLAIN_MESSAGE);
					return;
				}
					
				EditPanel.this.view.getModel().updateItem(EditPanel.this.currentType, format);
				JOptionPane.showMessageDialog(EditPanel.this.view, "Successfully edited item", 
						"Success",JOptionPane.PLAIN_MESSAGE);
			}
			catch(NullPointerException npex)
			{
				JOptionPane.showMessageDialog(EditPanel.this.view, "Please select an item to edit.",
						"ERROR", JOptionPane.PLAIN_MESSAGE);
			}
			catch(IllegalArgumentException ilex)
			{
				JOptionPane.showMessageDialog(EditPanel.this.view, 
						"Please make sure all fields are entered correctly and are not empty.",
						"ERROR", JOptionPane.PLAIN_MESSAGE);
			}
			
			// refresh table and display new list;
			EditPanel.this.view.getDisplayPanel().refreshTableData(EditPanel.this.currentType);
			
		}
		
		// method to check if any fields are empty
		private boolean isAnyEmpty(String[] formattedString)
		{
			for(String temp: formattedString)
			{
				if(temp.equals(""))
					return true;
			}
			return false;
		}
	}
}
