package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.exception.EmptyDeckException;

import java.util.ArrayList;

public class LorenzoDeck {
    private final ArrayList<LorenzoCard> original;
    private Deck<LorenzoCard> inUse;

    public LorenzoDeck(ArrayList<LorenzoCard> cards){
        this.original = new ArrayList<>(cards);
        this.inUse = new Deck<>(cards);
        this.inUse.shuffle();
    }

    /**
     * Remove and return the card on top of the LorenzoDeck
     * @throws EmptyDeckException if deck is empty
     */
    public LorenzoCard drawCard(){
        return inUse.drawCard();
    }

    /**
     * Put back in the deck all the cards given to the constructor and shuffle them
     * @throws EmptyDeckException if deck is empty
     */
    public void backToOriginalAndShuffle(){
        ArrayList<LorenzoCard> c = new ArrayList<>(original);
        inUse = new Deck<>(c);
        inUse.shuffle();
    }
}
