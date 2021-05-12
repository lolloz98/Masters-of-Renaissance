package it.polimi.ingsw.server.model.exception;

public class AlreadyAppliedLeaderCardException extends ModelException {
    public AlreadyAppliedLeaderCardException() {
        super("The effect of the specified leader was already applied");
    }
}
