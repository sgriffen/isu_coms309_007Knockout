package application.items;

import application.tools.embeddables.Location;

/**
 * Extends the range of a User's view radius by 10 meters
 * 
 * @author Sean Griffen
 */
public class Binoculars extends Item {
	
	public Binoculars() {
		
		super();
		setType(0);
		setEffectType(0);
		setEffect(10.0);
		setCost(5);
	}
	
	public Binoculars(String name, String url, String desc, int cost, int owner, double range) {
		
		super(name, url, desc, cost, owner, 0, 0, 10.0);
	}
}
