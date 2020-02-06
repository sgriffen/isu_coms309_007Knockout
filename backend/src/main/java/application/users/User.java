package application.users;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import application.items.Item;
import application.items.ItemInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;

/**
 * Defines instance variables, getters, setters, and misc methods common to all {@code User} types
 * See {@code UserInterface} for details on getters and setters for instance variables
 * 
 * @author Sean Griffen
 */
@Entity
@Table(name = "Users")
public class User implements UserInterface {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * ID of the {@code User}
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	/**
	 * User name of the {@code User}
	 */
	private String username;
	
	/**
	 * Password of the {@code User}
	 */
	private String passwordd;
	
	/**
	 * Authorization level of the User. 0 means player, 1 means Moderator, 2 means Administrator
	 */
	private int authLevel;
	
	/**
	 * Current level of the {@code User}
	 */
	private int levell;	
	
	/**
	 * Total number of kills of the {@code User}
	 */
	private int kills;
	
	/**
	 * Total number of deaths of the {@code User}
	 */
	private int deaths;
	
	/**
	 * Amount of money the {@code User} has
	 */
	private double currency;
	
	/**
	 * View radius of the area around the {@code User}
	 */
	private double viewRadius;
	
	/**
	 * Kill radius of the {@code User}
	 */
	private double killRadius;
	
	/**
	 * Time last updated for the {@code User}
	 */
	private long timeUpdated;
	
	/**
	 * List of {@code Items} the {@code User} has
	 */
	@ManyToMany(targetEntity = Item.class, fetch = FetchType.EAGER)
	private Set<ItemInterface> items;
	
	/**
	 * List of {@code User}s this {@code User} is assigned to eliminate
	 */
	@OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
	@JsonIgnore
//	@JoinTable(name = "users_targets")
	private List<UserInterface> targets;
	
	/**
	 * {@code Location} of the {@code User}
	 */
	@Embedded
	private Location location;
	
	/**
	 * {@code Token} of the {@code User}
	 */
	@Embedded
	private Token token;
	
	/**
	 * {@code Token} of the {@code Session} of the {@code User}
	 */
	@ElementCollection(fetch = FetchType.EAGER)
//	@JoinTable(name = "user_session")
	private Set<Token> session;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/******************************************************************* START CONSTRUCTORS ******************************************************************/
	
	/**
	 * Default Constructor for {@code User}
	 */
	public User() {
		
		setAuthLevel(0);
		setLevel(1);
		setKills(0);
		setDeaths(0);
		setCurrency(0.0);
		setViewRadius(30.0);
		setKillRadius(1.0);
		setItems(new HashSet<>());
		setLocation(new Location());
		setToken(new Token());
		setSession(new HashSet<>());
		setTargets(new ArrayList<>());
	}
	
	/**
	 * Create {@code User} with user name {@code username}, and password {@code password}
	 * @param username
	 * 		Desired {@code username} of the {@code User}
	 * @param password
	 * 		Desired {@code password} of the {@code User}
	 */
	public User(String username, String password) {
		
		this();
		
		setUsername(username);
		setPassword(password);
		location = new Location();
		
		String[] s = { username, password };
		token = new Token(s, 24);
	}
	
	/**
	 * Create {@code User} with user name {@code username}, password {@code password}, and authorization level {@code authLevel}
	 * @param username
	 * 		Desired {@code username} of the {@code User}
	 * @param password
	 * 		Desired {@code password} of the {@code User}
	 * @param auth
	 * 		Desired {@code authLevel} of the {@code User}
	 */
	public User(String username, String password, int auth) {
		
		this();
		
		setUsername(username);
		setPassword(password);
		setAuthLevel(auth);
		location = new Location();
		
		String[] s = { username, password };
		token = new Token(s, 24);
		
		this.session = new HashSet<Token>();
	}
	
	/**
	 * Create User with ID user name {@code username}, password {@code password}, authorization level {@code authLevel}, and {@code Location} data {@code latitude}, {@code longitude}, and {@code accuracy}
	 * @param username
	 * 		Desired {@code username} of the {@code User}
	 * @param password
	 * 		Desired {@code password} of the {@code User}
	 * @param auth
	 * 		Desired {@code authLevel} of the {@code User}
	 * @param location
	 * 		Desired {@code location} of the {@code User}
	 */
	public User(String username, String password, int auth, Location location) {
		
		this();
		
		setUsername(username);
		setPassword(password);
		setAuthLevel(auth);
		setLocation(location);
		
		String[] s = { username, password };
		setToken(new Token(s, 24));
	}
	
	/******************************************************************** END CONSTRUCTORS *******************************************************************/
	
	/***************************************************************** START GETTERS/SETTERS *****************************************************************/
	
	@Override
	public int getId() { return id; }
	@Override
	public void setId(int id) { this.id = id; }
	
	@Override
	public String getUsername() { return username; }
	@Override
	public void setUsername(String username) { this.username = username; }
	
	@Override
	public String getPassword() { return passwordd; }
	@Override
	public void setPassword(String passwordd) { this.passwordd = passwordd; }	
	
	@Override
	public int getLevel() { return levell; }
	@Override
	public void setLevel(int levell) { this.levell = levell; }
	
	@Override
	public int getKills() { return kills; }
	@Override
	public void setKills(int kills) { this.kills = kills; }
	
	@Override
	public int getDeaths() { return deaths; }
	@Override
	public void setDeaths(int deaths) { this.deaths = deaths; }
	
	@Override
	public double getKDRatio() {
		
		if (deaths == 0) { return kills; }
		return kills / deaths;
	}
	
	@Override
	public double getCurrency() { return currency; }
	@Override
	public void setCurrency(double currency) { this.currency = currency; }
	
	@Override
	public int getAuthLevel() { return authLevel; }
	@Override
	public void setAuthLevel(int authLevel) { this.authLevel = authLevel; }
	
	@Override
	public double getViewRadius() {
		
		double buff = 0.0;
		for (ItemInterface i : items) {
			
			if (i.getType() == 0 && i.getEffectType() == 0) { buff += i.getEffect(); }
		}
		return viewRadius + buff;
	}
	@Override
	public void setViewRadius(double viewRadius) { this.viewRadius = viewRadius; }
	
	@Override
	public double getKillRadius() {
		
		double buff = 0.0;
		for (ItemInterface i : items) {
			
			if (i.getType() == 0 && i.getEffectType() == 1) { buff += i.getEffect(); }
		}
		
		return killRadius + buff;
	}
	@Override
	public void setKillRadius(double killRadius) { this.killRadius = killRadius; }
	
	@Override
	public long getTimeUpdated() { return timeUpdated; }
	@Override
	public void setTimeUpdated(long timeUpdated) { this.timeUpdated = timeUpdated; }
	
	@Override
	public Set<ItemInterface> getItems() { return items; }
	@Override
	public void setItems(Set<ItemInterface> items) { this.items = items; }
	
	@Override
	public List<UserInterface> getTargets() { return targets; }
	@Override
	public void setTargets(List<UserInterface> targets) { this.targets = targets; }

	@Override
	public Location getLocation() { return location; }
	@Override
	public void setLocation(Location location) { this.location = location; }
	
	@Override
	public Token getToken() { return token; }
	@Override
	public void setToken(Token token) { this.token = token; }
	
	@Override
	public Set<Token> getSession() { return this.session; }
	@Override
	public void setSession(Set<Token> session) { this.session = session; }
	
	/******************************************************************* END GETTERS/SETTERS *****************************************************************/

	/******************************************************************** START MISC METHODS *****************************************************************/
	
	@Override
	public boolean equal(UserInterface user) {
		
		if (/*this.getDeaths() == user.getDeaths() &&*/ this.getId() == user.getId() && /*this.getKDRatio() == user.getKDRatio() && 
			this.getKills() == user.getKills() && this.getLevel() == user.getLevel() &&*/ this.getPassword().contentEquals(user.getPassword()) && 
			this.getUsername().equals(user.getUsername()) /* && this.getToken().getKey().equals(user.getToken().getKey())*/) {
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		
		if (this.getUsername() == null || this.getPassword() == null) { return true; }
		if (this.getUsername().trim().isEmpty() || this.getPassword().trim().isEmpty()) { return true; }
		
		return false;
	}
	
	@Override
	public boolean updateInfo(UserInterface user) {
		
		boolean result = false;
		
		if (user.getUsername() != null) {
			if(!user.getUsername().isEmpty()) {
				if (!this.getUsername().equals(user.getUsername())) {
					
					this.setUsername(user.getUsername());
					result = true;
				}
			}
		}
		
		if (user.getPassword() != null) {
			if(!user.getPassword().isEmpty()) {
				if (!user.getPassword().equals(this.getPassword())) {
					
					this.setPassword(user.getPassword());
					result = true;
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		
		return new ToStringCreator(this)
				
				.append("ID", this.getId())
				.append("User name", this.getUsername())
				.append("Password", this.getPassword())
				.append("Authorization Level", this.getAuthLevel())
				.append("Level", this.getLevel())
				.append("Kills", this.getKills())
				.append("Deaths", this.getDeaths())
				.append("View Range", this.getViewRadius())
				.append("Kill Range", this.getKillRadius())
				.append("Location", this.getLocation())
				.append("Token", this.getToken())
				.append("Targets", this.getTargets())
				.toString();
	}
	
	/********************************************************************* END MISC METHODS ******************************************************************/
	
	/********************************************************************** END CLASS USER *******************************************************************/
}
