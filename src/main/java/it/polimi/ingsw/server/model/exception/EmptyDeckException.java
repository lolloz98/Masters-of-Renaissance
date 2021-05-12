package it.polimi.ingsw.server.model.exception;

public class EmptyDeckException extends ModelException {
    public EmptyDeckException() {
        super("Cannot perform operation on deck: it's empty");
    }
}
