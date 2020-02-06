package application.tools.wrappers;

import application.tools.embeddables.Token;
import application.users.User;

/**
 * Used to update a {@code User} to another {@code User}
 * 
 * @author Sean Griffen
 */
public class UserUpdateWrapper {
	
	/**
	 * {@code Token} of the {@code User} to update
	 */
	private Token toUpdate;
	
	/**
	 * {@code User} to update to
	 */
	private User updateTo;
	
	/**
	 * {@code Token} of an {@code User} with {@code authLevel} equal to 2 to authorize updating the {@code authLevel} or {@code toUpdate} to greater than 0
	 */
	private Token admin;
	
	/**
	 * Get {@code toUpdate} of the {@code UserUpdateWrapper}
	 * @return
	 * 		{@code toUpdate}
	 */
	public Token getToUpdate() { return toUpdate; }
	/**
	 * Set {@code toUpdate} of the {@code UserUpdateWrapper}
	 * @param toUpdate
	 * 		Desired {@code toUpdate} of the {@code UserUpdateWrapper}
	 */
	public void setToUpdate(Token toUpdate) { this.toUpdate = toUpdate; }

	/**
	 * Get {@code updateTo} of the {@code UserUpdateWrapper}
	 * @return
	 * 		{@code updateTo}
	 */
	public User getUpdateTo() { return updateTo; }
	/**
	 * Set {@code updateTo} of the {@code UserUpdateWrapper}
	 * @param updateTo
	 * 		Desired {@code updateTo} of the {@code UserUpdateWrapper}
	 */
	public void setUpdateTo(User updateTo) { this.updateTo = (User) updateTo; }

	/**
	 * Get {@code admin} of the {@code UserUpdateWrapper}
	 * @return
	 * 		{@code admin}
	 */
	public Token getAdmin() { return admin; }
	/**
	 * Set {@code admin} of the {@code UserUpdateWrapper}
	 * @param admin
	 * 		Desired {@code admin} of the {@code UserUpdateWrapper}
	 */
	public void setAdmin(Token admin) { this.admin = admin; }
}
