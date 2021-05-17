package it.polimi.ingsw.server.model.exception;

public class NotLorenzoTurnException extends ModelException {
    public NotLorenzoTurnException() {
        super("This is not Lorenzo's turn: you cannot perform a Lorenzo action.");
    }
}
