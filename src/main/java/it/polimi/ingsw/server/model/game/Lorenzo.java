package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.FaithTrack;

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
     * @param singlePlayer current game
     * @throws EmptyDeckException if deck is empty
     */
    public void performLorenzoAction(SinglePlayer singlePlayer) throws EmptyDeckException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException, InvalidStepsException, EndAlreadyReachedException {
        singlePlayer.getLorenzoDeck().drawCard().applyEffect(singlePlayer);
    }
}
