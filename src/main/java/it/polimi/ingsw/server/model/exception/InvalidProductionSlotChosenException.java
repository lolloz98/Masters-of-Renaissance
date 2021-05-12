package it.polimi.ingsw.server.model.exception;

public class InvalidProductionSlotChosenException extends ModelException{
    public InvalidProductionSlotChosenException() {
        super("The production slot selected is invalid");
    }
}
