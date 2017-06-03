package org.jfclarkjr.java3hw3;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

/**
 * InventoryView is class used for accessing an inventory system composed
 * of DVDs, CDs, and Books.
 * <p>
 * This version is for Java 3, Homework #2.  It uses Java Swing components
 * to display the user interface.
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryView implements Viewable
{
	private InventoryController controller;
	private InventoryModel model;
	private InventoryItem currentItem;
	private JTextField titleTextField;
	private JTextField artistTextField;
	private JTextField inventoryNumberTextField;
	private JButton button;
	private JButton refreshButton;
	private JButton exitButton;
	private JComboBox<String> userChoiceComboBox;
	private JComboBox<String> mediaTypeComboBox;
	private JPanel userChoiceComboPanel;
	private JPanel mediaTypeComboPanel;
	private JPanel titleTextFieldPanel;
	private JPanel artistTextFieldPanel;
	private JPanel inventoryNumberFieldPanel;
	private JPanel buttonPanel;
	private JPanel exitButtonPanel;
	private JPanel messagePanel;
	private JTextArea searchResultsTextArea;
	private JScrollPane searchResultsScrollPane;
	private JLabel messagePanelLabel;
	private String userChoice;
	private String mediaType;
	private String title;
	private String artist;
	private String inventoryNumber;
	private JFrame frame;
	private static final String[] userChoiceList = {"", "Create", "Retrieve", "Update", "Delete",
			"List Entire Catalog"};
	private static final String[] mediaTypeList = {"", "CD", "DVD", "Book"};
	
	// JTable Testing
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mediainventory?useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String DEFAULT_QUERY = "SELECT * FROM mediaitems";
	private static ResultSetTableModel tableModel;
	private JTable resultTable;
	private JScrollPane resultTablePane;
	
	
	public InventoryView(InventoryController controller)
	{
		this.controller = controller;
		currentItem = new InventoryItem();
	}
	
	/**
	 * This method registers the Model object
	 * 
	 * @param model  The inventory model object
	 */
	public void setModel(InventoryModel model)
	{
		this.model = model;
	}
	
	/**
	 * Method called by the model to alert the view object that a state change
	 * has occurred.
	 * 
	 * @param stateChange An enum that specifies the change in state if the inventory
	 */
	public void notifyOfModelStateChange(InventoryStateChange stateChange)
	{
		if (stateChange == InventoryStateChange.RETRIEVE)
			displayItems();
		else if (stateChange == InventoryStateChange.ITEM_NOT_FOUND)
			searchResultsTextArea.append("Item not found!");
		else
			searchResultsTextArea.append("Undefined response, please try again.");
	}
	
	/**
	 * This is the primary method for the InventoryView class
	 * This initiates the user interface to access the media catalog
	 */
	public void start()
	{

		try
		{
			// JTable Testing
			tableModel = new ResultSetTableModel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);
			resultTable = new JTable(tableModel);
			resultTablePane = new JScrollPane(resultTable);
			
		}
		catch( SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		
		// Initialize the JFrame 
		frame = new JFrame("Media Inventory System");
		frame.setLayout(new GridLayout(7,2));
		
		// Create the JPanels
		userChoiceComboPanel = new JPanel();
		mediaTypeComboPanel = new JPanel();
		titleTextFieldPanel = new JPanel();
		artistTextFieldPanel = new JPanel();
		inventoryNumberFieldPanel = new JPanel();
		buttonPanel = new JPanel();
		exitButtonPanel = new JPanel();
		messagePanel = new JPanel();
		
		// Construct text fields with 10 columns
		titleTextField = new JTextField(10);
		artistTextField = new JTextField(10);
		inventoryNumberTextField = new JTextField(10);
		
		// Create JButtons
		button = new JButton("OK");
		refreshButton = new JButton("Start Over");
		exitButton = new JButton("Exit");
		
		// Create JComboBoxes
		userChoiceComboBox = new JComboBox<String>(userChoiceList);
		mediaTypeComboBox = new JComboBox<String>(mediaTypeList);
		
		// Create Labels for the ComboBoxes and TextFields
		JLabel userChoiceComboBoxLabel = new JLabel("Choose action: ");
		JLabel mediaTypeComboBoxLabel = new JLabel("Enter Media Type: ");
		JLabel titleTextFieldLabel = new JLabel("Enter Title: ");
		JLabel artistTextFieldLabel = new JLabel("Enter Artist: ");
		JLabel inventoryNumberFieldLabel = new JLabel("Enter Inventory Number: ");
		messagePanelLabel = new JLabel("");
		
		// Set up JTextArea
		searchResultsTextArea = new JTextArea(5, 30);
		searchResultsScrollPane = new JScrollPane(searchResultsTextArea);
		searchResultsTextArea.setEditable(false);
		
		// Add the components to the JPanels
		userChoiceComboPanel.add(userChoiceComboBoxLabel);
		userChoiceComboPanel.add(userChoiceComboBox);
		mediaTypeComboPanel.add(mediaTypeComboBoxLabel);
		mediaTypeComboPanel.add(mediaTypeComboBox);
		titleTextFieldPanel.add(titleTextFieldLabel);
		titleTextFieldPanel.add(titleTextField);
		artistTextFieldPanel.add(artistTextFieldLabel);
		artistTextFieldPanel.add(artistTextField);
		inventoryNumberFieldPanel.add(inventoryNumberFieldLabel);
		inventoryNumberFieldPanel.add(inventoryNumberTextField);
		buttonPanel.add(button);
		exitButtonPanel.add(refreshButton);
		exitButtonPanel.add(exitButton);
		messagePanel.add(messagePanelLabel);
		
		// Anonymous inner class to handle the OK button.
		// This captures the information in the various text fields
		// depending on which action the user is performing
		button.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						inventoryNumber = "";
						title = "";
						artist = "";

						inventoryNumber = inventoryNumberTextField.getText();
						title = titleTextField.getText();
						artist = artistTextField.getText();

						if ( userChoice.equals("Create"))
						{
							controller.createItem(mediaType, title, artist);
							messagePanelLabel.setText("Item Created!");
							frame.add(messagePanel);
							refreshFrame();
						}
						else if ( userChoice.equals("Retrieve"))
						{
							controller.retrieveItemByTitle(title);
						}
						else if ( userChoice.equals("Update"))
						{
							controller.updateItem(inventoryNumber, mediaType, title, artist);
							messagePanelLabel.setText("Item Updated!");
							frame.add(messagePanel);
							refreshFrame();
						}
						else if ( userChoice.equals("Delete"))
						{
							controller.deleteItem(inventoryNumber);
							messagePanelLabel.setText("Item Deleted!");
							frame.add(messagePanel);
							refreshFrame();
						}


						// Display exit options
						frame.add(exitButtonPanel);
						refreshFrame();
					}
				}
				);
		
		// Anonymous inner class to handle the "Start Over" button.
		// This clears all the text fields and starts the JFrame session
		// from the beginning.
		refreshButton.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						resetJFrame();
					}
				}
				);
		
		// Anonymous inner class to handle the selection made in the
		// Initial JComboBox presented to the user
		userChoiceComboBox.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						userChoice = "";

						userChoice = (String)userChoiceComboBox.getSelectedItem();

						if ( userChoice.equals("Create"))
						{
							resetJFrame();
							displayCreateOptions();
							refreshFrame();
						}
						else if (userChoice.equals("Retrieve"))
						{
							displayRetrieveOptions();
							refreshFrame();
						}
						else if (userChoice.equals("Update"))
						{
							displayUpdateOptions();
							refreshFrame();
						}
						else if (userChoice.equals("Delete"))
						{
							displayDeleteOptions();
							refreshFrame();
						}
						else if (userChoice.equals("List Entire Catalog"))
						{
							displayEntireInventoryList();
							refreshFrame();
						}
					}
				}
				);
		
		// Anonymous inner class listener for the Media Type JComboBox
		mediaTypeComboBox.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						mediaType = "";
						mediaType = (String)mediaTypeComboBox.getSelectedItem();
					}
				}
				);
		
		// Anonymous inner class listener for the exit button
		exitButton.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent event)
					{
						frame.dispose();
					}
				}
				);
		
		// Add the initial ComboBox Panel and make the frame visible
		frame.add(userChoiceComboPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,500);
		frame.setVisible(true);
		
	}
	
	/**
	 * Method to prompt user for info needed to create an inventory item
	 */
	private void displayCreateOptions()
	{
		frame.remove(userChoiceComboPanel);
		frame.add(mediaTypeComboPanel);
		frame.add(titleTextFieldPanel);
		frame.add(artistTextFieldPanel);
		frame.add(buttonPanel);
	}
	
	/**
	 * Method to prompt user for info needed to retrieve an inventory item
	 */
	private void displayRetrieveOptions()
	{
		frame.remove(userChoiceComboPanel);
		frame.add(titleTextFieldPanel);
		frame.add(searchResultsScrollPane);
		frame.add(buttonPanel);
	}
	
	/**
	 * Method to prompt user for info needed to update an inventory item
	 */
	private void displayUpdateOptions()
	{
		frame.remove(userChoiceComboPanel);
		frame.add(inventoryNumberFieldPanel);
		frame.add(mediaTypeComboPanel);
		frame.add(titleTextFieldPanel);
		frame.add(artistTextFieldPanel);
		frame.add(buttonPanel);
	}
	
	/**
	 * Method to prompt user for info needed to delete an inventory item
	 */
	private void displayDeleteOptions()
	{
		frame.remove(userChoiceComboPanel);
		frame.add(inventoryNumberFieldPanel);
		frame.add(buttonPanel);
	}
	
	/**
	 * Display the contents of the Properties object which contains
	 * all of the inventory items
	 */
	private void displayEntireInventoryList()
	{
		/*
		frame.add(searchResultsScrollPane);
		frame.add(exitButtonPanel);
		
		Properties props;
		props = model.getEntireTable();
		
		Set<Object> keys = props.keySet();
		
		for (Object key : keys)
			searchResultsTextArea.append( key + " " + props.getProperty((String) key) + "\n");
		
		System.out.println();
		*/
		
		generateTable(DEFAULT_QUERY);
		
		
	}
	
	private void generateTable(String Query)
	{
		try
		{
			tableModel.setQuery(Query);
		}
		catch( SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		resultTable.setRowSorter(sorter);
		
		for (int i=0; i < tableModel.getColumnCount(); i++)
		{
			String name = resultTable.getColumnName(i);
			if ( name.equalsIgnoreCase("mediatype"))
				resultTable.getColumnModel().getColumn(i).setHeaderValue("Media Type");
			else if (name.equalsIgnoreCase("artist"))
				resultTable.getColumnModel().getColumn(i).setHeaderValue("Artist");
			else if (name.equalsIgnoreCase("title"))
				resultTable.getColumnModel().getColumn(i).setHeaderValue("Title");	
		}
		
		
		frame.setSize(700,500);
		frame.add(resultTablePane);
		frame.add(exitButtonPanel);
	}
	
	
	/**
	 * Method to display the Current Item which was retrieved by the retrieveItemByTitle 
	 * method on the controller
	 */
	private void displayItems()
	{
		// Retrieve currentItem from the Model
		currentItem = model.getCurrentItem();
		
		// Display the results in the JTextArea
		searchResultsTextArea.append( "Inventory Number: " + currentItem.getinventoryNumber() + "\n");
		searchResultsTextArea.append( "Media Type: " + currentItem.getType() + "\n");
		searchResultsTextArea.append( "Title: " + currentItem.getTitle() + "\n");
		searchResultsTextArea.append( "Artist: " + currentItem.getArtist() + "\n");
	}
	
	/**
	 * Method used to refresh the JFrame whenever components are added or
	 * removed
	 */
	private void resetJFrame()
	{
		userChoiceComboBox.setSelectedIndex(0);
		frame.remove(inventoryNumberFieldPanel);
		inventoryNumberTextField.setText("");
		frame.remove(mediaTypeComboPanel);
		mediaTypeComboBox.setSelectedIndex(0);
		frame.remove(titleTextFieldPanel);
		titleTextField.setText("");
		frame.remove(artistTextFieldPanel);
		artistTextField.setText("");
		frame.remove(buttonPanel);
		frame.remove(exitButtonPanel);
		frame.remove(searchResultsScrollPane);
		searchResultsTextArea.setText("");
		frame.remove(messagePanel);
		frame.add(userChoiceComboPanel);
		refreshFrame();
	}
	
	/**
	 * Method used to refresh the JFrame whenever components are added or
	 * removed
	 */
	private void refreshFrame()
	{
		frame.revalidate();
		frame.repaint();
	}

}
