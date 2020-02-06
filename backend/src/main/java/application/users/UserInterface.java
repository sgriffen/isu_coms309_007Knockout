package application.users;

import java.util.List;
import java.util.Set;

import application.items.ItemInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;

/**
 * Interface for {@code User} objects and sub types
 * Defines basic methods common to all {@code User} types
 * 
 * @author Sean Griffen
 */
public interface UserInterface {
	
	/**
	 * Get {@code id} of the {@code User}
	 * @return
	 * 		{@code id}
	 */
	public int getId();
	/**
	 * Set {@code id} of the {@code User}
	 * @param id
	 * 		Desired {@code id} of the {@code User}
	 */
	public void setId(int id);
	
	/**
	 * Get {@code username} of the {@code User}
	 * @return
	 * 		{@code username}
	 */
	public String getUsername();
	/**
	 * Set {@code username} of the {@code User}
	 * @param username
	 * 		Desired {@code username} of the {@code User}
	 */
	public void setUsername(String username);
	
	/**
	 * Get {@code passwordd} of the {@code User}
	 * @return
	 * 		{@code passwordd}
	 */
	public String getPassword();
	/**
	 * Set {@code passwordd} of the {@code User}
	 * @param passwordd
	 * 		Desired {@code passwordd} of the {@code User}
	 */
	public void setPassword(String passwordd);	
	
	/**
	 * Get {@code levell} of the {@code User}
	 * @return
	 * 		{@code levell}
	 */
	public int getLevel();
	/**
	 * Set {@code levell} of the {@code User}
	 * @param levell
	 * 		Desired {@code levell} of the User
	 */
	public void setLevel(int levell);
	
	/**
	 * Get {@code kills} of the {@code User}
	 * @return
	 * 		{@code kills}
	 */
	public int getKills();
	/**
	 * Set {@code kills} of the {@code User}
	 * @param kills
	 * 		Desired {@code kills} of the {@code User}
	 */
	public void setKills(int kills);
	
	/**
	 * Get {@code deaths} of the {@code User}
	 * @return
	 * 		{@code deaths}
	 */
	public int getDeaths();
	/**
	 * Set {@code deaths} of the {@code User}
	 * @param deaths
	 * 		Desired {@code deaths} of the {@code User}
	 */
	public void setDeaths(int deaths);
	
	/**
	 * Get {@code kdRatio} of the {@code User}
	 * @return
	 * 		{@code kdRatio}
	 */
	public double getKDRatio();
	
	/**
	 * Get {@code authLevel} of the {@code User}
	 * @return
	 * 		{@code authLevel}
	 */
	public int getAuthLevel();
	/**
	 * Set {@code authLevel} of the {@code User}
	 * @param authLevel
	 * 		Desired {@code authLevel} of the {@code User}
	 */
	public void setAuthLevel(int authLevel);
	
	/**
	 * Get {@code currency} of the {@code User}
	 * @return
	 * 		{@code currency}
	 */
	public double getCurrency();
	/**
	 * Set {@code currency} of the {@code User}
	 * @param currency
	 * 		Desired {@code currency} of the {@code User}
	 */
	public void setCurrency(double currency);
	
	/**
	 * Get {@code viewRadius} of the {@code User}
	 * @return
	 * 		{@code viewRadius}
	 */
	public double getViewRadius();
	/**
	 * Set {@code viewRadius} of the {@code User}
	 * @param viewRadius
	 * 		Desired {@code viewRadius} of the {@code User}
	 */
	public void setViewRadius(double viewRadius);
	
	/**
	 * Get {@code killRadius} of the {@code User}
	 * @return
	 * 		{@code killRadius}
	 */
	public double getKillRadius();
	/**
	 * Set {@code killRadius} of the {@code User}
	 * @param killRadius
	 * 		Desired {@code killRadius} of the {@code User}
	 */
	public void setKillRadius(double killRadius);
	
	/**
	 * Get {@code timeUpdated} of the {@code User}
	 * @return
	 * 		{@code timeUpdated}
	 */
	public long getTimeUpdated();
	/**
	 * Set {@code timeUpdated} of the {@code User}
	 * @param timeUpdated
	 * 		Desired {@code timeUpdated} of the {@code User}
	 */
	public void setTimeUpdated(long timeUpdated);
	
	/**
	 * Get {@code items} of the {@code User}
	 * @return
	 * 		{@code items}
	 */
	public Set<ItemInterface> getItems();
	/**
	 * Set {@code items} of the {@code User}
	 * @param items
	 * 		Desired {@code items} of the {@code User}
	 */
	public void setItems(Set<ItemInterface> items);
	
	/**
	 * Get {@code targets} of the {@code User}
	 * @return
	 * 		{@code targets}
	 */
	public List<UserInterface> getTargets();
	/**
	 * Set {@code targets} of the {@code User}
	 * @param targets
	 * 		Desired {@code targets} of the {@code User}
	 */ 
	public void setTargets(List<UserInterface> targets);
	
	/**
	 * Get {@code Location} of the {@code User}
	 * @return
	 * 		{@code Location}
	 */
	public Location getLocation();
	/**
	 * Set {@code Location} of the {@code User}
	 * @param location
	 * 		Desired {@code Location} of the {@code User}
	 */
	public void setLocation(Location location);
	
	/**
	 * Get {@code Token} of the {@code User}
	 * @return
	 * 		{@code Token}
	 */
	public Token getToken();
	/**
	 * Set {@code Token} of the {@code User}
	 * @param token
	 * 		Desired {@code Token} of the {@code User}
	 */
	public void setToken(Token token);
	
	/**
	 * Get {@code sessionTokens} of the {@code User}
	 * @return
	 * 		{@code sessionTokens}
	 */
	public Set<Token> getSession();
	/**
	 * Set {@code session} of the {@code User}
	 * @param session
	 * 		Desired {@code session} of the {@code User}
	 */
	public void setSession(Set<Token> session);
	
	/**
	 * Compares {@code id}, {@code password}, {@code authLevel}, and {@code username} of this and another {@code User} to check for equality
	 * @param user
	 * 		{@code User} to compare to
	 * @return
	 * 		{@code true} if equal, {@code false} otherwise
	 */
	public boolean equal(UserInterface user);
	
	/**
	 * Checks if the {@code User} has no {@code username} or {@code password}
	 * @return
	 * 		{@code true} if {@code User} is empty, {@code false} otherwise
	 */
	public boolean isEmpty();
	
	/**
	 * Updates the {@code username} and {@code password} to that of an inputed {@code User}
	 * @param user 
	 * 		{@code User} with {@code username} and {@code password} updating to
	 * @return
	 * 		{@code true} if {@code User} was updated, {@code false} otherwise
	 */
	public boolean updateInfo(UserInterface user);
	
	/**
	 * Converts {@code User} to String object
	 */
	@Override
	public String toString();
}
