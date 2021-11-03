package model.players;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A Collection for storing GamePlayer objects.
 * @author Andrew Hocking
 */
public class PlayerCollection extends AbstractCollection<GamePlayer> {
	
	private GamePlayer[] data;
	private int size;
	
	public PlayerCollection() {
		data = new GamePlayer[2];
		size = 0;
	}
	
	/**
	 * Gets the specified GamePlayer in the collection, if it is there.
	 * @param player
	 * @return The player if it is present, or null otherwise.
	 */
	public GamePlayer get(GamePlayer player) {
		for(GamePlayer p : this) {
			if (p.equals(player)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets the GamePlayer with the specified name in the collection, if it is there.
	 * @param playerName
	 * @return The player if it is present, or null otherwise.
	 */
	public GamePlayer get(String playerName) {
		for(GamePlayer p : this) {
			if (p.getPlayerName().equals(playerName)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Gets the GamePlayer at the specified index.
	 * @param index
	 * @return The player at the index.
	 */
	public GamePlayer get(int index) {
		try {
			return data[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Sorts the elements by their natural ordering.
	 */
	public void sort() {
		Arrays.sort(data);
	}

	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Iterator<GamePlayer> iterator() {
		return new PlayerIterator(this);
	}

	@Override
	public boolean add(GamePlayer e) {
		try {
			if (data.length == size) {
				data = Arrays.copyOf(data, size * 2);
			}
			data[size] = e;
			size++;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * Finds the index of the specified GamePlayer in the collection.
	 * @param player
	 * @return The index of the player if it is present, or -1 otherwise.
	 */
	public int indexOf(GamePlayer player) {
		int index = Arrays.binarySearch(data, player);
		if (index < 0) {
			return -1;
		} else {
			return index;
		}
	}
	
	/**
	 * Removes the GamePlayer at the specified index.
	 * @param index
	 * @return True if the player as removed, false otherwise.
	 */
	public boolean remove(int index) {
		if (index >= 0 && index < size) {
			for (int i = index; i < size - 1; i++) {
				data[i] = data[i+1];
			}
			data[size - 1] = null;
			size--;
			return true;
		}
		return false;
	}
}