package it.polimi.ingsw.client.localmodel;


public abstract class LocalGame<T extends LocalTurn> extends Observable {
    protected LocalDevelopmentGrid localDevelopmentGrid;
    protected LocalMarket localMarket;
    protected int gameId;
    protected T localTurn;
    protected final Error error;

    public Error getError() {
        return error;
    }

    public LocalDevelopmentGrid getLocalDevelopmentGrid() {
        return localDevelopmentGrid;
    }

    public LocalMarket getLocalMarket() {
        return localMarket;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public T getLocalTurn() {
        return localTurn;
    }

    public LocalGame(){
        this.localDevelopmentGrid = new LocalDevelopmentGrid();
        this.localMarket = new LocalMarket();
        error = new Error();
    }
}
