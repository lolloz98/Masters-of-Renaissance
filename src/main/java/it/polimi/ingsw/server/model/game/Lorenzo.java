package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.cards.lorenzo.LorenzoCard;
import it.polimi.ingsw.server.model.cards.lorenzo.ReshuffleLorenzoCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.FaithTrack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class Lorenzo implements Serializable {
    private static final Logger logger = LogManager.getLogger(Lorenzo.class);

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
     */
    public void performLorenzoAction(SinglePlayer singlePlayer) throws EndAlreadyReachedException, GameIsOverException, NotLorenzoTurnException {
        if(!singlePlayer.getTurn().isLorenzoPlaying()) throw new NotLorenzoTurnException();
        if(singlePlayer.isGameOver()) throw new GameIsOverException();

        LorenzoCard card = null;
        try {
            card = singlePlayer.getLorenzoDeck().drawCard();
        }catch (EmptyDeckException e){
            logger.error("Lorenzo's deck, because of its structure, can never be empty. The status of the game is corrupted: " + e);
        }
        if (card != null) card.applyEffect(singlePlayer);
    }
}
