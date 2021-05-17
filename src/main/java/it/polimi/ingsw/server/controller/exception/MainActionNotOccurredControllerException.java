package it.polimi.ingsw.server.controller.exception;

public class MainActionNotOccurredControllerException extends InvalidActionControllerException {
    public MainActionNotOccurredControllerException() {
        super("You have not done a main action yet: you cannot pass the turn!");
    }
}
