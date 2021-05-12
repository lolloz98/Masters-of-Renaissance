package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;

public class LocalMulti extends LocalGame<LocalTurnMulti>{
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

    public LocalMulti(){
        super();
        this.localTurn = new LocalTurnMulti();
        localPlayers = new ArrayList<>();
        state = LocalGameState.NEW;
    }
}
