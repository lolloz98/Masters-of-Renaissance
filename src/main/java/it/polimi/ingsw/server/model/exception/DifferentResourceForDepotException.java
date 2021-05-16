package it.polimi.ingsw.server.model.exception;

import it.polimi.ingsw.enums.Resource;

public class DifferentResourceForDepotException extends ModelException{
    public DifferentResourceForDepotException(Resource r) {
        super("This depot cannot contain " + r.name() + " in this moment");
    }
}

