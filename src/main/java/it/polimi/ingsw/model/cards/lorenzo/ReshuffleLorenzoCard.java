package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.game.SinglePlayer;

/**
 * LorenzoCard with effect of moving Lorenzo of 1 step on the faithTrack and restoring LorenzoDeck.
 */
public class ReshuffleLorenzoCard extends LorenzoCard {
    public ReshuffleLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add one faith points to Lorenzo. Then it restores the original state of LorenzoDeck and it shuffles it.
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) {
        game.getLorenzo().getFaithTrack().move(1, game);
        game.getLorenzoDeck().backToOriginalAndShuffle();
    }
}
