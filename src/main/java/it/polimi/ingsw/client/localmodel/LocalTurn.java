package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

/**
 * notify localGame observer for an update of this class
 */
public abstract class LocalTurn extends Observable implements Serializable {
    protected boolean mainActionOccurred;
    protected boolean productionsActivated;
    protected boolean marketActivated;

    protected LocalTurn() {
    }

    public synchronized boolean isMainActionOccurred() {
        return mainActionOccurred;
    }

    public synchronized void setMainActionOccurred(boolean mainActionOccurred) {
        this.mainActionOccurred = mainActionOccurred;
    }

    public synchronized boolean isProductionsActivated() {
        return productionsActivated;
    }

    public synchronized void setProductionsActivated(boolean productionsActivated) {
        this.productionsActivated = productionsActivated;
    }

    public synchronized boolean isMarketActivated() {
        return marketActivated;
    }

    public synchronized void setMarketActivated(boolean marketActivated) {
        this.marketActivated = marketActivated;
    }

    protected LocalTurn(boolean mainActionOccurred, boolean productionsActivated, boolean marketActivated){
        this.mainActionOccurred = mainActionOccurred;
        this.productionsActivated = productionsActivated;
        this.marketActivated = marketActivated;
    }
}
