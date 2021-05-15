package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.SinglePlayer;

/**
 * LorenzoCard with effect of moving Lorenzo of 1 step on the faithTrack and restoring LorenzoDeck.
 */
public class ReshuffleLorenzoCard extends LorenzoCard {
    private static final long serialVersionUID = 1011L;

    public ReshuffleLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add one faith points to Lorenzo. Then it restores the original state of LorenzoDeck and it shuffles it.
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) throws EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        game.getLorenzo().getFaithTrack().move(1, game);
        game.getLorenzoDeck().backToOriginalAndShuffle();
    }
}
