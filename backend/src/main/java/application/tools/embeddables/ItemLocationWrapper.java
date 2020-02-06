package application.tools.embeddables;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import application.items.Item;
import application.items.ItemInterface;

@Embeddable
public class ItemLocationWrapper {
	
	/**
	 * {@code Item} at {@code Location} {@code location}
	 */
	private ItemInterface item;
	
	/**
	 * {@code Location} {@code Item} {@code {item} is at
	 */
	@Embedded
	private Location location;
	
	/**
	 * Constructs an {@code ItemLocationWrapper} with {@code ItemInterface} item, and {@code Location} location
	 * @param item
	 * 		{@code ItemInterface} at {@code Location} {@code location}
	 * @param location
	 * 		{@code Location} for {@code ItemInterface} {@code item}
	 */
	public ItemLocationWrapper(ItemInterface item, Location location) {
		
		setItem(item);
		setLocation(location);
	}
	
	/**
	 * Get {@code item} of the {@code ItemLocationWrapper}
	 * @return
	 * 		{@code item}
	 */
	public ItemInterface getItem() { return item; }
	/**
	 * Set {@code item} of the {@code ItemLocationWrapper}
	 * @param item
	 * 		Desired {@code item} of the {@code ItemLocationWrapper}
	 */
	public void setItem(ItemInterface item) { this.item = item; }
	
	/**
	 * Get {@code location} of the {@code ItemLocationWrapper}
	 * @return
	 * 		{@code location}
	 */
	public Location getLocation() { return location; }
	/**
	 * Set {@code location} of the {@code ItemLocationWrapper}
	 * @param location
	 * 		Desired {@code location} of the {@code ItemLocationWrapper}
	 */
	public void setLocation(Location location) { this.location = location; }
}
