package org.jfclarkjr.java3hw3;

import java.sql.SQLException;

import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * InventoryApp is a class containing the main method for accessing an 
 * inventory system composed of DVDs, CDs, and Books
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryApp
{
	public static void main(String[] args) throws SQLException
	{
		String DATABASE_URL = "jdbc:mysql://localhost:3306/mediainventory?useSSL=false";
		String USERNAME = "root";
		String PASSWORD = "root";
		String DEFAULT_QUERY = "SELECT * FROM mediaitems";
		
		InventoryModel model = new InventoryModel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);
		InventoryController controller= new InventoryController(model);
		InventoryView view = new InventoryView(controller);
		
		// Register the view and model objects with each other
		model.setView(view);
		view.setModel(model);
		
		// Start the interactive user interface
		view.start();
	}
}
