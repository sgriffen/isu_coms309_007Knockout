package application.items;

import java.util.ArrayList;
import java.util.List;

import application.tools.embeddables.Location;
import application.users.UserInterface;

/**
 * Entity on the map that notifies the {@code User} that holds this {@code Item} when their {@code target} walks within its are of view
 * 
 * @author Sean Griffen, Theodore Davis
 */
public class Bomb extends Item {
	
	public Bomb() {
		
		super();
		setEffect(20.0);
		setType(1);
		setEffectType(0);
		setCost(20);
	}
	
	public Bomb(String name, String url, String desc, int cost, int owner, double range) {
		
		super(name, url, desc, cost, owner, 1, 1, 15.0);
	}
	
//	/**
//	 * Checks if a location is inside the range of the Camera
//	 * @param loc
//	 * 		Location to check
//	 * @return
//	 * 		True if loc is in range, false otherwise
//	 */
//	public boolean locationInRange(Location loc) {
//		
//		if (this.getLocation().getDistance(loc) <= super.getEffect()) { return true; }
//		return false;
//	}
//	
//	/**
//	 * Returns a list of Users inside the range of the Camera, given a list of Users to check
//	 * @param users
//	 * 		List of Users to check
//	 * @return
//	 * 		List of Users in the range
//	 */
//	public List<UserInterface> getUsersInRange(List<UserInterface> users) {
//		
//		List<UserInterface> inRange = new ArrayList<>();
//		
//		for (UserInterface u : users) {
//			
//			if (this.getLocation().getDistance(u.getLocation()) <= super.getEffect()) { inRange.add(u); }
//		}
//		
//		return inRange;
//	}
}
