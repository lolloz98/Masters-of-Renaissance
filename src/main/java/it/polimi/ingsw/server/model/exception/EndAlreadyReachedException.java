package it.polimi.ingsw.server.model.exception;

public class EndAlreadyReachedException extends  ModelException{
    public EndAlreadyReachedException() {
        super("The end of the faith track has already been reached, cannot move further");
    }
}
