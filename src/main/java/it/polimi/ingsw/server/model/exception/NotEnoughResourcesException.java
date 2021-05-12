package it.polimi.ingsw.server.model.exception;

public class NotEnoughResourcesException extends ModelException{
    public NotEnoughResourcesException() {
        super("Not enough resources to perform the desired action");
    }
}
