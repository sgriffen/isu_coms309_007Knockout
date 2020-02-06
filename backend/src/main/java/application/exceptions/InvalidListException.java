package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code List} was not in an expected format
 * 
 * @author Sean Griffen
 */
public class InvalidListException extends BackendException {
	
	public InvalidListException() { super(); }
	public InvalidListException(String message) { super(message); }
}
