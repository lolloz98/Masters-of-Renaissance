package it.polimi.ingsw.server.model.exception;

public class InvalidResourceQuantityToDepotException extends ModelException{
    public InvalidResourceQuantityToDepotException(int maxResInDepot) {
        super("The depot cannot hold less than 0 resources nor more than " + maxResInDepot);
    }
}

