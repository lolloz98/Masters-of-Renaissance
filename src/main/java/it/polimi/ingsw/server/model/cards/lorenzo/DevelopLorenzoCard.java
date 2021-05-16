package it.polimi.ingsw.server.model.cards.lorenzo;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;

import java.util.TreeMap;

/**
 * LorenzoCard with effect of discarding 2 developCards of a certain color from the game.
 */
public class DevelopLorenzoCard extends LorenzoCard {
    private static final long serialVersionUID = 1008L;

    private final Color color;

    public DevelopLorenzoCard(int id, Color color) {
        super(id);
        this.color = color;
    }

    /**
     * Discards, if possible, two develop cards of color equal to this.color
     *
     * @param game current single game player
     */
    @Override
    public void applyEffect(SinglePlayer game) throws EmptyDeckException {
        TreeMap<Integer, DeckDevelop> decks = game.getDecksDevelop().get(color);
        for (int i = 0; i < 2; i++) removeCardFromDevelop(decks);
    }

    /**
     * if there is a non-empty deck, remove one card
     *
     * @param decks TreeMap of deck develop of color equal to this.color
     */
    private void removeCardFromDevelop(TreeMap<Integer, DeckDevelop> decks) throws EmptyDeckException {
        for (Integer i : decks.keySet()) {
            if (!decks.get(i).isEmpty()) {
                decks.get(i).drawCard();
                return;
            }
        }
    }

    public Color getColor() {
        return color;
    }
}
