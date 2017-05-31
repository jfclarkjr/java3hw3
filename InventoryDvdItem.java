package org.jfclarkjr.java3hw2;

/**
 * InventoryDvdItem is a class for storing an inventory object of type DVD
 * <p>
 * This class extends the InventoryItem class
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryDvdItem extends InventoryItem
{
	/**
	 * Method to return the media type.
	 * 
	 */
	@Override
	public String getType()
	{
		return "DVD Media";
	}

}
