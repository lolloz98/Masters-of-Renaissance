package it.polimi.ingsw.server.controller.exception;

public class InvalidArgumentControllerException extends ControllerException {
    public InvalidArgumentControllerException(String s) {
        super(s);
    }

    public InvalidArgumentControllerException(String message, int ignore) {
        super("While making your choices something went wrong: " + message);
    }
}
