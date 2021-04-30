package it.polimi.ingsw.requests;

public class CreateGameMessage extends ClientMessage{
    private int playersNumber;
    private String userName;

    public CreateGameMessage(int playersNumber, String userName) {
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
