package application.tools.wrappers;

import java.util.List;

import application.tools.embeddables.Token;

/**
 * Used to add an {@code User} to a {@code Session}
 * 
 * @author Sean Griffen
 */
public class SessionAddUserWrapper {
	
	/**
	 * {@code Token} to add to the {@code Session}
	 */
	private Token toAdd;
	/**
	 * {@code passcode} of the {@code Session} to add to
	 */
	private int passcode;
	
	/**
	 * Get {@code toAdd} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code toAdd}
	 */
	public Token getToAdd() { return toAdd; }
	/**
	 * Set {@code toAdd} of the {@code SessionAddUserWrapper}
	 * @param toAdd
	 * 		Desired {@code toAdd} of the {@code SessionAddUserWrapper}
	 */
	public void setToAdd(Token toAdd) { this.toAdd = toAdd; }
	
	/**
	 * Get {@code passcode} of the {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code passcode}
	 */
	public int getPasscode() { return passcode; }
	/**
	 * Set {@code passcode} of the {@code SessionAddUserWrapper}
	 * @param passcode
	 * 		Desired {@code passcode} of the {@code SessionAddUserWrapper}
	 */
	public void setPasscode(int passcode) { this.passcode = passcode; }
}