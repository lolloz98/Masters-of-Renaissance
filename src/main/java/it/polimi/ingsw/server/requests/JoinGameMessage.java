package it.polimi.ingsw.server.requests;

public class JoinGameMessage {
    private int gameId;
    private String userName;

    public JoinGameMessage(int gameId, String userName) {
        this.gameId=gameId;
        this.userName = userName;
    }

    public int getId() {
        return gameId;
    }

    public String getUserName() {
        return userName;
    }

}
