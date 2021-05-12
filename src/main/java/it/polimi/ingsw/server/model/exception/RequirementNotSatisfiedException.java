package it.polimi.ingsw.server.model.exception;

public class RequirementNotSatisfiedException extends ModelException {
    public RequirementNotSatisfiedException() {
        super("The requirement for the activation of the specified leaderCard is not satisfied");
    }
}
