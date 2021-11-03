package model.players;

/**
 * A data class for the GamePlayer class.
 * @author Andrew Hocking
 */
public class PlayerStatistics {
	
	private int statistics = 0;

	public Integer getStatistics() {
		return statistics;
	}

	public void setStatistics(Integer newStatistics) {
		statistics = newStatistics.intValue();
	}
	
	public String toString() {
		return Integer.toString(statistics);
	}

}
