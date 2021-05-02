package it.polimi.ingsw.server.requests;

import java.io.Serializable;

public abstract class ClientMessage implements Serializable {
    private int gameId;
    private int playerId;

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
