package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code User} with an invalid format has appeared when there shouldn't have
 * 
 * @author Sean Griffen
 */
public class InvalidUserException extends BackendException {
	
	public InvalidUserException() { super(); }
	public InvalidUserException(String message) { super(message); }
}
