package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LorenzoCard with effect of moving Lorenzo of 2 steps on the faithTrack.
 */
public class FaithLorenzoCard extends LorenzoCard {
    private static final long serialVersionUID = 1009L;
    private static final Logger logger = LogManager.getLogger(FaithLorenzoCard.class);

    public FaithLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add 2 faith points to Lorenzo.
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) throws EndAlreadyReachedException {
        try {
            game.getLorenzo().getFaithTrack().move(2, game);
        } catch (InvalidStepsException e) {
            logger.error("Even though the number of steps is constant to 2, this exception has been thrown: " + e);
        }
    }
}
