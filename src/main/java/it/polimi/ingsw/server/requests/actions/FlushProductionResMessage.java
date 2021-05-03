package it.polimi.ingsw.server.requests.actions;

import it.polimi.ingsw.server.requests.ClientMessage;

public class FlushProductionResMessage extends ClientMessage {
    private static final long serialVersionUID = 110L;

    public FlushProductionResMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
