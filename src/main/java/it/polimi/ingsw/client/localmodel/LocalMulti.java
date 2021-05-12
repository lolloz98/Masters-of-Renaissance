package it.polimi.ingsw.client.localmodel;

import java.util.ArrayList;

public class LocalMulti extends LocalGame<LocalTurnMulti>{
    /**
     * id of the player playing on the specific device
     */
    private int mainPlayerId;
    private ArrayList<LocalPlayer> localPlayers;

    public synchronized void setLocalPlayers(ArrayList<LocalPlayer> localPlayers) {
        this.localPlayers = localPlayers;
        notifyObserver();
    }

    public synchronized void addLocalPlayer(LocalPlayer localPlayer) {
        this.localPlayers.add(localPlayer);
        notifyObserver();
    }

    public synchronized int getMainPlayerId() {
        return mainPlayerId;
    }

    public synchronized void setMainPlayerId(int mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }

    public synchronized ArrayList<LocalPlayer> getLocalPlayers() {
        return localPlayers;
    }

    public synchronized LocalPlayer getMainPlayer(){
        for(LocalPlayer p : localPlayers){
            if (p.getId() == mainPlayerId) return p;
        }
        return null;
    }

    public synchronized int getMainPlayerPosition(){
        LocalPlayer mainPlayer = getMainPlayer();
        return localPlayers.indexOf(mainPlayer);
    }

    public LocalMulti(){
        super();
        this.localTurn = new LocalTurnMulti();
        localPlayers = new ArrayList<>();
        state = LocalGameState.NEW;
    }

    public LocalMulti(int gameId, LocalDevelopmentGrid localDevelopmentGrid, LocalMarket localMarket, LocalTurnMulti localTurn, ArrayList<LocalPlayer> localPlayers, int mainPlayerId){
        super(gameId,localDevelopmentGrid, localMarket, localTurn);
        this.localPlayers = localPlayers;
        this.mainPlayerId = mainPlayerId;
    }
}
