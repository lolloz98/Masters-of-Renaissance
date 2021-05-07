package it.polimi.ingsw.messages.answers;

public class JoinGameAnswer extends Answer {
    private static final long serialVersionUID = 52L;

    public JoinGameAnswer(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
