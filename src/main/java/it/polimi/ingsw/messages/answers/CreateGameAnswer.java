package it.polimi.ingsw.messages.answers;

public class CreateGameAnswer extends Answer {
    private static final long serialVersionUID = 51L;

    public CreateGameAnswer(int gameId, int playerId) {
        super(gameId, playerId);
    }
}
