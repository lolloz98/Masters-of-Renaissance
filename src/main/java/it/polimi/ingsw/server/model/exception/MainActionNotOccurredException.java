package it.polimi.ingsw.server.model.exception;

public class MainActionNotOccurredException extends ModelException{
    public MainActionNotOccurredException() {
        super("A main action has not occurred yet");
    }
}
