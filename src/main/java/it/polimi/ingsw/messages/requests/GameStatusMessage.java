package it.polimi.ingsw.messages.requests;

public class GameStatusMessage extends ClientMessage{
    private static final long serialVersionUID = 114L;

    public GameStatusMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
