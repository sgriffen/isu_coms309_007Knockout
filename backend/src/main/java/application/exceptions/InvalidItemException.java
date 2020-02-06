package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code Item} with that doesn't exist appeared when there shouldn't have
 * 
 * @author Sean Griffen
 */
public class InvalidItemException extends BackendException {
	
	public InvalidItemException() { super(); }
	public InvalidItemException(String message) { super(message); }
}
