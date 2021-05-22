package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.localmodel.LocalGame;

public class GUI extends UI {

    public void setGameHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    public GameHandler getGameHandler(){
        return gameHandler;
    }
}
