package it.polimi.ingsw.client.localmodel;


import it.polimi.ingsw.client.gui.controllergui.RemoveLeadersGUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class LocalGame<T extends LocalTurn> extends Observable implements Serializable {
    private static final Logger logger = LogManager.getLogger(LocalGame.class);

    protected LocalDevelopmentGrid localDevelopmentGrid;
    protected LocalMarket localMarket;
    protected int gameId;
    /**
     * notify localGame observer for an update of localTurn
     */
    protected T localTurn;
    protected final Error error = new Error();
    protected LocalGameState state;

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

    public synchronized Error getError() {
        logger.debug("error in localGame: " + error);
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
            localBoard.notifyObservers();
        }
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

    public abstract boolean isMainPlayerTurn();
}
