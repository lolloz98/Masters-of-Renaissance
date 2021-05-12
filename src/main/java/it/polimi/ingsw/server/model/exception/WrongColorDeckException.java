package it.polimi.ingsw.server.model.exception;

public class WrongColorDeckException extends ModelException {
    public WrongColorDeckException() {
        super("A card was put in a wrong deck (color is different)");
    }
}
