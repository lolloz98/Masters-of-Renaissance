package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.EmptyDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Lorenzo Carpaneto
 * Deck of cards.
 */
public class Deck<T extends Card> {
    protected final ArrayList<T> cards = new ArrayList<>();

    public Deck(ArrayList<T> cards){
        this.cards.addAll(cards);
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Shuffle the cards contained in the deck
     * @throws EmptyDeckException
     */
    public void shuffle() {
        if(isEmpty()) throw new EmptyDeckException();
        Collections.shuffle(cards);
    }

    /**
     * Remove and return the card on top of the deck
     * @throws EmptyDeckException
     */
    public T drawCard(){
        if (isEmpty()) throw new EmptyDeckException();
        return cards.remove(0);
    }

    /**
     * Return the card on top of the deck WITHOUT removing it
     * @throws EmptyDeckException
     */
    public T topCard(){
        if (isEmpty()) throw new EmptyDeckException();
        return cards.get(0);
    }

    /**
     * Return and remove from the to of the deck the number of specified cards
     * @throws EmptyDeckException
     */
    public ArrayList<T> distributeCards(int number){
        ArrayList<T> cToDis = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            cToDis.add(drawCard());
        }
        return cToDis;
    }
}
