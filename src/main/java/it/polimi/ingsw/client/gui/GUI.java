package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.LocalGame;

public class GUI extends UI {
    private int whoIAmSeeingId = -1;

    public int getWhoIAmSeeingId() {
        if(whoIAmSeeingId == -1) return whoIAmSeeingId = getLocalGame().getGameId();
        return whoIAmSeeingId;
    }

    public void setGameHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
        Thread thread = new Thread(gameHandler);

        // When the application is closed, we want to close the thread as well
        thread.setDaemon(true);

        thread.start();
    }

    public GameHandler getGameHandler(){
        return gameHandler;
    }
}
