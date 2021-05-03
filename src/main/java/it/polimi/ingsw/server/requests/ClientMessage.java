package it.polimi.ingsw.server.requests;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    private static final long serialVersionUID = 100L;

    private final int gameId;
    private final int playerId;

    public ClientMessage(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }
}