package application.exceptions;

/**
 * {@code Exception} that when thrown means that a {@code User} with a {@code authLevel} less than 2 has appeared when there shouldn't have
 * 
 * @author Sean Griffen
 */
public class InvalidAdministratorException extends BackendException {
	
	public InvalidAdministratorException() { super(); }
	public InvalidAdministratorException(String message) { super(message); }
}
