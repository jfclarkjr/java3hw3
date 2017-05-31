package org.jfclarkjr.java3hw3;

import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import javax.swing.table.AbstractTableModel;


public class ResultSetTableModel extends AbstractTableModel
{
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	private boolean connectedToDatabase = false;
	
	// Constructor
	public ResultSetTableModel(String url, String username, String password, String query) throws SQLException
	{
		// Establish a connection to the database
		connection = DriverManager.getConnection(url, username, password);
		
		// Create statement
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		connectedToDatabase = true;
		
		// Execute the query
		setQuery(query);
		
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
}
