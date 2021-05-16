package it.polimi.ingsw.server.controller.exception;

public class AlreadyActiveLeaderControllerException extends ControllerException {
    public AlreadyActiveLeaderControllerException(){
        super("The selected leader was already activated");
    }
}
