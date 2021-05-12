package it.polimi.ingsw.client.localmodel;

import java.util.ArrayList;

public class LocalMulti extends LocalGame<LocalTurnMulti>{
    private int mainPlayerId;
    private ArrayList<LocalPlayer> localPlayers;
    private MultiState state;

    public synchronized void setLocalPlayers(ArrayList<LocalPlayer> localPlayers) {
        this.localPlayers = localPlayers;
        notifyObserver();
    }

    public synchronized void addLocalPlayer(LocalPlayer localPlayer) {
        this.localPlayers.add(localPlayer);
        notifyObserver();
    }

    public synchronized MultiState getState() {
        return state;
    }

    public synchronized void setState(MultiState state) {
        this.state = state;
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
        state = MultiState.NEW;
    }
}
