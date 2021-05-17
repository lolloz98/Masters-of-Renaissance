package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.cards.Deck;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Handle the deck of LorenzoCard.
 */
public class LorenzoDeck implements Serializable {
    private static final long serialVersionUID = 1011L;
    private static final Logger logger = LogManager.getLogger(LorenzoDeck.class);

    private final ArrayList<LorenzoCard> original;
    private Deck<LorenzoCard> inUse;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LorenzoDeck){
            LorenzoDeck t = (LorenzoDeck) obj;
            return original.equals(t.original) && inUse.equals(t.inUse);
        }
        return false;
    }

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
    public void backToOriginalAndShuffle() {
        inUse = new Deck<>(original);
        try {
            inUse.shuffle();
        } catch (EmptyDeckException e) {
            logger.error("Deck just refilled: it cannot be empty");
        }
    }
}
