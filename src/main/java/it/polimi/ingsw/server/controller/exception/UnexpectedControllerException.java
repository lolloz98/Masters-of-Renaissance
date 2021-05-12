package it.polimi.ingsw.server.controller.exception;

/**
 * all exceptions due to something went wrong due to unexpected problems in the model (such as retrieval of the cards as json file)
 */
public class UnexpectedControllerException extends ControllerException{
    public UnexpectedControllerException(String message){
        super(message);
    }
}
