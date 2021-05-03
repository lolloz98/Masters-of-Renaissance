package it.polimi.ingsw.messages.answers;

public class CreateGameAnswer extends Answer {
    private static final long serialVersionUID = 51L;

    private final int gameId;

    private final int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public int getGameId() {
        return gameId;
    }

    public CreateGameAnswer(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
