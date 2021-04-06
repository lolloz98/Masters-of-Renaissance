package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.SinglePlayer;
import it.polimi.ingsw.model.game.TurnSingle;

public class FaithLorenzoCard extends LorenzoCard {
    public FaithLorenzoCard(int id) {
        super(id);
    }

    /**
     * Add 2 faith points to Lorenzo.
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) {
        // TODO: check
        game.getLorenzo().getFaithTrack().move(2, game);
    }
}
