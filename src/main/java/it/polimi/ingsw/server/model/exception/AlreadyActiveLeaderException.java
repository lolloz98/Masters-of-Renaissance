package it.polimi.ingsw.server.model.exception;

public class AlreadyActiveLeaderException extends ModelException {
    public AlreadyActiveLeaderException() {
        super("The specified leaderCard is already active");
    }
}
