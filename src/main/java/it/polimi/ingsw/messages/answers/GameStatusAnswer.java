package it.polimi.ingsw.messages.answers;

/**
 * this answer sends the player the entire game
 */
public class GameStatusAnswer extends Answer{

    public GameStatusAnswer(int gameId, int playerId) {
        super(gameId, playerId);
        // todo decide how to pass the game status
    }
}
