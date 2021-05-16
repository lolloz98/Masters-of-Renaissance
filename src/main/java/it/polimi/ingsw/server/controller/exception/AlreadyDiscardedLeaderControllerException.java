package it.polimi.ingsw.server.controller.exception;

public class AlreadyDiscardedLeaderControllerException extends ControllerException {
    public AlreadyDiscardedLeaderControllerException() {
        super("The leader selected was already discarded");
    }
}
