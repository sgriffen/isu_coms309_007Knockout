package application.tools.wrappers;

import application.tools.embeddables.Token;

/**
 * Used to return an {@code authLevel} of an {@code User} from queries
 * 
 * @author Sean Griffen
 */
public class LoginReturnWrapper extends ObjectReturnWrapper<Token> {
	
	/**
	 * {@code authLevel} of an {@code User}
	 */
	private Integer authLevel;
	
	/**
	 * Default constructor
	 */
	public LoginReturnWrapper() {
		
		super();
		setAuthLevel(new Integer(-1));
	}
	
	/**
	 * Get {@code toUpdate} of the {@code LocationUpdateWrapper}
	 * @return
	 * 		{@code toUpdate}
	 */	
	public Integer getAuthLevel() { return authLevel; }
	/**
	 * Set {@code toUpdate} of the {@code LocationUpdateWrapper}
	 * @param authLevel
	 * 		Desired {@code toUpdate} of the {@code LocationUpdateWrapper}
	 */
	public void setAuthLevel(Integer authLevel) { this.authLevel = authLevel; }
	
	/**
	 * See {@code ObjectReturnWrapper.excecute()}
	 * Sets the {@code authLevel} to return
	 * @param token
	 * 		{@code Token} to return
	 * @param authLevel
	 * 		{@code authLevel} to return
	 * @param message
	 * 		{@code message} to return
	 */
	public void excecute(Token token, Integer authLevel, String message) {
		
		super.excecute(token, message);
		setAuthLevel(authLevel);
	}
}
