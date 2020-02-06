package application.items;

import application.tools.embeddables.Location;

/**
 * Extends the range of a User's view radius by 10 meters
 * 
 * @author Sean Griffen
 */
public class Sword extends Item {
	
	public Sword() {
		
		super();
		setType(0);
		setEffectType(1);
		setEffect(1.0);
		setCost(10);
	}
	
	public Sword(String name, String url, String desc, int cost, int owner, double range) {
		
		super(name, url, desc, cost, owner, 0, 1, 1.0);
	}
}
