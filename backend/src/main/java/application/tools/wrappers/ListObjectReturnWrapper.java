package application.tools.wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to return 2 different types of {@code List<>}s from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		{@code List<T>} this is going to be used for
 * @param <V>
 * 		Object this is going to be used for
 */
public class ListObjectReturnWrapper<T, V> extends ReturnWrapper {
	
	/**
	 * {@code List<T>} to return
	 */
	private List<T> list;
	
	/**
	 * {@code List<V>} to return
	 */
	private V object;
	
	/**
	 * Default constructor
	 */
	public ListObjectReturnWrapper() {
		
		super();
		list = new ArrayList<>();
		object = null;
	}
	
	/**
	 * Get {@code list} of the {@code ListObjectReturnWrapper}
	 * @return
	 * 		{@code list}
	 */
	public List<T> getList() { return list; }
	/**
	 * Set {@code list} of the {@code ListObjectReturnWrapper}
	 * @param list
	 * 		Desired {@code list} of the {@code ListObjectReturnWrapper}
	 */
	public void setList(List<T> list) { this.list = list; }
	
	/**
	 * Get {@code object} of the {@code ListObjectReturnWrapper}
	 * @return
	 * 		{@code object}
	 */
	public V getObject() { return object; }
	/**
	 * Set {@code object} of the {@code ListObjectReturnWrapper}
	 * @param object
	 * 		Desired {@code object} of the {@code ListObjectReturnWrapper}
	 */
	public void setObject(V object) { this.object = object; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code List<T>} and a {@code Object<V>} to return
	 * @param list
	 * 		{@code List<T>} to return
	 * @param object
	 * 		{@code Object<V>} to return
	 * @param message
	 * 		{@code String} to return along with {@code list}
	 */
	public void excecute(List<T> list, V object, String message) {
		
		super.excecute(message);
		setList(list);
		setObject(object);
	}
}
