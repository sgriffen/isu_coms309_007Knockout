package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code Session} with an incorrect format has appeared when there shouldn't have
 * 
 * @author Sean Griffen
 */
public class InvalidSessionException extends BackendException {
	
	public InvalidSessionException() { super(); }
	public InvalidSessionException(String message) { super(message); }
}
