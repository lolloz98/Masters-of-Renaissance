package it.polimi.ingsw.messages.requests;

public class GetGameStatus extends ClientMessage{
    private static final long serialVersionUID = 114L;

    public GetGameStatus(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
