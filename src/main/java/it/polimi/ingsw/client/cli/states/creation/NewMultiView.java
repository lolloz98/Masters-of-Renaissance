package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.DestroyedView;
import it.polimi.ingsw.client.localmodel.*;

/**
 * CLI state for joining a game
 */
public class NewMultiView extends View<CLI> {
    private final LocalMulti localMulti;

    public NewMultiView(CLI cli) {
        this.ui = cli;
        this.localMulti = (LocalMulti) cli.getLocalGame();
        localMulti.overrideObserver(this);
        localMulti.getError().addObserver(this);
    }

    @Override
    public synchronized void draw() {
        CLIutils.clearScreen();
        if (localMulti.getState() == LocalGameState.NEW) {
            System.out.println("Please wait");
        } else if (localMulti.getState() == LocalGameState.WAITING_PLAYERS) {
            System.out.println("The id of the game is: " + localMulti.getGameId());
            System.out.println(" ");
            System.out.println("Players currently connected:");
            for (LocalPlayer p : localMulti.getLocalPlayers()) {
                System.out.println(p.getName());
            }
        }
    }

    @Override
    public synchronized void notifyUpdate() {
        if (localMulti.getState() == LocalGameState.PREP_LEADERS) {
            localMulti.removeObservers();
            localMulti.getError().removeObserver();
            ui.setState(new BoardView(ui, localMulti.getMainPlayer()));
            ui.getState().draw();
        } else if (localMulti.getState() == LocalGameState.DESTROYED) {
            localMulti.removeAllObservers();
            ui.setState(new DestroyedView(ui, localMulti, false));
            ui.getState().draw();
        } else draw();
    }

    @Override
    public synchronized void notifyError() {
    }

    @Override
    public synchronized void handleCommand(String ans) {
    }
}
