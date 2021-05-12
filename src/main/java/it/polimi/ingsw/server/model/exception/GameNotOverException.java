package it.polimi.ingsw.server.model.exception;

public class GameNotOverException extends ModelException {
    public GameNotOverException() {
        super("the game is not over, cannot perform this action");
    }
}
