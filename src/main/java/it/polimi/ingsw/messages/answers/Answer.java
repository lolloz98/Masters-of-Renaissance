package it.polimi.ingsw.messages.answers;

import java.io.Serializable;

public abstract class Answer implements Serializable {
    private static final long serialVersionUID = 50L;

    private final int gameId;

    /**
     * player who asked the request who generated this answer
     */
    private final int playerId;

    public Answer(int gameId, int playerId) {
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
