package it.polimi.ingsw.messages.requests;

public class FinishTurn extends ClientMessage {
    private static final long serialVersionUID = 113L;

    public FinishTurn(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
