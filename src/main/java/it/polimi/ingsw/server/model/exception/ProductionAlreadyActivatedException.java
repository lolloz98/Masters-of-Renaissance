package it.polimi.ingsw.server.model.exception;

public class ProductionAlreadyActivatedException extends ModelException {
    public ProductionAlreadyActivatedException() {
        super("The production specified was already activated during this turn");
    }
}
