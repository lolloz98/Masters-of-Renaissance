package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.leader.LeaderCard;

import java.util.ArrayList;

public class Player {

    private final String playerId;
    private final int playerNumber;
    private final Board board;

    public Player(String playerid, int playerNumber) {
        this.playerId = playerid;
        this.playerNumber = playerNumber;
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

}
