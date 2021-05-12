package it.polimi.ingsw.server.model.exception;

public class WrongLevelDeckException extends ModelException {
    public WrongLevelDeckException() {
        super("A card was put in the a wrong deck (level is different)");
    }
}
