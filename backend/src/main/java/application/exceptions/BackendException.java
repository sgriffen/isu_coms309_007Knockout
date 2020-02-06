package application.exceptions;

/**
 * An {@code Exception} unique to this project
 * 
 * @author Sean Griffen
 */
public class BackendException extends Exception{
	
	/**
	 * Default Constructor
	 */
	public BackendException() { super(); }
	
	/**
	 * Constructs a {@code BackendException} with a {@code message}
	 * @param message
	 * 		{@code String} to construct this {@code Exception} with
	 */
	public BackendException(String message) { super(message); }
}
