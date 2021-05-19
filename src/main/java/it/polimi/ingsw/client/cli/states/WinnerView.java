package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalGame;

public class WinnerView extends View<CLI>{
    private final LocalGame localGame;

    public WinnerView(CLI cli, LocalGame localGame) {
        this.ui = cli;
        this.localGame = localGame;
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void notifyError() {

    }

    @Override
    public void handleCommand(String ans) {

    }

    @Override
    public void draw() {

    }
}
