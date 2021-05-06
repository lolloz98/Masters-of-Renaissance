package it.polimi.ingsw.server.controller.exception;

/**
 * class which handle the expected exceptions generated during the game
 */
public class ControllerException extends Exception{
    public ControllerException(){}
    public ControllerException(String s) {
        super(s);
    }
}
