package org.jfclarkjr.java3hw2;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * InventoryModel is a class used for managing an inventory system composed
 * of DVDs, CDs, and Books
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryModel
{
	private InventoryView view;
	private Properties inventoryTable = new Properties();
	private InventoryItem currentItem;

	public InventoryModel()
	{
		inventoryTable.clear();
		loadTable(inventoryTable);
		currentItem = new InventoryItem();
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
	
	/**
	 * This method will load a Properties table from a file
	 * If the file doesn't exist, it will create a new one
	 * 
	 * @param propTable The Properties file to be loaded from a file
	 */
	private void loadTable(Properties propTable)
	{
		try
		{
			FileInputStream input = new FileInputStream("InventoryTable.data");
			propTable.load(input);
			input.close();
		}
		catch(IOException ioException)
		{
			// If no file is present, continue without error
			// A new file will be created as part of the saveTable method
		}
	}
	
	/**
	 * This method saves a Properties table to file
	 * 
	 * @param propTable The Properties file to be saved to a file
	 */
	private void saveTable(Properties propTable)
	{
		try
		{
			FileOutputStream output = new FileOutputStream("InventoryTable.data");
			propTable.store(output, "Inventory Table");
			output.close();
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	
	/**
	 * This method adds a new inventory item to the Properties table.
	 * It steps through the existing inventory table until it finds an
	 * unallocated inventory number.  The new item is then added using
	 * the number found.
	 * 
	 * @param type  Media type
	 * @param title  Title of the CD/DVD/Book
	 * @param artist  Musician/Director/Author depending on the media type
	 */
	public void createItem(String type, String title, String artist)
	{
		Integer inventoryNumber=1;
		
		// Step through the Inventory Table until we find an unallocated inventoryNumber
		while (inventoryTable.containsKey(inventoryNumber.toString()) == true)
		{
			inventoryNumber++;
		}
 
		inventoryTable.setProperty(inventoryNumber.toString(), type +"/" + title + "/" + artist);
		saveTable(inventoryTable);
	}
	
	/**
	 * This method retrieves an item from the Properties table
	 * The lookup is performed by title only
	 * 
	 * @param title Title of the CD/DVD/Book
	 */
	public void retrieveItemByTitle(String title)
	{
		String currentTableEntry;
		String[] parsedLine;
		boolean itemFound=false;
		InventoryStateChange currentStateChange = null;
		InventoryDvdItem currentDvdItem = new InventoryDvdItem();
		InventoryCdItem currentCdItem = new InventoryCdItem();
		InventoryBookItem currentBookItem = new InventoryBookItem();
		
		Set<Object> keys = inventoryTable.keySet();
		
		for (Object key : keys)
		{
			currentTableEntry=inventoryTable.getProperty((String)key);
			
			parsedLine=currentTableEntry.split("/");
			if ( parsedLine[1].equalsIgnoreCase(title))
			{
				itemFound=true;
				
				if (parsedLine[0].equals("DVD"))
				{
					currentDvdItem.setValues((String)key, parsedLine[1], parsedLine[2]);
					currentItem = currentDvdItem;
				}
				else if (parsedLine[0].equals("CD"))
				{
					currentCdItem.setValues((String)key, parsedLine[1], parsedLine[2]);
					currentItem = currentCdItem;
				}
				else if (parsedLine[0].equals("Book"))
				{
					currentBookItem.setValues((String)key, parsedLine[1], parsedLine[2]);
					currentItem = currentBookItem;
				}
				
			}
			
		}
		
		if ( itemFound == true)
			currentStateChange = Enum.valueOf(InventoryStateChange.class, "RETRIEVE");
		else
			currentStateChange = Enum.valueOf(InventoryStateChange.class, "ITEM_NOT_FOUND");
		
		view.notifyOfModelStateChange(currentStateChange);
	
	}
	
	/**
	 * Provide the full Properties table, unsorted and unformatted
	 * 
	 * @return the Properties file containing all inventory items
	 */
	public Properties getEntireTable()
	{
		return inventoryTable;
	}
	
	/**
	 * This method updates an inventory item in the Properties table
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 * @param type The media type - CD, DVD, or Book
	 * @param title The title of the media item
	 * @param artist Depending on the media type, this will be the musician, author, or director of the item
	 */
	public void updateItem(String inventoryNumber,String type, String title, String artist)
	{
		inventoryTable.setProperty(inventoryNumber, type +"/" + title + "/" + artist);
		saveTable(inventoryTable);
	}
	
	/**
	 * Deletion of an inventory item in the Properties table
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 */
	public void deleteItem(String inventoryNumber)
	{
		inventoryTable.remove(inventoryNumber);
		saveTable(inventoryTable);
	}
	
	/**
	 * Returns an InventoryItem object to be used by the view object
	 * 
	 * @return an InventoryItem object
	 */
	public InventoryItem getCurrentItem()
	{
		return currentItem;
	}
	
}
