package it.polimi.ingsw.client.localmodel;


import java.io.Serializable;
import java.util.ArrayList;

public abstract class LocalGame<T extends LocalTurn> extends Observable implements Serializable {
    protected LocalDevelopmentGrid localDevelopmentGrid;
    protected LocalMarket localMarket;
    protected int gameId;
    /**
     * notify localGame observer for an update of localTurn
     */
    protected T localTurn;
    protected final Error error = new Error();
    protected LocalGameState state;
    /**
     * boolean indicating if the main player has picked the resources for the preparation phase
     */
    protected boolean pickedResources;
    /**
     * boolean indicating if the main player has picked the leaders for the preparation phase
     */
    protected boolean pickedLeaders;

    public synchronized boolean isPickedResources() {
        return pickedResources;
    }

    public synchronized void setPickedResources(boolean pickedResources) {
        this.pickedResources = pickedResources;
    }

    public synchronized boolean isPickedLeaders() {
        return pickedLeaders;
    }

    public synchronized void setPickedLeaders(boolean pickedLeaders) {
        this.pickedLeaders = pickedLeaders;
    }

    public synchronized void setLocalDevelopmentGrid(LocalDevelopmentGrid localDevelopmentGrid) {
        this.localDevelopmentGrid = localDevelopmentGrid;
    }

    public synchronized void setLocalMarket(LocalMarket localMarket) {
        this.localMarket = localMarket;
    }

    public synchronized void setLocalTurn(T localTurn) {
        this.localTurn = localTurn;
    }

    public abstract ArrayList<LocalPlayer> getLocalPlayers();

    public Error getError() {
        return error;
    }

    public synchronized LocalDevelopmentGrid getLocalDevelopmentGrid() {
        return localDevelopmentGrid;
    }

    public synchronized LocalMarket getLocalMarket() {
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
    }

    public abstract LocalPlayer getMainPlayer();

    public abstract LocalPlayer getPlayerById(int playerId);

    public synchronized void updatePlayerFaithTracks(ArrayList<LocalTrack> tracksUpdated){
        ArrayList<LocalPlayer> localPlayers = getLocalPlayers();
        LocalBoard localBoard;

        for(int i=0;i<localPlayers.size();i++){
            localBoard=localPlayers.get(i).getLocalBoard();
            localBoard.setLocalTrack(tracksUpdated.get(i));
            localBoard.notifyObserver();
        }
    }

    public LocalGame(){
        this.localDevelopmentGrid = new LocalDevelopmentGrid();
        this.localMarket = new LocalMarket();
        pickedResources = false;
        pickedLeaders = false;
    }

    public LocalGame(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, T localTurn, LocalGameState state){
        this.gameId = gameId;
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localMarket = localMarket;
        this.localTurn = localTurn;
        this.state = state;
        pickedResources = false;
        pickedLeaders = false;
    }

    public abstract boolean isMainPlayerTurn();
}
