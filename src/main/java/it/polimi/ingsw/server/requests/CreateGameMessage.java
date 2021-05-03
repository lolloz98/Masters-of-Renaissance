package it.polimi.ingsw.server.requests;

/**
 * Request to create a game
 */
public class CreateGameMessage extends ClientMessage {
    private static final long serialVersionUID = 101L;

    private final int playersNumber;
    private final String userName;

    public CreateGameMessage(int playersNumber, String userName) {
        // gameId and playerId useless in this kind of request
        super(-1, -1);
        this.playersNumber = playersNumber;
        this.userName = userName;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public String getUserName() {
        return userName;
    }

}
