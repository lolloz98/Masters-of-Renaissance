package it.polimi.ingsw.requests;

public class JoinGameMessage extends ClientMessage{
    private int id;
    private String userName;

    public JoinGameMessage(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

}
