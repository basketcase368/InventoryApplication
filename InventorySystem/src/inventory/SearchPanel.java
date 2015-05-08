/**
 * Class SearchPanel provides very basic GUI search functionality. It allows the user to 
 * enter the title of a an item they are searching for. The GUI then displays the information
 * of the MediItem that contains the same title as what was searched for. 
 * 
 * Future:
 * 1) Provide media type specific searching (i.e. search only through books, cds, etc);
 * 2) Provide functionality to search by more than just the title (i.e. search by artist,
 * author, etc); 
 */
package inventory;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import java.util.Properties;
//import java.util.Set;

public class SearchPanel extends JPanel
{
	//------------------------------------- FIELDS -------------------------------------
	private JTextField searchField;
	private JLabel searchLabel;
	private JTextArea searchResultsDisplay;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private InventoryView view;
	//------------------------------------- CONSTRUCTOR -------------------------------------
	public SearchPanel(InventoryView view)
	{
		super();
		this.view = view;
		buildGUI();
	}
	
	//------------------------------------- GUI METHODS -------------------------------------
	// method to build and add all components to the container
	private void buildGUI()
	{
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(layout);
		
		//create components
		searchLabel = new JLabel("Enter Item Title:");
		searchField = new JTextField("",10);
		searchResultsDisplay = new JTextArea(15,20);
		searchResultsDisplay.setEditable(false);
		
		// register event handlers
		searchField.addActionListener(new SearchFieldHandler());
		
		// add and layout components
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(searchLabel,constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(searchField,constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 0.5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(searchResultsDisplay, constraints);	
	}

	//------------------------------------- HANDLERS -------------------------------------
	// action listener handler for text field;
	private class SearchFieldHandler implements ActionListener
	{
		private String searchString = null;
		private String resultString = null;
		@Override
		public void actionPerformed(ActionEvent e)
		{
			searchString = e.getActionCommand();
			resultString = view.getModel().searchInventory(searchString);
			if(resultString != null)
				searchResultsDisplay.setText(String.format("Found the following item...\n\n%s",resultString));
			else
				searchResultsDisplay.setText(String.format("Could not find item with following title... \n\n %s", searchString));
		}
	}
}