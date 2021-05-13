package it.polimi.ingsw.messages.answers;

public class CreateGameAnswer extends Answer {
    private static final long serialVersionUID = 51L;
    private final String name;

    public CreateGameAnswer(int gameId, int playerId, String name) {
        super(gameId, playerId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
