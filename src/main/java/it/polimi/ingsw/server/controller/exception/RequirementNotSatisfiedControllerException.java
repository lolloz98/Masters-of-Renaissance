package it.polimi.ingsw.server.controller.exception;

public class RequirementNotSatisfiedControllerException extends ControllerException {
    public RequirementNotSatisfiedControllerException() {
        super("At this time, you don't have the requirements to activate this leader card");
    }
}
