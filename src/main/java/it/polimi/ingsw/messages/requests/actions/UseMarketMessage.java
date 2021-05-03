package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;

/**
 * Request to use the market (push the marbles on the market)
 */
public class UseMarketMessage extends ClientMessage {
    private static final long serialVersionUID = 107L;

    /**
     * true if I want to push the marbles on a row, otherwise I want to push them on a column
     */
    private final boolean onRow;
    /**
     * index of the row or the column on which I want to push the marbles.
     */
    private final int index;


    public boolean isOnRow() {
        return onRow;
    }

    public int getIndex() {
        return index;
    }

    public UseMarketMessage(int gameId, int playerId, boolean onRow, int index) {
        super(gameId, playerId);
        this.onRow = onRow;
        this.index = index;
    }
}
