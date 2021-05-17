package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.controller.messagesctr.playing.FinishTurnMessageController;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LorenzoCard with effect of moving Lorenzo of 1 step on the faithTrack and restoring LorenzoDeck.
 */
public class ReshuffleLorenzoCard extends LorenzoCard {
    private static final long serialVersionUID = 1011L;
    private static final Logger logger = LogManager.getLogger(ReshuffleLorenzoCard.class);


    public ReshuffleLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add one faith points to Lorenzo. Then it restores the original state of LorenzoDeck and it shuffles it.
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) throws EndAlreadyReachedException {
        game.getLorenzoDeck().backToOriginalAndShuffle();
        try {
            game.getLorenzo().getFaithTrack().move(1, game);
        } catch (InvalidStepsException e) {
            logger.error("Even though the number of steps is constant to 1, this exception has been thrown: " + e);
        }
    }
}
