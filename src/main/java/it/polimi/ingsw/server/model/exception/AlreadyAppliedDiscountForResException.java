package it.polimi.ingsw.server.model.exception;

import it.polimi.ingsw.server.model.game.Resource;

public class AlreadyAppliedDiscountForResException extends ModelException {
    public AlreadyAppliedDiscountForResException(Resource r) {
        super("The discount was already applied for " + r.name());
    }
}
