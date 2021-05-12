package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.exception.EndAlreadyReachedException;
import it.polimi.ingsw.server.model.exception.InvalidStepsException;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.game.SinglePlayer;

/**
 * LorenzoCard with effect of moving Lorenzo of 2 steps on the faithTrack.
 */
public class FaithLorenzoCard extends LorenzoCard {
    public FaithLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add 2 faith points to Lorenzo.
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) throws ModelException {
        game.getLorenzo().getFaithTrack().move(2, game);
    }
}
