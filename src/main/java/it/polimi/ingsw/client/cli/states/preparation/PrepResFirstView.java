package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;

public class PrepResFirstView extends View<CLI> {
    private final LocalMulti localMulti;

    public PrepResFirstView(CLI cli, LocalMulti localMulti) {
        this.ui = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
    }

    @Override
    public void notifyUpdate() {
        if(localMulti.getState() == LocalGameState.READY){
            localMulti.removeObserver();
            ui.setState(new BoardView(ui, localMulti, localMulti.getMainPlayer()));
            ui.getState().draw();
        }
    }

    @Override
    public void notifyError() {

    }

    @Override
    public void handleCommand(String ans) {

    }

    @Override
    public void draw() {
        // todo inform player of the order
        System.out.println("Please wait");
    }
}
