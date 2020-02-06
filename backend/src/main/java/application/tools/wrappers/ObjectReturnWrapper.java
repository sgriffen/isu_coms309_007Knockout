package application.tools.wrappers;

/**
 * Used to return an Object from queries
 * 
 * @author Sean Griffen
 *
 * @param <T>
 * 		Object this is going to be used for
 */
public class ObjectReturnWrapper<T> extends ReturnWrapper {
	
	/**
	 * Object to return
	 */
	private T object;
	
	/**
	 * Default Constructor
	 */
	public ObjectReturnWrapper() {
		
		super();
		object = null;
	}
	
	/**
	 * Get {@code object} of the {@code ObjectReturnWrapper}
	 * @return
	 * 		{@code object}
	 */
	public T getObject() { return object; }
	/**
	 * Set {@code object} of the {@code ObjectReturnWrapper}
	 * @param object
	 * 		Desired {@code object} of the {@code ObjectReturnWrapper}
	 */
	public void setObject(T object) { this.object = object; }
	
	/**
	 * See {@code ReturnWrapper.excecute()}
	 * @param object
	 * 		Object to return
	 * @param message
	 * 		{@code String} to return along with {@code Object}
	 */
	public void excecute(T object, String message) {
		
		super.excecute(message);
		setObject(object);
	}
}
