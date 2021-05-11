package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.localmodel.LocalGame;

public abstract class GameView extends View {
    protected LocalGame localGame;

    public abstract void draw();
    public abstract void notifyAction();
    public abstract void handleCommand(int ans); // todo generic game commands (moving between views)

    public void drawTurn(){
        // todo print turn info
    }
}
