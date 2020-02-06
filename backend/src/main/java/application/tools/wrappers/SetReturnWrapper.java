package application.tools.wrappers;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to return a {@code List<T>} from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		Object this is going to be used for
 */
public class SetReturnWrapper<T> extends ReturnWrapper {
	
	/**
	 * {@code List<T>} to return
	 */
	private Set<T> set;
	
	/**
	 * Default constructor
	 */
	public SetReturnWrapper() {
		
		super();
		set = new HashSet<>();
	}
	
	/**
	 * Get {@code set} of the {@code ListReturnWrapper}
	 * @return
	 * 		{@code set}
	 */
	public Set<T> getList() { return set; }
	/**
	 * Set {@code set} of the {@code ListReturnWrapper}
	 * @param set
	 * 		Desired {@code set} of the {@code ListReturnWrapper}
	 */
	public void setList(Set<T> set) { this.set = set; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code List<T>} to return
	 * @param set
	 * 		{@code List<T>} to return
	 * @param message
	 * 		{@code String} to return along with {@code set}
	 */
	public void excecute(Set<T> set, String message) {
		
		super.excecute(message);
		setList(set);
	}
}
