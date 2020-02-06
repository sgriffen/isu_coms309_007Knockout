package application.tools.wrappers;

import java.util.ArrayList;
import java.util.List;

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
public class MultiListReturnWrapper<T, V> extends ReturnWrapper {
	
	/**
	 * {@code List<T>} to return
	 */
	private List<T> list1;
	
	/**
	 * {@code List<V>} to return
	 */
	private List<V> list2;
	
	/**
	 * Default constructor
	 */
	public MultiListReturnWrapper() {
		
		super();
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
	}
	
	/**
	 * Constructs a {@code MultiListReturnWrapper<T,V>} with {@code <T>} list1 and {@code <V>} list2
	 * @param list1
	 * 		{@code List<T>} to set
	 * @param list2
	 * 		{@code List<V>} to set
	 */
	public MultiListReturnWrapper(List<T> list1, List<V> list2) {
		
		super();
		
		setList1(list1);
		setList2(list2);
	}
	
	/**
	 * Get {@code list1} of the {@code MultiListReturnWrapper}
	 * @return
	 * 		{@code list1}
	 */
	public List<T> getList1() { return list1; }
	/**
	 * Set {@code list1} of the {@code MultiListReturnWrapper}
	 * @param list1
	 * 		Desired {@code list1} of the {@code MultiListReturnWrapper}
	 */
	public void setList1(List<T> list1) { this.list1 = list1; }
	
	/**
	 * Get {@code list2} of the {@code MultiListReturnWrapper}
	 * @return
	 * 		{@code list2}
	 */
	public List<V> getList2() { return list2; }
	/**
	 * Set {@code list2} of the {@code MultiListReturnWrapper}
	 * @param list2
	 * 		Desired {@code list2} of the {@code MultiListReturnWrapper}
	 */
	public void setList2(List<V> list2) { this.list2 = list2; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code List<T>} to return
	 * @param list1
	 * 		{@code List<T>} to return
	 * @param list2
	 * 		{@code List<V>} to return
	 * @param message
	 * 		{@code String} to return along with {@code list}
	 */
	public void excecute(List<T> list1, List<V> list2, String message) {
		
		super.excecute(message);
		setList1(list1);
		setList2(list2);
	}
}
