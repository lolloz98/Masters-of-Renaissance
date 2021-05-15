package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.cards.Card;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.SinglePlayer;

public abstract class LorenzoCard implements Card {
    private static final long serialVersionUID = 1010L;

    private final int id;

    public LorenzoCard(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Card){
            Card t = (Card) obj;
            return id == t.getId();
        }
        return false;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * apply the effect of LorenzoCard
     *
     * @param game current single game player
     */
    public abstract void applyEffect(SinglePlayer game) throws EmptyDeckException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException, InvalidStepsException, EndAlreadyReachedException;
}
