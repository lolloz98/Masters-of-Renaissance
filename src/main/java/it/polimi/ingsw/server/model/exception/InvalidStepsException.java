package it.polimi.ingsw.server.model.exception;

public class InvalidStepsException extends ModelException{
    public InvalidStepsException() {
        super("Invalid number of steps specified");
    }
}
