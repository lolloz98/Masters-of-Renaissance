package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.localmodel.LocalGame;

public abstract class GameView extends View {
    protected LocalGame localGame;

    public abstract void draw();
    public abstract void notifyUpdate();

    public void handleCommand(String ans){
        switch (ans){
            // todo: generic game view commands (moving through views)
        }
    }

    public void drawTurn(){
        // todo print turn info
    }
}
