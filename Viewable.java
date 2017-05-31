package org.jfclarkjr.java3hw2;

/**
 * The Viewable interface defines required methods to be implemented in a View object 
 * for use with the InventoryModel object.
 * 
 * @author John Clark
 * @since 1.8
 *
 */
public interface Viewable
{
	/**
	 * A method to register a Model object
	 * 
	 * @param model The inventory model object
	 */
	public void setModel(InventoryModel model);
	
	/**
	 * A primary method for the View class
	 * This initiates the interactive user interface to access the Model class
	 */
	void start();
	
	/**
	 * A method called by the model to alert the view object that a state change
	 * has occurred.
	 * 
	 * @param stateChange An enum that specifies the change in state if the inventory
	 */
	public void notifyOfModelStateChange(InventoryStateChange stateChange);
}
