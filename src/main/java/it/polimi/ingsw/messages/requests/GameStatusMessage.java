package it.polimi.ingsw.messages.requests;

/**
 * request the whole game
 */
public class GameStatusMessage extends ClientMessage{
    private static final long serialVersionUID = 60L;

    public GameStatusMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
