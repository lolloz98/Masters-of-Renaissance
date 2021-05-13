package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.localmodel.LocalGame;

public abstract class GameView extends View {
    protected LocalGame localGame;

    public abstract void draw();
    public abstract void notifyUpdate();
    public abstract void handleCommand(String ans); // todo generic game commands (moving between views)

    public void drawTurn(){
        // todo print turn info
    }
}
