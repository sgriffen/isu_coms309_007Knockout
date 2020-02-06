package application.tools.wrappers;

import java.util.List;

import application.sessions.Session;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;

/**
 * Used to add an {@code Session} to the database
 * 
 * @author Sean Griffen
 */
public class SessionAddUpdateWrapper {
	
	/**
	 * {@code name} to give to the {@code Session}
	 */
	private String name;
	/**
	 * {@code Location} to give to the {@code Session}
	 */
	private Location center;
	/**
	 * {@code radius} to give to the {@code Session}
	 */
	private double radius;
	
	/**
	 * {@code Token} of the {@code Session} to add to
	 */
	private Token token;
	
	/**
	 * Get {@code token} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code token}
	 */
	public Token getToken() { return token; }
	/**
	 * Set {@code token} of the {@code SessionAddUserWrapper}
	 * @param token
	 * 		Desired {@code token} of the {@code SessionAddUserWrapper}
	 */
	public void setToken(Token token) { this.token = token; }
	
	/**
	 * Get {@code name} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code name}
	 */
	public String getName() { return name; }
	/**
	 * Set {@code name} of the {@code SessionAddUserWrapper}
	 * @param name
	 * 		Desired {@code name} of the {@code SessionAddUserWrapper}
	 */
	public void setName(String name) { this.name = name; }
	
	/**
	 * Get {@code center} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code center}
	 */
	public Location getCenter() { return center; }
	/**
	 * Set {@code center} of the {@code SessionAddUserWrapper}
	 * @param center
	 * 		Desired {@code center} of the {@code SessionAddUserWrapper}
	 */
	public void setCenter(Location center) { this.center = center; }
	
	/**
	 * Get {@code radius} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code radius}
	 */
	public double getRadius() { return radius; }
	/**
	 * Set {@code radius} of the {@code SessionAddUserWrapper}
	 * @param radius
	 * 		Desired {@code radius} of the {@code SessionAddUserWrapper}
	 */
	public void setRadius(double radius) { this.radius = radius; }
}