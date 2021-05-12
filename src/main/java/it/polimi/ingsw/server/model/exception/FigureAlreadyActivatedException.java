package it.polimi.ingsw.server.model.exception;

public class FigureAlreadyActivatedException extends ModelException {
    public FigureAlreadyActivatedException() {
        super("The figure was already activated");
    }
}
