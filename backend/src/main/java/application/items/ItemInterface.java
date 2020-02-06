package application.items;

import application.tools.embeddables.Location;

/**
 * @author Theodore Davis
 * Declares methods all items are required to have.
 * See @Code Item
 */
public interface ItemInterface {
	
	public int getId();
	public void setId(int id);
	
	public int getOwner();
	public void setOwner(int owner);
	
	public String getURL();
	public void setURL(String imageURL);
	
	public String getDescription();
	public void setDescription(String desc);
	
	public double getCost();
	public void setCost(double cost);
	
	public int getSessionId();
	public void setSesionId(int server);
	
	public int getType();
	public void setType(int type);
	
	public int getEffectType();
	public void setEffectType(int effectType);
	
	public double getEffect();
	public void setEffect(double effect);
	
	public String getName();
	public void setName(String name);
}
