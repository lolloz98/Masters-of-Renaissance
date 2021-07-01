package it.polimi.ingsw.messages.requests.actions;

import it.polimi.ingsw.messages.requests.ClientMessage;

/**
 * Request to flush the gained production resources on the board
 */
public class FlushProductionResMessage extends ClientMessage {
    private static final long serialVersionUID = 50L;

    public FlushProductionResMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
