package application.tools.wrappers;

import java.util.List;

import application.sessions.Session;
import application.tools.embeddables.Token;

/**
 * Used to start a {@code Session}
 * 
 * @author Sean Griffen
 */
public class SessionStartUpdateWrapper {
	
	/**
	 * {@code Session} to add to the {@code Session}
	 */
	private Token session;
	/**
	 * {@code Token} of the {@code Session} to add to
	 */
	private Token user;
	
	/**
	 * Get {@code user} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code user}
	 */
	public Token getUser() { return user; }
	/**
	 * Set {@code user} of the {@code SessionAddUserWrapper}
	 * @param user
	 * 		Desired {@code user} of the {@code SessionAddUserWrapper}
	 */
	public void setUser(Token user) { this.user = user; }
	
	/**
	 * Get {@code session} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code session}
	 */
	public Token getSession() { return session; }
	/**
	 * Set {@code session} of the {@code SessionAddUserWrapper}
	 * @param session
	 * 		Desired {@code session} of the {@code SessionAddUserWrapper}
	 */
	public void setSession(Token session) { this.session = session; }
}