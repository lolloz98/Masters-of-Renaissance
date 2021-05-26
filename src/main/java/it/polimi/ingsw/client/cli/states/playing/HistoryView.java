package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.util.ArrayList;

public class HistoryView extends GameView {

    public HistoryView(CLI cli, LocalGame<?> localGame){
        this.ui = cli;
        this.localGame = localGame;
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
        if (fullHistory.size() < 29){
            lastHistory = new ArrayList<>(fullHistory);
        } else {
            lastHistory = new ArrayList<>(fullHistory.subList(fullHistory.size() - 29, fullHistory.size() - 1));
        }
        CLIutils.printBlock(lastHistory);
        System.out.println("");
        drawTurn();
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localGame.getLocalTurn().removeObservers();
        localGame.removeObservers();
    }
}
