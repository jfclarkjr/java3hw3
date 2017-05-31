package org.jfclarkjr.java3hw3;

/**
 * InventoryCdItem is a class for storing an inventory object of type CD
 * <p>
 * This class extends the InventoryItem class
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public class InventoryCdItem extends InventoryItem
{
	/**
	 * Method to return the media type.
	 * 
	 */
	@Override
	public String getType()
	{
		return "CD Media";
	}

}
