package it.polimi.ingsw.server.model.exception;

public class FigureAlreadyDiscardedException extends ModelException {
    public FigureAlreadyDiscardedException() {
        super("The figure was already discarded");
    }
}
