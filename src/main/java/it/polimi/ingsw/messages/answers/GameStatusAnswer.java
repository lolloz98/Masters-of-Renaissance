package it.polimi.ingsw.messages.answers;

/**
 * this answer sends the player the entire game
 */
public class GameStatusAnswer extends Answer{
    /**
     * player who will receive this specific answer
     */
    private final int playerIdReceiver;

    public GameStatusAnswer(int gameId, int playerId, int playerIdReceiver) {
        super(gameId, playerId);
        // todo decide how to pass the game status
        this.playerIdReceiver = playerIdReceiver;
    }

    public int getPlayerIdReceiver() {
        return playerIdReceiver;
    }
}
