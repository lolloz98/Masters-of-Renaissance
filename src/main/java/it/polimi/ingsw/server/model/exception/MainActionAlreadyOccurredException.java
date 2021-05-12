package it.polimi.ingsw.server.model.exception;

public class MainActionAlreadyOccurredException extends ModelException {
    public MainActionAlreadyOccurredException() {
        super("a main action already occurred, cannot do another one in this turn");
    }
}
