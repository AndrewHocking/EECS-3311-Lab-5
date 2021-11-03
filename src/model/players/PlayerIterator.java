package model.players;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator class for the PlayerCollection class.
 * @author Andrew Hocking
 */
public class PlayerIterator implements Iterator<GamePlayer> {
	
	private PlayerCollection playerCollection;
	private int nextIndex = 0;
	private int prevIndex = -1;
	
    public PlayerIterator(PlayerCollection playerCollection) {
    	this.playerCollection = playerCollection;
    }

    @Override
    public boolean hasNext() {
        return nextIndex < playerCollection.size();
    }

    @Override
    public GamePlayer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (nextIndex >= playerCollection.size()) {
            throw new ConcurrentModificationException();
        }
        
        prevIndex = nextIndex;
        nextIndex++;
        return playerCollection.get(prevIndex);
    }

    @Override
    public void remove() {
        if (prevIndex < 0) {
            throw new IllegalStateException();
        }

        try {
        	playerCollection.remove(prevIndex);
            nextIndex = prevIndex;
            prevIndex = -1;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }
}
