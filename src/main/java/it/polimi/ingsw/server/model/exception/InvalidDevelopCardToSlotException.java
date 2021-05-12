package it.polimi.ingsw.server.model.exception;

public class InvalidDevelopCardToSlotException extends ModelException{
    public InvalidDevelopCardToSlotException() {
        super("The given card cannot be put on the specified slot at this time");
    }
}
