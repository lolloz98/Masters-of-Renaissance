package it.polimi.ingsw.server.model.exception;

public class MatrixIndexOutOfBoundException extends ModelException {
    public MatrixIndexOutOfBoundException() {
        super("selected index for push out of bound");
    }
}
