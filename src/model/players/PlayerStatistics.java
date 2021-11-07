package model.players;

/**
 * A data class for the GamePlayer class.
 * @author Andrew Hocking
 */
public class PlayerStatistics {
	
	private int statistics = 0;

	/**
	 * @return The player's statistics.
	 */
	public Integer getStatistics() {
		return statistics;
	}

	/**
	 * @param newStatistics The new value to set the player's statistics to.
	 */
	public void setStatistics(Integer newStatistics) {
		statistics = newStatistics.intValue();
	}
	
	@Override
	public String toString() {
		return Integer.toString(statistics);
	}

}
