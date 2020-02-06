package application.tools.wrappers;

import application.tools.embeddables.Token;

/**
 * Used to update the Inventory of an {@code User}
 * 
 * @author Sean Griffen
 */
public class InventoryUpdateWrapper {
	
	/**
	 * {@code Token} of the {@code User} to update
	 */
	private Token toUpdate;
	
	/**
	 * Array of {@code id}s of the {@code Items} to update the {@code User} with
	 */
	private int[] items;
	
	/**
	 * Get {@code toUpdate} of the {@code InventoryUpdateWrapper}
	 * @return
	 * 		{@code toUpdate}
	 */
	public Token getToUpdate() { return toUpdate; }
	/**
	 * Set {@code toUpdate} of the {@code InventoryUpdateWrapper}
	 * @param toUpdate
	 * 		Desired {@code toUpdate} of the {@code InventoryUpdateWrapper}
	 */
	public void setToUpdate(Token toUpdate) { this.toUpdate = toUpdate; }
	
	/**
	 * Get {@code items} of the {@code InventoryUpdateWrapper}
	 * @return
	 * 		{@code items}
	 */
	public int[] getItems() { return items; }
	/**
	 * Set {@code items} of the {@code InventoryUpdateWrapper}
	 * @param items
	 * 		Desired {@code items} of the {@code InventoryUpdateWrapper}
	 */
	public void setItems(int[] items) { this.items = items; }
}
