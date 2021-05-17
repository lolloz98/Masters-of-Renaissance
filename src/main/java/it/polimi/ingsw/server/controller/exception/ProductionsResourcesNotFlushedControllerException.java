package it.polimi.ingsw.server.controller.exception;

public class ProductionsResourcesNotFlushedControllerException extends InvalidActionControllerException {
    public ProductionsResourcesNotFlushedControllerException(){
        super("The productions have resources to be gained. Please flush those resources to the StrongBox before passing the turn.");
    }
}
