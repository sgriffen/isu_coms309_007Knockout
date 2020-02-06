package application.tools.wrappers;

import application.tools.embeddables.Token;

/**
 * Used to update the statistics ({@code level}, {@code kills}, and {@code deaths}) of a {@code User}
 * 
 * @author Sean Griffen
 */
public class StatisticUpdateWrapper {
	
	/**
	 * {@code Token} of the {@code User} to update
	 */
	private Token toUpdate;
	
	/**
	 * {@code level} of the {@code User} to update to
	 */
	private Integer level;
	/**
	 * {@code kills} of the {@code User} to update to
	 */
	private Integer kills;
	/**
	 * {@code deaths} of the {@code User} to update to
	 */
	private Integer deaths;
	
	/**
	 * Get {@code toUpdate} of the {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code toUpdate}
	 */
	public Token getToUpdate() { return toUpdate; }
	/**
	 * Set {@code toUpdate} of the {@code StatisticUpdateWrapper}
	 * @param userToUpdate
	 * 		Desired {@code toUpdate} of the {@code StatisticUpdateWrapper}
	 */
	public void setToUpdate(Token userToUpdate) { this.toUpdate = userToUpdate; }
	
	/**
	 * Get {@code level} of the {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code level}
	 */
	public Integer getLevel() { return level; }
	/**
	 * Set {@code level} of the {@code StatisticUpdateWrapper}
	 * @param level
	 * 		Desired {@code level} of the {@code StatisticUpdateWrapper}
	 */
	public void setLevel(int level) { this.level = new Integer(level); }
	
	/**
	 * Get {@code kills} of the {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code kills}
	 */
	public Integer getKills() { return kills; }
	/**
	 * Set {@code kills} of the {@code StatisticUpdateWrapper}
	 * @param kills
	 * 		Desired {@code kills} of the {@code StatisticUpdateWrapper}
	 */
	public void setKills(int kills) { this.kills = new Integer(kills); }
	
	/**
	 * Get {@code deaths} of the {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code deaths}
	 */
	public Integer getDeaths() { return deaths; }
	/**
	 * Set {@code deaths} of the {@code StatisticUpdateWrapper}
	 * @param deaths
	 * 		Desired {@code deaths} of the {@code StatisticUpdateWrapper}
	 */
	public void setDeaths(int deaths) { this.deaths = new Integer(deaths); }
}
