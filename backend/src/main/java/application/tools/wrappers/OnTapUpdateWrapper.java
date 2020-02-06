package application.tools.wrappers;

import application.tools.embeddables.Location;
import application.tools.embeddables.Token;

/**
 * Used to update an {@code User} based off its {@code Location}
 * 
 * @author Sean Griffen
 */
public class OnTapUpdateWrapper {
	
	/**
	 * {@code Token} of the {@code Session} to pull data from
	 */
	private Token session;
	/**
	 * {@code Token} of the {@code User} that tapped
	 */
	private Token tapper;
	/**
	 * {@code Location} of the tap
	 */
	private Location tapped;

	/**
	 * Get {@code session} of the {@code OnTapUpdateWrapper}
	 * @return
	 * 		{@code session}
	 */
	public Token getSession() { return session; }
	/**
	 * Set {@code session} of the {@code OnTapUpdateWrapper}
	 * @param session
	 * 		Desired {@code session} of the {@code OnTapUpdateWrapper}
	 */
	public void setToUpdate(Token session) { this.session = session; }
	
	/**
	 * Get {@code tapper} of the {@code OnTapUpdateWrapper}
	 * @return
	 * 		{@code tapper}
	 */
	public Token getTapper() { return tapper; }
	/**
	 * Set {@code tapper} of the {@code OnTapUpdateWrapper}
	 * @param tapper
	 * 		Desired {@code tapper} of the {@code OnTapUpdateWrapper}
	 */
	public void setTapper(Token tapper) { this.tapper = tapper; }
	
	/**
	 * Get {@code tapped} of the {@code OnTapUpdateWrapper}
	 * @return
	 * 		{@code tapped}
	 */
	public Location getTapped() { return tapped; }
	/**
	 * Set {@code tapped} of the {@code OnTapUpdateWrapper}
	 * @param tapped
	 * 		Desired {@code tapped} of the {@code OnTapUpdateWrapper}
	 */
	public void setTapped(Location tapped) { this.tapped = tapped; }
}
