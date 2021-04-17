package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Deck<T extends Card> {
    private static final Logger logger = LogManager.getLogger(Deck.class);

    protected final ArrayList<T> cards = new ArrayList<>();

    public Deck(ArrayList<T> cards) {
        this.cards.addAll(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * @return number of cards still in this deck
     */
    public int howManyCards() {
        return cards.size();
    }

    /**
     * Shuffle the cards contained in the deck
     *
     * @throws EmptyDeckException if deck is empty
     */
    public void shuffle() throws EmptyDeckException {
        if (isEmpty()) throw new EmptyDeckException();
        // logger.debug("Shuffle in deck, isTest: " + CollectionsHelper.isTest());
        CollectionsHelper.shuffle(cards);
    }

    /**
     * Remove and return the card on top of the deck
     *
     * @throws EmptyDeckException if deck is empty
     */
    public T drawCard() throws EmptyDeckException {
        if (isEmpty()) throw new EmptyDeckException();
        return cards.remove(0);
    }

    /**
     * Return the card on top of the deck WITHOUT removing it
     *
     * @throws EmptyDeckException if deck is empty
     */
    public T topCard() throws EmptyDeckException {
        if (isEmpty()) throw new EmptyDeckException();
        return cards.get(0);
    }

    /**
     * Return and remove from the to of the deck the number of specified cards.
     *
     * @param number cards to be retrieved and removed from the deck
     * @return ArrayList of cards from the deck
     * @throws EmptyDeckException if deck is empty
     */
    public ArrayList<T> distributeCards(int number) throws EmptyDeckException {
        ArrayList<T> cToDis = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            cToDis.add(drawCard());
        }
        return cToDis;
    }
}
