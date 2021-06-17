package it.polimi.ingsw.messages.requests;

/**
 * request to pass the turn
 */
public class FinishTurnMessage extends ClientMessage {
    private static final long serialVersionUID = 113L;

    public FinishTurnMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
