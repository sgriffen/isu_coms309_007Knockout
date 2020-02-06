package application.tools.wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to return a {@code List<T>} from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		Object this is going to be used for
 */
public class ListReturnWrapper<T> extends ReturnWrapper {
	
	/**
	 * {@code List<T>} to return
	 */
	private List<T> list;
	
	/**
	 * Default constructor
	 */
	public ListReturnWrapper() {
		
		super();
		list = new ArrayList<>();
	}
	
	/**
	 * Get {@code list} of the {@code ListReturnWrapper}
	 * @return
	 * 		{@code list}
	 */
	public List<T> getList() { return list; }
	/**
	 * Set {@code list} of the {@code ListReturnWrapper}
	 * @param list
	 * 		Desired {@code list} of the {@code ListReturnWrapper}
	 */
	public void setList(List<T> list) { this.list = list; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code List<T>} to return
	 * @param list
	 * 		{@code List<T>} to return
	 * @param message
	 * 		{@code String} to return along with {@code list}
	 */
	public void excecute(List<T> list, String message) {
		
		super.excecute(message);
		setList(list);
	}
}
