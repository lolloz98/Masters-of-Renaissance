package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGame;

public class DestroyedView extends GameView {
    private boolean fromGameView;

    public DestroyedView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        this.localGame = localGame;
        this.fromGameView = true;
    }

    public DestroyedView(CLI cli, LocalGame<?> localGame, boolean fromGameView) {
        this.ui = cli;
        this.localGame = localGame;
        this.fromGameView = fromGameView;
    }

    @Override
    public void draw() {
        CLIutils.clearScreen();
        System.out.println("The game ended because someone left it!");
        if(fromGameView)
            System.out.println("You can still move around with 'sd', 'sb', 'sm'. ");
        System.out.println("Type 'quit' exit the game");

    }

    @Override
    public void removeObserved() {
    }
}
