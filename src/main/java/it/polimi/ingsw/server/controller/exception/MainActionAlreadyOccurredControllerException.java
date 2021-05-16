package it.polimi.ingsw.server.controller.exception;

public class MainActionAlreadyOccurredControllerException extends ControllerException {
    public MainActionAlreadyOccurredControllerException() {
        super("Invalid action. In this turn, you have already done your main action");
    }
}
