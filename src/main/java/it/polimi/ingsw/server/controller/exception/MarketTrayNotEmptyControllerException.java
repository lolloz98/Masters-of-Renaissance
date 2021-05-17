package it.polimi.ingsw.server.controller.exception;

public class MarketTrayNotEmptyControllerException extends InvalidActionControllerException {
    public MarketTrayNotEmptyControllerException(){
        super("The market has resources to be gained. Please choose a combination and gain those resources before passing the turn.");
    }
}
