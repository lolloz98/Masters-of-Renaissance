package it.polimi.ingsw.model.cards.lorenzo;

import it.polimi.ingsw.model.cards.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.TurnSingle;

public class DevelopLorenzoCard extends LorenzoCard {
    private final Color color;

    public DevelopLorenzoCard(int id, Color color) {
        super(id);
        this.color = color;
    }

    /**
     * Discards two develop cards of color equal to this.color
     * @param game current single game player
     */
    @Override
    public void applyEffect(Game<TurnSingle> game) {
        // TODO: discards two cards from deck of develop
    }

    public Color getColor(){
        return color;
    }
}