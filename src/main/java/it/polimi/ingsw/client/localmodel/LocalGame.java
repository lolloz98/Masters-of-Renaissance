package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.UI;

import java.util.ArrayList;

public class LocalGame extends LocalModelAbstract{
    private int currentPlayerId;

    public synchronized int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public synchronized void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
        ui.notifyAction(this);
    }

    public synchronized boolean isMultiPlayer() {
        return multiPlayer;
    }

    public synchronized void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }

    public synchronized ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public synchronized void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    private boolean multiPlayer;
    private ArrayList<String> playerNames;
    private UI ui;

    public LocalGame(UI ui){
        this.ui = ui;
        //todo substitute with real constructor
        this.playerNames = new ArrayList<>(){{
            add("firstplayer");
            add("secondplayer");
            add("thirdplayer");
            add("fourthplayer");
        }};
    }
}
