package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.FaithTrack;

import java.io.Serializable;

public class Lorenzo implements Serializable {
    private static final long serialVersionUID = 1017L;

    private final FaithTrack faithTrack;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Lorenzo){
            Lorenzo t = (Lorenzo) obj;
            return faithTrack.equals(t.faithTrack);
        }
        return false;
    }

    public Lorenzo() {
        this.faithTrack = new FaithTrack();
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Draws a card from the LorenzoDeck in SinglePlayer and activates its effect
     *
     * @param singlePlayer current game
     * @throws EmptyDeckException if deck is empty
     */
    public void performLorenzoAction(SinglePlayer singlePlayer) throws EmptyDeckException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException, InvalidStepsException, EndAlreadyReachedException {
        singlePlayer.getLorenzoDeck().drawCard().applyEffect(singlePlayer);
    }
}
