package it.polimi.ingsw.server.model.exception;

public class PlayersOutOfBoundException extends ModelException {
    public PlayersOutOfBoundException() {
        super("the number of players selected is out of bound");
    }
}
