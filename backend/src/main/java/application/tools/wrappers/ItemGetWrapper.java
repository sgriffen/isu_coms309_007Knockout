package application.tools.wrappers;

import application.tools.embeddables.Token;

/**
 * Used to get an {@code Item} from the database
 * 
 * @author Sean Griffen
 */
public class ItemGetWrapper {
	
	/**
	 * {@code Token} of an {@code User} who's {@code authLevel} is greater than 2
	 */
	private Token admin;
	/**
	 * {@code id} of the {@code Item} to get
	 */
	private int toGet;
	
	/**
	 * Get {@code admin} of the {@code ItemGetWrapper}
	 * @return
	 * 		{@code admin}
	 */
	public Token getAdmin() { return admin; }
	/**
	 * Set {@code admin} of the {@code ItemGetWrapper}
	 * @param admin
	 * 		Desired {@code admin} of the {@code ItemGetWrapper}
	 */
	public void setAdmin(Token admin) { this.admin = admin; }
	
	/**
	 * Get {@code toGet} of the {@code ItemGetWrapper}
	 * @return
	 * 		{@code toGet}
	 */
	public int getToGet() { return toGet; }
	/**
	 * Set {@code toGet} of the {@code ItemGetWrapper}
	 * @param toGet
	 * 		Desired {@code toGet} of the {@code ItemGetWrapper}
	 */
	public void setToGet(int toGet) { this.toGet = toGet; }
}