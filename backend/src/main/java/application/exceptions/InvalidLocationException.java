package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code Locations} was used inappropriately
 * 
 * @author Sean Griffen
 */
public class InvalidLocationException extends BackendException {
	
	public InvalidLocationException() { super(); }
	public InvalidLocationException(String message) { super(message); }
}
