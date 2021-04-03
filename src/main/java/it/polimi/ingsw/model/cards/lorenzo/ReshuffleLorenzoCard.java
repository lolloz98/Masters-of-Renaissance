package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.game.SinglePlayer;

public class ReshuffleLorenzoCard extends LorenzoCard {
    public ReshuffleLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add one faith points to Lorenzo. Then it restores the original state of LorenzoDeck and it shuffles it.
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) {
        // TODO: add one faith point to lorenzo. Call game.lorenzoDeck.backToOriginalAndShuffle
    }
}
