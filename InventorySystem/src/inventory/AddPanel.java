package inventory;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class AddPanel extends JPanel
{
	//------------------------------------- FIELDS -------------------------------------
	private JLabel typeLabel, titleLabel, creatorLabel, genreLabel, durationLabel;
	private JRadioButton bookButton, cdButton, dvdButton;
	private JTextField titleField, creatorField, genreField, durationField;
	private JButton addItemButton;
	private ButtonGroup buttonGroup;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private InventoryView view;
	private MediaType currentType;
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public AddPanel(InventoryView view)
	{
		super();
		buildGUI();
		this.view = view;
	}
	
	//------------------------------------- GUI MEHTODS -------------------------------------
	// method to create and add basic gui components to panel.
	private void buildGUI()
	{
		// layout defaults
		layout  = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(layout);
	
		// create labels
		typeLabel = new JLabel("Select Type:");
		titleLabel = new JLabel("Enter Title:");
		creatorLabel = new JLabel("Enter Creator:");
		genreLabel = new JLabel("Enter Genre:");
		durationLabel = new JLabel("Enter Duration:");
		
		// create buttons
		bookButton = new JRadioButton("Book",true);
		cdButton = new JRadioButton("CD",false);
		dvdButton = new JRadioButton("DVD",false);
		addItemButton = new JButton("Add Item");
		currentType = MediaType.BOOK; // since book is "true"
		
		// create button logic for radio buttons 
		buttonGroup = new ButtonGroup();
		buttonGroup.add(bookButton);
		buttonGroup.add(cdButton);
		buttonGroup.add(dvdButton);
		
		// create fields
		titleField = new JTextField(null,20);
		creatorField = new JTextField(null,20);
		genreField = new JTextField(null,20);
		durationField = new JTextField(null,20);
		
		// add handlers to JRadioButtons and JButton
		bookButton.addItemListener(new RadioButtonHandler(MediaType.BOOK));
		cdButton.addItemListener(new RadioButtonHandler(MediaType.CD));
		dvdButton.addItemListener(new RadioButtonHandler(MediaType.DVD));
		addItemButton.addActionListener(new AddItemHandler());
		
		// add components via gridBagLayout
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(typeLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(bookButton, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(cdButton, constraints);
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(dvdButton, constraints);
		
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
		constraints.gridwidth = 3;
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
		constraints.gridwidth = 3;
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
		constraints.gridwidth = 3;
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
		constraints.gridwidth = 3;
		add(durationField, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridheight = 1;
		constraints.gridwidth = 2;
		add(addItemButton, constraints);
		
	}
	
	// method to clear fields. to be called after changing radio buttons in add panel
	private void clearFields()
	{
		titleField.setText(null);
		creatorField.setText(null);
		genreField.setText(null);
		durationField.setText(null);
	}
	
	// method to get current type
	private MediaType getCurrentType()
	{
		return this.currentType;
	}
	//------------------------------------- HANDLERS -------------------------------------
	// Button Handler Class for Radio Buttons
	private class RadioButtonHandler implements ItemListener
	{
		MediaType changeType;
		public RadioButtonHandler(MediaType type)
		{
			changeType = type;
		}
		
		// handle ItemChanged
		@Override
		public void itemStateChanged(ItemEvent event)
		{
			AddPanel.this.currentType = changeType;
			AddPanel.this.clearFields();
		}
	}
	
	// Button Handler Class for JButton (addItem)
	private class AddItemHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			int success = 0;
			String[] format = new String[5];
			try
			{
				format[0] = "null"; //auto increment of database
				format[1] = titleField.getText();
				format[2] = creatorField.getText();
				format[3] = genreField.getText();
				format[4] = durationField.getText();
				
				// check if there are any empty fields
				if(isAnyEmpty(format))
					throw new IllegalArgumentException();
				
				//attempt to add item to model
				success = AddPanel.this.view.getModel().addItem(AddPanel.this.getCurrentType(),format);
				
				//show success if item did add to model
				if (success > 0)
				JOptionPane.showMessageDialog(AddPanel.this.view, "Successfully added item to " + currentType + " category.",
						"SUCCESS", JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(AddPanel.this.view, "Error item not added " + currentType + " category.",
							"ERROR", JOptionPane.PLAIN_MESSAGE);
				
			}
			catch(NullPointerException npex)
			{
				JOptionPane.showMessageDialog(AddPanel.this.view, 
						"Please make sure all fields are entered correctly and are _____.",
						"ERROR", JOptionPane.PLAIN_MESSAGE);
			}
			catch(IllegalArgumentException ilex)
			{
				JOptionPane.showMessageDialog(AddPanel.this.view, 
						"Please make sure all fields are entered correctly and are not empty.",
						"ERROR", JOptionPane.PLAIN_MESSAGE);
			}
			
			// clear fields for next add
			AddPanel.this.clearFields();
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
