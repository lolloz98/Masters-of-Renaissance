package it.polimi.ingsw.server.controller.exception;

public class NotCurrentPlayerException extends ControllerException{
    public NotCurrentPlayerException() {
    }

    public NotCurrentPlayerException(String s) {
        super(s);
    }
}
