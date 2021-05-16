package it.polimi.ingsw.server.model.exception;

import it.polimi.ingsw.enums.Resource;

public class ResourceNotDiscountableException extends ModelException {
    public ResourceNotDiscountableException() {
        super("The resource is not discountable");
    }
    public ResourceNotDiscountableException(Resource res) {
        super("The resource of type " + res.name() + " is not discountable");
    }
}
