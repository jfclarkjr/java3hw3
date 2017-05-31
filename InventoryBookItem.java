package org.jfclarkjr.java3hw2;

/**
 * InventoryBookItem is a class for storing an inventory object of type Book
 * <p>
 * This class extends the InventoryItem class
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryBookItem extends InventoryItem
{
	/**
	 * Method to return the media type.
	 * 
	 */
	@Override
	public String getType()
	{
		return "Book Media";
	}
}
