package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.util.ArrayList;

/**
 * CLI state to show a brief history of the last actions of all the players
 */
public class HistoryView extends GameView {

    public HistoryView(CLI cli){
        this.ui = cli;
        this.localGame = cli.getLocalGame();
        waiting = false;
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
    }

    @Override
    public void draw() {
        CLIutils.clearScreen();
        ArrayList<String> lastHistory;
        ArrayList<String> fullHistory = localGame.getLocalTurn().getHistoryObservable().getHistory();
        if (fullHistory.size() < 26) {
            lastHistory = new ArrayList<>(fullHistory);
        } else {
            lastHistory = new ArrayList<>(fullHistory.subList(fullHistory.size() - 26, fullHistory.size() - 1));
        }
        CLIutils.printBlock(lastHistory);
        System.out.println("");
        drawTurn();
    }
}
