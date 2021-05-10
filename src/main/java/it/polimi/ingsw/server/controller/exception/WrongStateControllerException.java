package it.polimi.ingsw.server.controller.exception;

public class WrongStateControllerException extends ControllerException {
    public WrongStateControllerException() {
    }

    public WrongStateControllerException(String s) {
        super(s);
    }
}
