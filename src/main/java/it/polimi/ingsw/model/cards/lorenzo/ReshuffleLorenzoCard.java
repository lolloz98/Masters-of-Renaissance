package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.TurnSingle;

public class ReshuffleLorenzoCard extends LorenzoCard {
    public ReshuffleLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add one faith points to Lorenzo. Then it restores the original state of LorenzoDeck and it shuffles it.
     * @param game current single game player
     */
    @Override
    public void applyEffect(Game<TurnSingle> game) {
        // TODO: add one faith point to lorenzo. Call game.lorenzoDeck.backToOriginalAndShuffle
    }
}
