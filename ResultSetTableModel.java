package org.jfclarkjr.java3hw3;

import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.table.AbstractTableModel;


public class ResultSetTableModel extends AbstractTableModel
{
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	private PreparedStatement insertNewItem;
	
	private boolean connectedToDatabase = false;
	
	// Constructor
	public ResultSetTableModel(String url, String username, String password, String query) throws SQLException
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
		insertNewItem = connection.prepareStatement("INSERT INTO mediaitems VALUES " + 
				"(NULL, ?, ?, ?)");
		/*
		insertNewItem = connection.prepareStatement("INSERT INTO mediaitems VALUES " + 
				"(ID, mediatype, title, artist) " + "VALUES (?, ?, ?, ?)");
		*/
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
		insertNewItem.setString(1, type);
		insertNewItem.setString(2, title);
		insertNewItem.setString(3, artist);
		
		insertNewItem.executeUpdate();
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
