package application.sessions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import application.items.Item;
import application.items.ItemInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.users.UserInterface;

/**
 * Interface for {@code Session} objects and sub types
 * Defines basic methods common to all {@code Session} types
 * 
 * @author Sean Griffen
 */
public interface SessionInterface {
		
	/**
	 * Get {@code id} of the {@code Session}
	 * @return
	 * 		{@code id}
	 */
	public int getId();
	/**
	 * Set {@code id} of the {@code Session}
	 * @param id
	 * 		Desired {@code id} of the {@code Session}
	 */
	public void setId(int id);
	
	/**
	 * Get {@code name} of the {@code Session}
	 * @return
	 * 		{@code name}
	 */
	public String getName();
	/**
	 * Set {@code name} of the {@code Session}
	 * @param name
	 * 		Desired {@code name} of the {@code Session}
	 */
	public void setName(String name);
	
	/**
	 * Get {@code center} of the {@code Session}'s play area
	 * @return
	 * 		{@code center}
	 */
	public Location getCenter();
	/**
	 * Set {@code center} of the {@code Session}'s play area
	 * @param center
	 * 		Desired {@code center} of the {@code Session}'s play area
	 */
	public void setCenter(Location center);
	
	/**
	 * Get {@code radius} of the {@code Session}'s play area
	 * @return
	 * 		{@code radius}
	 */
	public double getRadius();
	/**
	 * Set {@code radius} of the {@code Session}'s play area
	 * @param radius
	 * 		Desired {@code radius} of the {@code Session}'s play area
	 */
	public void setRadius(double radius);
	
	/**
	 * Get {@code users} active in the {@code Session}
	 * @return
	 * 		{@code users}
	 */
	public List<UserInterface> getUsers();
	/**
	 * Set {@code users} in the {@code Session}
	 * @param users
	 * 		Desired {@code users} in the {@code Session}
	 */
	public void setUsers(List<UserInterface> users);
	
	/**
	 * Get {@code items} active in the {@code Session}
	 * @return
	 * 		{@code items}
	 */
	public Set<ItemInterface> getItems();
	/**
	 * Set {@code items} in the {@code Session}
	 * @param items
	 * 		Desired {@code items} in the {@code Session}
	 */
	public void setItems(Set<ItemInterface> items);
	
	/**
	 * Get {@code token} of the {@code Session}
	 * @return
	 * 		{@code token}
	 */
	public Token getToken();
	/**
	 * Set {@code Token} of the {@code Session}
	 * @param token
	 * 		Desired {@code Token} of the {@code Session}
	 */
	public void setToken(Token token);
	
	/**
	 * Get {@code passcode} of the {@code Session}
	 * @return
	 * 		{@code passcode}
	 */
	public int getPasscode();
	/**
	 * Set {@code passcode} of the {@code Session}
	 * @param passcode
	 * 		Desired {@code passcode} of the {@code Session}
	 */
	public void setPasscode(int passcode);
	
	/**
	 * Get {@code started} of the {@code Session}
	 * @return
	 * 		{@code started}
	 */
	public int getStarted();
	/**
	 * Set {@code started} of the {@code Session}
	 * @param started
	 * 		Desired {@code started} of the {@code Session}
	 */
	public void setStarted(int started);
	
	/**
	 * Get {@code long} of the {@code Session}
	 * @return
	 * 		{@code long}
	 */
	public long getRandTime();
	/**
	 * Set {@code long} of the {@code Session}
	 * @param long
	 * 		Desired {@code long} of the {@code Session}
	 */
	public void setRandTime(long randTime);
	
	/**
	 * Get {@code iLoc} of the {@code Session}
	 * @return
	 * 		{@code iLoc}
	 */
	public Map<Location, Item> getILoc();
	/**
	 * Set {@code iLoc} of the {@code Session}
	 * @param iLoc
	 * 		Desired {@code iLoc} of the {@code Session}
	 */
	public void setILoc(Map<Location, Item> iLoc);
	
	/**
	 * Add a {@code User} to the {@code Session}
	 * @param toAdd
	 * 		{@code User} to add
	 */
	public void addUser(UserInterface toAdd);
	
	/**
	 * Checks if a given {@code Location} is within the play area
	 * @param check
	 * 		{@code Location} to check
	 * @return
	 * 		{@code true} if {@code check} is in the play area, {@code false} otherwise
	 */
	public boolean inPlayArea(Location check);
	
	/**
	 * Converts {@code User} to a {@code String}
	 * @return
	 * 		{@code String} version of the {@code Session}
	 */
	public String toString();
	
}
