package application.sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.core.style.ToStringCreator;

import application.items.Item;
import application.items.ItemInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.users.User;
import application.users.UserInterface;

/**
 * Defines instance variables, getters, setters, and misc methods common to all {@code Session} types
 * See {@code SessionInterface} for details on getters and setters for instance variables
 * 
 * @author Sean Griffen
 */
@Entity
@Table(name = "Sessions")
public class Session implements SessionInterface {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * ID of the {@code Session}
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	/**
	 * List of the users in the {@code Session}
	 */
	@OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
//	@JoinTable(name = "sessions_users")
	private List<UserInterface> users;
	
	/**
	 * {@code Items} this {@code Session} has
	 */
	@ManyToMany(targetEntity = Item.class, fetch = FetchType.EAGER)
	private Set<ItemInterface> items;
	
	/**
	 * {@code Items} with {@code Locations} this {@code Session} has
	 */
	@ManyToMany(targetEntity = Item.class, fetch = FetchType.EAGER)
//	@JoinTable(name = "sessions_i_loc")
	private Map<Location, Item> iLoc;
	
	/**
	 * Name of the {@code Session}
	 */
	String name;
	
	/**
	 * Location of the center of the {@code Session}'s play area
	 */
	@Embedded
	private Location center;
	
	/**
	 * Radius of the {@code Session}'s play area
	 */
	private double radius;
	
	/**
	 * {@code Token} of the {@code Session} 
	 */
	@Embedded
	private Token token;
	
	/**
	 * Passcode to add {@code User} to this {@code Session}
	 */
	private int passcode;
	
	/**
	 * 0 if the {@code Session} has not been started, 1 otherwise
	 */
	private int started;
	
	/**
	 * Time to add a new {@code Item} to the {@code Session}
	 */
	private long randTime;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/******************************************************************* START CONSTRUCTORS ******************************************************************/
	
	/**
	 * Default constructor
	 */
	public Session() {
		
		setName(null);
		setUsers(new ArrayList<UserInterface>());
		setCenter(new Location());
		setRadius(0);
		setToken(new Token());
		setPasscode(0);
		setStarted(0);
		setILoc(new HashMap<>());
		setRandTime(0);
}
	
	/**
	 * Construct a Server with a name, center and a radius to define the play area
	 * @param name
	 * 		Desired {@code name} of the {@code Server}
	 * @param center
	 * 		Desired {@code center} of the {@code Server}
	 * @param radius
	 * 		Desired {@code radius} of the {@code Server}
	 */
	public Session(String name, Location center, double radius) {
		
		this();
		
		setName(name);
		setCenter(center);
		setRadius(radius);
	}
	
	public Session(String name, Location center, double radius, List<UserInterface> users) {
		
		this();
		
		setName(name);
		setCenter(center);
		setRadius(radius);
		setUsers(users);
	}
	
	/******************************************************************** END CONSTRUCTORS *******************************************************************/
	
	/***************************************************************** START GETTERS/SETTERS *****************************************************************/
		
	@Override
	public int getId() {return id; }
	@Override
	public void setId(int id) { this.id = id; }
	
	@Override
	public String getName() { return name; }
	@Override
	public void setName(String name) { this.name = name; }
	
	@Override
	public Location getCenter() { return center; }
	@Override
	public void setCenter(Location center) { this.center = center; }

	@Override
	public double getRadius() { return radius; }
	@Override
	public void setRadius(double radius) { this.radius = radius; }

	@Override
	public List<UserInterface> getUsers() { return users; }
	@Override
	public void setUsers(List<UserInterface> users) { this.users = users; }
	
	@Override
	public Set<ItemInterface> getItems() { return items; }
	@Override
	public void setItems(Set<ItemInterface> items) { this.items = items; }

	@Override
	public Token getToken() { return token; }
	@Override
	public void setToken(Token token) { this.token = token; }
	
	@Override
	public int getPasscode() { return passcode; }
	@Override
	public void setPasscode(int passcode) { this.passcode = passcode; }
	
	@Override
	public int getStarted() { return started; }
	@Override
	public void setStarted(int started) { this.started = started; }
	
	@Override
	public long getRandTime() { return randTime; }
	@Override
	public void setRandTime(long randTime) { this.randTime = randTime; }
	
	@Override
	public Map<Location, Item> getILoc() { return iLoc; }
	@Override
	public void setILoc(Map<Location, Item> iLoc) { this.iLoc = iLoc; }
	
	/******************************************************************* END GETTERS/SETTERS *****************************************************************/
	
	/******************************************************************** START MISC METHODS *****************************************************************/
	
	@Override
	public void addUser(UserInterface toAdd) { users.add(toAdd); }
	
	@Override
	public boolean inPlayArea(Location check) {
		
		double distance = center.getDistance(check);
		
		if (distance <= radius) { return true; }
		return false;
	}
	
	@Override
	public String toString() {
		
		return new ToStringCreator(this)
				
				.append("ID", this.getId())
				.append("Name", this.getName())
				.append("Passcode", this.getPasscode())
				.append("Center", this.getCenter())
				.append("Radius", this.getRadius())
				.append("Users", this.getUsers())
//				.append("Items", this.getItems())
				.append("Token", this.getToken())
				.toString();
	}
	
	/********************************************************************* END MISC METHODS ******************************************************************/
	
	/********************************************************************** END CLASS USER *******************************************************************/
	
}