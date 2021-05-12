package it.polimi.ingsw.server.model.exception;

public class ProductionsResourcesNotFlushedException extends ModelException {
    public ProductionsResourcesNotFlushedException() {
        super("Production resources not flushed yet");
    }
}
