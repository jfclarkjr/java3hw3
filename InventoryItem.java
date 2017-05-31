package org.jfclarkjr.java3hw3;

/**
 * InventoryItem is a class for storing an inventory object that can be 
 * a DVD, CD, or Book.
 * <p>
 * This class implements the Comparable interface
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryItem
{
	private String inventoryNumber;
	private String type;
	private String title;
	private String artist;
	
	public InventoryItem()
	{
		this.inventoryNumber="";
		this.type="";
		this.title="";
		this.artist="";
	}
	
	public InventoryItem(String inventoryNumber, String type, String title, String artist)
	{
		this.inventoryNumber=inventoryNumber;
		this.type="";
		this.title=title;
		this.artist=artist;
	}
	
	/**
	 * This method sets the values for the InventoryItem object
	 * 
	 * @param inventoryNumber The unique inventory number for each item
	 * @param title The title of the media item
	 * @param artist Depending on the media type, this will be the musician, author, or director of the item
	 */
	public void setValues(String inventoryNumber, String title, String artist)
	{
		this.inventoryNumber=inventoryNumber;
		this.title=title;
		this.artist=artist;
	}
	
	/**
	 * Method to retrieve the Inventory Number of the inventory item
	 * 
	 * @return a String with the Inventory Number
	 */
	public String getinventoryNumber()
	{
		return inventoryNumber;
	}
	
	/**
	 * Method to retrieve the Media Type of the inventory item
	 * 
	 * @return a String with the Media Type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Method to retrieve the Title of the inventory item
	 * 
	 * @return a String representing the Title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Method to retrieve the Artist of the inventory item
	 * 
	 * @return a String representing the Artist
	 */
	public String getArtist()
	{
		return artist;
	}
	
}
