package application.tools.wrappers;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to return 2 different types of {@code Set<>}s from queries
 * 
 * @author Sean Griffen
 * 
 * @param <T>
 * 		Object 1 this is going to be used for
 * @param <V>
 * 		Object 2 this is going to be used for
 */
public class MultiSetReturnWrapper<T, V> extends ReturnWrapper {
	
	/**
	 * {@code Set<T>} to return
	 */
	private Set<T> set1;
	
	/**
	 * {@code Set<V>} to return
	 */
	private Set<V> set2;
	
	/**
	 * Default constructor
	 */
	public MultiSetReturnWrapper() {
		
		super();
		set1 = new HashSet<>();
		set2 = new HashSet<>();
	}
	
	/**
	 * Constructs a {@code MultiSetReturnWrapper<T,V>} with {@code <T>} set1 and {@code <V>} set2
	 * @param set1
	 * 		{@code Set<T>} to set
	 * @param set2
	 * 		{@code Set<V>} to set
	 */
	public MultiSetReturnWrapper(Set<T> set1, Set<V> set2) {
		
		super();
		
		setSet1(set1);
		setSet2(set2);
	}
	
	/**
	 * Get {@code set1} of the {@code MultiSetReturnWrapper}
	 * @return
	 * 		{@code set1}
	 */
	public Set<T> getSet1() { return set1; }
	/**
	 * Set {@code set1} of the {@code MultiSetReturnWrapper}
	 * @param set1
	 * 		Desired {@code set1} of the {@code MultiSetReturnWrapper}
	 */
	public void setSet1(Set<T> set1) { this.set1 = set1; }
	
	/**
	 * Get {@code set2} of the {@code MultiSetReturnWrapper}
	 * @return
	 * 		{@code set2}
	 */
	public Set<V> getSet2() { return set2; }
	/**
	 * Set {@code set2} of the {@code MultiSetReturnWrapper}
	 * @param set2
	 * 		Desired {@code set2} of the {@code MultiSetReturnWrapper}
	 */
	public void setSet2(Set<V> set2) { this.set2 = set2; }
	
	/**
	 * See {@code ReturnWrapper.excecute}.
	 * Adds a {@code Set<T>} to return
	 * @param set1
	 * 		{@code Set<T>} to return
	 * @param set2
	 * 		{@code Set<V>} to return
	 * @param message
	 * 		{@code String} to return along with {@code set}
	 */
	public void excecute(Set<T> set1, Set<V> set2, String message) {
		
		super.excecute(message);
		setSet1(set1);
		setSet2(set2);
	}
}
