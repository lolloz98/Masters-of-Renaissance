package it.polimi.ingsw.server.model.exception;

import it.polimi.ingsw.enums.Resource;

public class InvalidTypeOfResourceToDepotException extends ModelException{
    public InvalidTypeOfResourceToDepotException(Resource r) {
        super("This depot cannot contain " + r.name());
    }
}
