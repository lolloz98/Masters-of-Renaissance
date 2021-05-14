package it.polimi.ingsw.server.controller.exception;

public class NoSuchControllerException extends ControllerException{
    public NoSuchControllerException() {
        super("There is no game with the given ID");
    }
}
