package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameHandler;
import it.polimi.ingsw.client.LocalSingleGameHandler;
import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.gui.componentsgui.ImageCache;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.awt.*;

public class GUI extends UI {
    private int whoIAmSeeingId = -1;

    public int getWhoIAmSeeingId() {
        if(whoIAmSeeingId == -1) return whoIAmSeeingId = getLocalGame().getMainPlayer().getId();
        return whoIAmSeeingId;
    }

    public void resetWhoIAmSeeingId(){
        whoIAmSeeingId = -1;
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
