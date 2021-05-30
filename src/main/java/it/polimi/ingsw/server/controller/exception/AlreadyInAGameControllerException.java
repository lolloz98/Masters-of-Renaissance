package it.polimi.ingsw.server.controller.exception;

public class AlreadyInAGameControllerException extends ControllerException {
    public AlreadyInAGameControllerException(){
        super("You cannot rejoin a game while playing in another one.");
    }
}
