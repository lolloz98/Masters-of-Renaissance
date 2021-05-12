package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.cards.Deck;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;

import java.util.ArrayList;

/**
 * Handle the deck of LorenzoCard.
 */
public class LorenzoDeck {
    private final ArrayList<LorenzoCard> original;
    private Deck<LorenzoCard> inUse;

    public LorenzoDeck(ArrayList<LorenzoCard> cards) throws EmptyDeckException {
        this.original = new ArrayList<>(cards);
        this.inUse = new Deck<>(cards);
        this.inUse.shuffle();
    }

    public LorenzoCard getTopCard() throws EmptyDeckException {
        return inUse.topCard();
    }

    /**
     * Remove and return the card on top of the LorenzoDeck
     *
     * @throws EmptyDeckException if deck is empty
     */
    public LorenzoCard drawCard() throws EmptyDeckException {
        return inUse.drawCard();
    }

    /**
     * Put back in the deck all the cards given to the constructor and shuffle them
     */
    public void backToOriginalAndShuffle() throws EmptyDeckException {
        inUse = new Deck<>(original);
        inUse.shuffle();
    }
}
