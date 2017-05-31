package org.jfclarkjr.java3hw2;

/**
 * InventoryStateChange is an enumeration class used for specifying the state change
 * of the Model object.  This state change can be returned to the View object
 * as part of the double dispatch communication between the objects.
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public enum InventoryStateChange
{
	CREATE, RETRIEVE, UPDATE, DELETE, ITEM_NOT_FOUND
}
