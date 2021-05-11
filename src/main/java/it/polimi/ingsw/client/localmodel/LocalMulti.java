package it.polimi.ingsw.client.localmodel;

import java.util.ArrayList;

public class LocalMulti extends LocalGame<LocalTurnMulti>{
    private int mainPlayerId;
    private ArrayList<LocalPlayer> localPlayers;

    public int getMainPlayerId() {
        return mainPlayerId;
    }

    public void setMainPlayerId(int mainPlayerId) {
        this.mainPlayerId = mainPlayerId;
    }

    public ArrayList<LocalPlayer> getLocalPlayers() {
        return localPlayers;
    }

    public LocalMulti(){
        super();
        this.localTurn = new LocalTurnMulti();
        localPlayers = new ArrayList<>();
    }
}
