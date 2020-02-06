package application.tools.wrappers;

import application.tools.embeddables.Location;
import application.tools.embeddables.Token;

/**
 * Used to update the {@code Location} of a {@code User}
 * 
 * @author Sean Griffen
 */
public class LocationUpdateWrapper {
	
	/**
	 * {@code Token} of the {@code User} to update
	 */
	private Token toUpdate;
	
	/**
	 * {@code Location} to update with
	 */
	private Location newLocation;

	/**
	 * Get {@code toUpdate} of the {@code LocationUpdateWrapper}
	 * @return
	 * 		{@code toUpdate}
	 */	
	public Token getToUpdate() { return toUpdate; }
	/**
	 * Set {@code toUpdate} of the {@code LocationUpdateWrapper}
	 * @param toUpdate
	 * 		Desired {@code toUpdate} of the {@code LocationUpdateWrapper}
	 */
	public void setToUpdate(Token toUpdate) { this.toUpdate = toUpdate; }
	
	/**
	 * Get {@code newLocation} of the {@code LocationUpdateWrapper}
	 * @return
	 * 		{@code newLocation}
	 */	
	public Location getNewLocation() { return newLocation; }
	/**
	 * Set {@code newLocation} of the {@code LocationUpdateWrapper}
	 * @param newLocation
	 * 		Desired {@code newLocation} of the {@code LocationUpdateWrapper}
	 */
	public void setNewLocation(Location newLocation) { this.newLocation = newLocation; }
}
