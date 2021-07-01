package it.polimi.ingsw.messages.answers.endgameanswer;

import it.polimi.ingsw.messages.answers.Answer;

/**
 * if something unexpected happens, and the game has to be destroyed this is the message the players will receive
 */
public class DestroyedGameAnswer extends Answer {
    private static final long serialVersionUID = 25L;

    private final String message;

    /**
     * @param gameId   current game id
     * @param playerId id of the player who sent the request
     * @param message message with explanation of the destruction
     */
    public DestroyedGameAnswer(int gameId, int playerId, String message) {
        super(gameId, playerId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
