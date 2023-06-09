package it.polimi.ingsw.messages.requests;

/**
 * Request to join a game
 */
public class JoinGameMessage extends ClientMessage {
    private static final long serialVersionUID = 61L;

    private final String userName;

    public JoinGameMessage(int gameId, String userName) {
        // playerId useless in this kind of request
        super(gameId, -1);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
