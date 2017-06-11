package org.jfclarkjr.java3hw3;

import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.table.AbstractTableModel;


public class InventoryModel extends AbstractTableModel
{
	private InventoryView view;
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	private PreparedStatement createItemStatement;
	private PreparedStatement retrieveItemStatement;
	private PreparedStatement updateItemStatement;
	private PreparedStatement deleteItemStatement;
	
	private boolean connectedToDatabase = false;
	
	// Constructor
	public InventoryModel(String url, String username, String password, String query) throws SQLException
	{
		// Establish a connection to the database
		connection = DriverManager.getConnection(url, username, password);
		
		// Create statement
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		connectedToDatabase = true;
		
		// Execute the query
		setQuery(query);
		
		// Specify the SQL Prepared Statements for CRUD operations on the database
		createItemStatement = connection.prepareStatement("INSERT INTO mediaitems VALUES " + 
				"(NULL, ?, ?, ?)");
		
		retrieveItemStatement = connection.prepareStatement("SELECT * from mediaitems WHERE title = ?");
		
		updateItemStatement = connection.prepareStatement(
				"UPDATE mediaitems SET mediatype = ?, title = ?, artist = ? WHERE ID = ?");
		
		deleteItemStatement = connection.prepareStatement("DELETE FROM mediaitems WHERE ID = ?");

	}
	
	/**
	 * This method will register the view object
	 * 
	 * @param view The inventory view object which provides the user interface
	 */
	public void setView(InventoryView view)
	{
		this.view=view;
	}
	
	public String getColumnName(int column) throws IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		try
		{
			return metaData.getColumnName(column +1);
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		
		// Return a null String if an error occurs
		return "";
	}

	public Class getColumnClass(int column) throws IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		// Determine the class type of the column
		try
		{
			String className = metaData.getColumnClassName(column +1);
			
			// Return the class type
			return Class.forName(className);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		// Return type Object if error occurs
		return Object.class;
	}
	
	public int getColumnCount() throws IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		try
		{
			return metaData.getColumnCount();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		
		// If exception occurs, return 0
		return 0;
	}
	
	public int getRowCount() throws IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		return numberOfRows;
	}
	
	public Object getValueAt(int row, int column) throws IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		try
		{
			// Get value at specified row and column
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		
		// If exception occurs, return empty String
		return "";
		
	}
	
	/**
	 * A method to execute a query against the database
	 * 
	 * @param query
	 * @throws SQLException
	 * @throws IllegalStateException
	 */
	public void setQuery(String query) throws SQLException, IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		// Execute query
		resultSet = statement.executeQuery(query);
		
		
		// Get metadata
		metaData = resultSet.getMetaData();
		
		// Get the number of rows to display
		resultSet.last();
		numberOfRows = resultSet.getRow();
		
		// Update JTable
		fireTableStructureChanged();
	}
	
	/**
	 * This method adds a new inventory item to the database.
	 * 
	 * @param type  Media type
	 * @param title  Title of the CD/DVD/Book
	 * @param artist  Musician/Director/Author depending on the media type
	 * @throws SQLException
	 */
	public void createItem(String type, String title, String artist) throws SQLException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		createItemStatement.setString(1, type);
		createItemStatement.setString(2, title);
		createItemStatement.setString(3, artist);
		
		createItemStatement.executeUpdate();
	}
	
	/**
	 * This method retrieves an item from the database
	 * The lookup is performed by title only
	 * 
	 * @param title Title of the CD/DVD/Book
	 * @throws SQLException
	 */
	public void retrieveItemByTitle(String title) throws SQLException
	{
		InventoryStateChange currentStateChange = null;
		
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		retrieveItemStatement.setString(1, title);
		resultSet = retrieveItemStatement.executeQuery();
		
		// Refresh the JTable with results of the query
		updateTable();
		
		// Notify the view of the state change of the JTable
		currentStateChange = Enum.valueOf(InventoryStateChange.class, "RETRIEVE");
		view.notifyOfModelStateChange(currentStateChange);

	}
	
	/**
	 * Update the JTable with the full list of inventory items
	 * 
	 * @throws SQLException
	 */
	public void getEntireTable() throws SQLException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		String query = "SELECT * from mediaitems";
		
		setQuery(query);
		
		// Refresh the JTable with results of the query
		updateTable();
	}
	
	/**
	 * This method updates an inventory item in the database
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 * @param type The media type - CD, DVD, or Book
	 * @param title The title of the media item
	 * @param artist Depending on the media type, this will be the musician, author, or director of the item
	 * @throws SQLException
	 */
	public void updateItem(String inventoryNumber,String type, String title, String artist) throws SQLException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		updateItemStatement.setString(4, inventoryNumber);
		updateItemStatement.setString(1, type);
		updateItemStatement.setString(2, title);
		updateItemStatement.setString(3, artist);
		
		updateItemStatement.executeUpdate();
	}
	
	/**
	 * Deletion of an inventory item in the database
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 * @throws SQLException
	 */
	public void deleteItem(String inventoryNumber) throws SQLException
	{
		deleteItemStatement.setString(1, inventoryNumber);
		deleteItemStatement.executeUpdate();
	}
	
	/**
	 * Method to refresh the JTable with results of a query
	 * 
	 * @throws SQLException
	 */
	private void updateTable() throws SQLException
	{
		// Get metadata
		metaData = resultSet.getMetaData();
		
		// Get the number of rows to display
		resultSet.last();
		numberOfRows = resultSet.getRow();
		
		// Update JTable
		fireTableStructureChanged();
	}
	
	/**
	 * Method to close all connections to the database
	 */
	public void disconnectFromDatabase()
	{
		if (!connectedToDatabase)
		{
			try
			{
				resultSet.close();
				statement.close();
				connection.close();
			}
			catch (SQLException sqlException)
			{
				sqlException.printStackTrace();
			}
			finally
			{
				connectedToDatabase = false;
			}
		}
	}
}
