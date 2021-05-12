package it.polimi.ingsw.server.model.exception;

public class ActivateDiscardedCardException extends ModelException {
    public ActivateDiscardedCardException() {
        super("Cannot activate a discarded card");
    }
}
