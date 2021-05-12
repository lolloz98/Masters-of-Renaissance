package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.InvalidArgumentException;

public class Player {
    private final String name;
    private final int playerId;
    private final Board board;

    public Player(String name, int playerId) throws InvalidArgumentException {
        this.name = name;
        this.playerId = playerId;
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }
}
