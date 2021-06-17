package it.polimi.ingsw.messages.answers;

public class CreateGameAnswer extends Answer {
    private static final long serialVersionUID = 51L;
    /**
     * name of the player which is creating the game
     */
    private final String name;

    public CreateGameAnswer(int gameId, int playerId, String name) {
        super(gameId, playerId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
