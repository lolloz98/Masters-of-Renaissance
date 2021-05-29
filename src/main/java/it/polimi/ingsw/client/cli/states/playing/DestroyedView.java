package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;

public class DestroyedView extends GameView {

    public DestroyedView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        this.localGame = localGame;
    }

    @Override
    public void draw() {
        System.out.println("The game ended because someone left it!");
        System.out.println("You can still move around, type 'quit' exit the game");
    }

    @Override
    public void removeObserved() {
    }
}
