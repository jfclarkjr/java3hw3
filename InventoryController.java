package org.jfclarkjr.java3hw2;

/**
 * InventoryController is class used for managing an inventory system composed
 * of DVDs, CDs, and Books
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryController
{
	private InventoryModel model;
	
	
	public InventoryController(InventoryModel model)
	{
		this.model = model;
	}
	
	/**
	 * This method adds a new inventory item to the Properties table
	 * 
	 * @param type  Media type
	 * @param title  Title of the CD/DVD/Book
	 * @param artist  Musician/Director/Author depending on the media type
	 */
	public void createItem(String type, String title, String artist)
	{
		model.createItem(type, title, artist);
	}
	
	/**
	 * This method retrieves an item from the Properties table
	 * The lookup is performed by title only
	 * 
	 * @param title Title of the CD/DVD/Book
	 */
	public void retrieveItemByTitle(String title)
	{
		model.retrieveItemByTitle(title);
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
		model.updateItem(inventoryNumber, type, title, artist);
	}
	
	/**
	 * Deletion of an inventory item in the Properties table
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 */
	public void deleteItem(String inventoryNumber)
	{
		model.deleteItem(inventoryNumber);
	}

}
