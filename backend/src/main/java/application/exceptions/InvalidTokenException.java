package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code Token} with either an invalid {@code authenticator} or with an {@code expiration} greater than {@code system.currentTime} was used
 * 
 * @author Sean Griffen
 */
public class InvalidTokenException extends BackendException {
	
	public InvalidTokenException() { super(); }
	public InvalidTokenException(String message) { super(message); }
}
