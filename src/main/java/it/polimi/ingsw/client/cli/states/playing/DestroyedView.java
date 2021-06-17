package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;

/**
 * CLI state that informs the player that the game has been destroyed
 */
public class DestroyedView extends GameView {
    /**
     * true if the player comes to this state from a game view, false if he comes from one of the creation states
     */
    private final boolean fromGameView;

    public DestroyedView(CLI cli) {
        this.ui = cli;
        this.localGame = cli.getLocalGame();
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
