package org.jfclarkjr.java3hw3;

import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
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
	private PreparedStatement insertNewItemStatement;
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
		//ResultSet.CONCUR_READ_ONLY
		
		connectedToDatabase = true;
		
		// Execute the query
		setQuery(query);
		
		// Specify the SQL Prepared Statements
		insertNewItemStatement = connection.prepareStatement("INSERT INTO mediaitems VALUES " + 
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
	
	public void execSQLManipulation(String sqlManipulation) throws SQLException, IllegalStateException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		statement.execute(sqlManipulation);
	}
	
	public void createItem(String type, String title, String artist) throws SQLException
	{
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database!");
		
		insertNewItemStatement.setString(1, type);
		insertNewItemStatement.setString(2, title);
		insertNewItemStatement.setString(3, artist);
		
		insertNewItemStatement.executeUpdate();
	}
	
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
	 * This method updates an inventory item in the Properties table
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 * @param type The media type - CD, DVD, or Book
	 * @param title The title of the media item
	 * @param artist Depending on the media type, this will be the musician, author, or director of the item
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
	 * Deletion of an inventory item in the Properties table
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 */
	public void deleteItem(String inventoryNumber) throws SQLException
	{
		deleteItemStatement.setString(1, inventoryNumber);
		deleteItemStatement.executeUpdate();
	}
	
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
