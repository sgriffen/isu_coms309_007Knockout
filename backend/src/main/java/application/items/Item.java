/**
 * 
 */
package application.items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author Theodore Davis
 * Item abstract for use mostly in the store view to make a nice iterable of the Items interface
 */
@Entity
@Table(name = "Items")
public class Item implements ItemInterface {
	
	/**
	 * key for finding items in the item table
	 * automatically generated
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	/**
	 * Location of the image for this item
	 */
	private String imageURL;
	
	/**
	 * Name of the item
	 */
	private String name;
	
	/**
	 * Description of the item
	 */
	public String des;

	/**
	 * cost of the item
	 */
	private double cost;
	
	/**
	 * id of this item's owner
	 */
	private int own;
	
	/**
	 * id of server this item is in
	 */
	private int sess;
	
	/**
	 * 0 if buff item, 1 if map entity
	 */
	private int typ;
	
	/**
	 * If buff type: 0 for view range buff, 1 for kill range buff
	 * If map type: 0 for passive (does not kill), 1 for aggressive (kills)
	 */
	private int effectType;
	
	/**
	 * Range of effect
	 */
	private double effect;
	
	/******************************************************************************************************************
	 * End of variables
	 * 
	 * Start of Methods
	 ******************************************************************************************************************/
	
	public Item() {
		
		setURL(null);
		setName(null);
		setDescription(null);
		setCost(0.0);
		setOwner(-1);
		setType(-1);
		setEffectType(-1);
		setEffect(-1);
	}
	
	/**
	 * Creates new item with respective parameters
	 * @param name Name of the item
	 * @param url String to find location of image data
	 * @param desc String to describe the object
	 * @param cost int for how muh item cost
	 * @param owner user that has the item
	 * @param type Type of the item
	 * @param effectType Effect type of the item
	 * @param effect Effect of the item
	 */
	public Item(String name, String url, String desc, int cost, int owner, int type, int effectType, double effect) {
		
		this();
		
		setName(name);
		setURL(url);
		setDescription(desc);
		setCost(cost);
		setOwner(owner);
		setType(type);
		setEffectType(effectType);
		setEffect(effect);
	}
	
	@Override
	public int getId() { return id; }
	@Override
	public void setId(int id) { this.id = id; }
	
	@Override
	public int getOwner() { return own; }
	@Override
	public void setOwner(int owner) { this.own = owner; }
	
	@Override
	public String getURL() { return imageURL; }
	@Override
	public void setURL(String imageURL) { this.imageURL = imageURL; }
	
	@Override
	public String getDescription() { return des; }
	@Override
	public void setDescription(String description) { this.des = description; }
	
	@Override
	public double getCost() { return cost; }
	@Override
	public void setCost(double cost) { this.cost = cost; }
	
	@Override
	public int getSessionId() { return sess; }
	@Override
	public void setSesionId(int server) { this.sess = server; }
	
	@Override
	public int getType() { return typ; }
	@Override
	public void setType(int type) { this.typ = type; }
	
	@Override
	public int getEffectType() { return effectType; }
	@Override
	public void setEffectType(int effectType) { this.effectType = effectType; }
	
	@Override
	public double getEffect() { return effect; }
	@Override
	public void setEffect(double effect) { this.effect = effect; }
	
	@Override
	public String getName() { return name; }
	@Override
	public void setName(String name) { this.name = name; }
}
