package it.polimi.ingsw.model.player;

public class Player {

    private final String playerId;
    private final int playerNumber;
    private final Board board;

    public Player(String playerid, int playerNumber) {
        this.playerId = playerid;
        this.playerNumber = playerNumber;
        board= new Board();
    }

    public Board getBoard() {
        return board;
    }

}
