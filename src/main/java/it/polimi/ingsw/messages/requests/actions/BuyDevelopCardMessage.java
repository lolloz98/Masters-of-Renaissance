package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.model.cards.Color;

/**
 * Request for buying a develop card
 */
public class BuyDevelopCardMessage extends ClientMessage {
    private static final long serialVersionUID = 111L;
    public final int level;
    public final Color color;
    /**
     * on which deck do we have to put the card just bought
     */
    public final int whichDeck;

    public BuyDevelopCardMessage(int gameId, int playerId, int level, Color color, int whichDeck) {
        super(gameId, playerId);
        this.level = level;
        this.color = color;
        this.whichDeck = whichDeck;
    }

    public int getWhichDeck() {
        return whichDeck;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }
}
