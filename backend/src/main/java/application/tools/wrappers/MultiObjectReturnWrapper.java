package application.tools.wrappers;

/**
 * Used to return a two different types of objects from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		Object 1 to return
 * @param <V>
 * 		Object 2 to return
 */
public class MultiObjectReturnWrapper<T, V> extends ReturnWrapper {
	
	/**
	 * {@code Token} of a {@code Session} to return
	 */
	private T object1;
	
	/**
	 * If a {@code Session} is started or not
	 */
	private V object2;
	
	/**
	 * Default constructor
	 */
	public MultiObjectReturnWrapper() {
		
		super();
		setObject1(null);
		setObject2(null);
	}
	
	/**
	 * Get {@code object1} of the {@code MultiObjectReturnWrapper}
	 * @return
	 * 		{@code object1}
	 */
	public T getObject1() { return object1; }
	/**
	 * Set {@code object1} of the {@code MultiObjectReturnWrapper}
	 * @param object1
	 * 		Desired {@code object1} of the {@code MultiObjectReturnWrapper}
	 */
	public void setObject1(T object1) { this.object1 = object1; }
	
	/**
	 * Get {@code object2} of the {@code MultiObjectReturnWrapper}
	 * @return
	 * 		{@code object2}
	 */
	public V getObject2() { return object2; }
	/**
	 * Set {@code object2} of the {@code MultiObjectReturnWrapper}
	 * @param object2
	 * 		Desired {@code object2} of the {@code MultiObjectReturnWrapper}
	 */
	public void setObject2(V object2) { this.object2 = object2; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code Object<T>}, and a {@code Object<V>} to return
	 * @param object1
	 * 		{@code List<T>} to return
	 * @param object2
	 * 		{@code Object<V>} to return
	 * @param message
	 * 		{@code String} to return along with {@code list}
	 */
	public void excecute(T object1, V object2, String message) {
		
		super.excecute(message);
		setObject1(object1);
		setObject2(object2);
	}
}
