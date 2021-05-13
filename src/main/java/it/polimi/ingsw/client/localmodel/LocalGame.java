package it.polimi.ingsw.client.localmodel;


import java.io.Serializable;

public abstract class LocalGame<T extends LocalTurn> extends Observable implements Serializable {
    protected LocalDevelopmentGrid localDevelopmentGrid;
    protected LocalMarket localMarket;
    protected int gameId;
    protected T localTurn;
    protected final Error error = new Error();
    protected LocalGameState state;

    public Error getError() {
        return error;
    }

    public LocalDevelopmentGrid getLocalDevelopmentGrid() {
        return localDevelopmentGrid;
    }

    public LocalMarket getLocalMarket() {
        return localMarket;
    }

    public synchronized int getGameId() {
        return gameId;
    }

    public synchronized void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public T getLocalTurn() {
        return localTurn;
    }

    public synchronized LocalGameState getState() {
        return state;
    }

    public synchronized void setState(LocalGameState state) {
        this.state = state;
        notifyObserver();
    }

    public LocalGame(){
        this.localDevelopmentGrid = new LocalDevelopmentGrid();
        this.localMarket = new LocalMarket();
    }

    public LocalGame(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, T localTurn, LocalGameState state){
        this.gameId = gameId;
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localMarket = localMarket;
        this.localTurn = localTurn;
        this.state = state;
    }
}
