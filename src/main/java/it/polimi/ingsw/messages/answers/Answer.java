package it.polimi.ingsw.messages.answers;

import java.io.Serializable;

/**
 * abstract class containing all the useful info to update the local game.
 */
public abstract class Answer implements Serializable {
    private static final long serialVersionUID = 42L;

    private final int gameId;

    /**
     * player who asked the request generated this answer
     */
    private final int playerId;

    /**
     * @param gameId current game id
     * @param playerId id of the player who sent the request
     */
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
