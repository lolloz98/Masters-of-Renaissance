package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalModelAbstract;

public abstract class View {
    protected LocalGame localGame;

    public abstract void draw();
    public abstract void notifyAction(LocalModelAbstract localModelAbstract);
    public abstract void handleCommand(String line);

    public void drawTurn(){
        System.out.println("Current player: " + localGame.getPlayerNames().get(localGame.getCurrentPlayerId()));
        System.out.println(" ");
    }
}
