package it.polimi.ingsw.messages.answers;

public class JoinGameAnswer extends Answer {
    private static final long serialVersionUID = 52L;

    private final int gameId;

    private final int playerId;

    public JoinGameAnswer(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getGameId() {
        return gameId;
    }

}
