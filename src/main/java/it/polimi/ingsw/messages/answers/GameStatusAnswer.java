package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalTurnMulti;

/**
 * this answer sends the player the entire game
 */
public class GameStatusAnswer extends Answer{
    private static final long serialVersionUID = 45L;

    /**
     * player who will receive this specific answer
     */
    private final int playerIdReceiver;
    private final LocalGame<?> game;

    /**
     * @param gameId current game id
     * @param playerId id of the player who sent the request
     * @param playerIdReceiver id of the player who will receive this answer
     * @param game current localGame
     */
    public GameStatusAnswer(int gameId, int playerId, int playerIdReceiver, LocalGame<?> game) {
        super(gameId, playerId);
        this.playerIdReceiver = playerIdReceiver;
        this.game = game;
    }

    public int getPlayerIdReceiver() {
        return playerIdReceiver;
    }

    public LocalGame<?> getGame() {
        return game;
    }
}
