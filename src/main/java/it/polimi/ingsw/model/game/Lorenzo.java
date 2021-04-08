package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.EmptyDeckException;
import it.polimi.ingsw.model.player.FaithTrack;

public class Lorenzo {
    private final FaithTrack faithTrack;

    public Lorenzo() {
        this.faithTrack = new FaithTrack();
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Draws a card from the LorenzoDeck in SinglePlayer and activates its effect
     *
     * @throws EmptyDeckException if deck is empty
     */
    public void performLorenzoAction(SinglePlayer singlePlayer){
        singlePlayer.getLorenzoDeck().drawCard().applyEffect(singlePlayer);
    }
}
