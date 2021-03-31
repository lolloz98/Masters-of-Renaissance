package it.polimi.ingsw.model.player;

public class Player {

    private String playerid;
    private int playernumber;
    private Board board;

    public Player(String playerid, int playerNumber) {
        this.playerid = playerid;
        playernumber = playerNumber;
        board= new Board();
    }

    public Board getBoard() {
        return board;
    }

}
