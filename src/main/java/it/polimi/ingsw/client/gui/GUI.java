package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.gui.componentsgui.ImageCache;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.awt.*;

public class GUI extends UI {
    private int whoIAmSeeingId = -1;

    public int getWhoIAmSeeingId() {
        if(whoIAmSeeingId == -1) return whoIAmSeeingId = getLocalGame().getMainPlayer().getId();
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

    public void setWhoIAmSeeingId(Integer playerId){
        whoIAmSeeingId = playerId;
    }

    @Override
    public void newSinglePlayer() {
        super.newSinglePlayer();
        ImageCache.setIsSinglePlayer(true);
    }

    @Override
    public void newMultiPlayer() {
        super.newMultiPlayer();
        ImageCache.setIsSinglePlayer(false);
    }
}
