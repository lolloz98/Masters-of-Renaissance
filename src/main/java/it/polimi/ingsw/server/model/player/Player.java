package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.InvalidArgumentException;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1031L;

    private final String name;
    private final int playerId;
    private final Board board;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player tmp = (Player) obj;
            return name.equals(tmp.name) && playerId == tmp.playerId && board.equals(tmp.board);
        }
        return false;
    }

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
