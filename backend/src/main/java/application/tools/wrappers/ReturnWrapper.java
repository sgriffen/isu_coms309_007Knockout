package application.tools.wrappers;

import application.exceptions.BackendException;

/**
 * Used to return an {@code Integer} and a {@code String} from queries
 * 
 * @author Sean Griffen
 */
public abstract class ReturnWrapper {
	
	/**
	 * {@code Integer} that denotes if an exception was thrown by the returning method
	 * {@code 0} means no exception
	 * {@code > 0} means an exception was thrown
	 */
	private Integer exception;
	/**
	 * {@code String} that the returning method is trying to return
	 */
	private String message;
	
	/**
	 * Default constructor
	 */
	public ReturnWrapper() {
		
		setMessage(null);
		setException(null);
	}
	
	/**
	 * Constructs a {@code ReturnWrapper} with {@code exception i}, and {@code message s}
	 * @param i
	 * 		{@code exception} to return
	 * @param s
	 * 		{@code message} to return
	 */
	public ReturnWrapper(Integer i, String s) {
		
		this();
		setMessage(s);
		setException(i);
	}
	
	/**
	 * Get {@code exception} of the {@code ReturnWrapper}
	 * @return
	 * 		{@code exception}
	 */
	public Integer getException() { return exception; }
	/**
	 * Set {@code exception} of the {@code ReturnWrapper}
	 * @param exception
	 * 		Desired {@code exception} of the {@code ReturnWrapper}
	 */
	public void setException(Integer exception) { this.exception = exception; }
	
	/**
	 * Get {@code message} of the {@code ReturnWrapper}
	 * @return
	 * 		{@code message}
	 */
	public String getMessage() { return message; }
	/**
	 * Set {@code message} of the {@code ReturnWrapper}
	 * @param message
	 * 		Desired {@code message} of the {@code ReturnWrapper}
	 */
	public void setMessage(String message) { this.message = message; }
	
	/**
	 * Sets {@code exception} to greater than 0 to denote that an {@code Exception} was thrown.
	 * Sets {@code message} to the thrown {@code Exception}'s message
	 * @param e
	 * 		{code Exception} that was thrown
	 */
	public void error(BackendException e) {
		
		setException(new Integer(1));
		setMessage(e.getMessage());
	}
	
	/**
	 * Sets {@code exception} to 0 to denote that no {@code Exception} was thrown.
	 * Sets {@code message} to what the returning method wants
	 * @param message
	 * 		{@code String} to return
	 */
	public void excecute(String message) {
		
		setException(new Integer(0));
		setMessage(message);
	}
}
