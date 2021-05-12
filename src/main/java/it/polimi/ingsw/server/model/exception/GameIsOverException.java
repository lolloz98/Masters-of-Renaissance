package it.polimi.ingsw.server.model.exception;

public class GameIsOverException extends ModelException {
    public GameIsOverException() {
        super("the game is over, cannot perform chosen action");
    }
}
