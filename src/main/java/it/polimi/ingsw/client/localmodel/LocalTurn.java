package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class LocalTurn extends Observable implements Serializable {
    protected boolean mainActionOccurred;
    protected boolean productionsActivated;
    protected boolean marketActivated;

    public synchronized ArrayList<String> getHistory() {
        return history;
    }

    public synchronized void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    protected ArrayList<String> history;

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
