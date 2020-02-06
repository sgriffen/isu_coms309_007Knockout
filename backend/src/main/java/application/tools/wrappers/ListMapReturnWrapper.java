package application.tools.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to return 2 different types of {@code List<>}s from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		Object 1 this is going to be used for
 * @param <V>
 * 		Object 2 this is going to be used for
 */
public class ListMapReturnWrapper<T, K, V> extends ReturnWrapper {
	
	/**
	 * {@code List<T>} to return
	 */
	private List<T> list;
	
	/**
	 * {@code List<V>} to return
	 */
	private Map<K, V> map;
	
	/**
	 * Default constructor
	 */
	public ListMapReturnWrapper() {
		
		super();
		list = new ArrayList<>();
		map = new HashMap<>();
	}
	
	/**
	 * Constructs a {@code MultiListReturnWrapper<T,V>} with {@code <T>} list and {@code <V>} map
	 * @param list
	 * 		{@code List<T>} to set
	 * @param map
	 * 		{@code List<V>} to set
	 */
	public ListMapReturnWrapper(List<T> list, Map<K, V> map) {
		
		super();
		
		setList(list);
		setMap(map);
	}
	
	/**
	 * Get {@code list} of the {@code MultiListReturnWrapper}
	 * @return
	 * 		{@code list}
	 */
	public List<T> getList() { return list; }
	/**
	 * Set {@code list} of the {@code MultiListReturnWrapper}
	 * @param list
	 * 		Desired {@code list} of the {@code MultiListReturnWrapper}
	 */
	public void setList(List<T> list) { this.list = list; }
	
	/**
	 * Get {@code map} of the {@code MultiListReturnWrapper}
	 * @return
	 * 		{@code map}
	 */
	public Map<K, V> getMap() { return map; }
	/**
	 * Set {@code map} of the {@code MultiListReturnWrapper}
	 * @param map
	 * 		Desired {@code map} of the {@code MultiListReturnWrapper}
	 */
	public void setMap(Map<K, V> map) { this.map = map; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code List<T>} to return
	 * @param list
	 * 		{@code List<T>} to return
	 * @param map
	 * 		{@code List<V>} to return
	 * @param message
	 * 		{@code String} to return along with {@code list}
	 */
	public void excecute(List<T> list, Map<K, V> map, String message) {
		
		super.excecute(message);
		setList(list);
		setMap(map);
	}
}
