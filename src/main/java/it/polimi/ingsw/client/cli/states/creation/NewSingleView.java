package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalSingle;

/**
 * CLI state for joining a game
 */
public class NewSingleView extends View<CLI> {
    private final LocalSingle localSingle;

    public NewSingleView(CLI cli){
        this.ui = cli;
        this.localSingle = (LocalSingle) cli.getLocalGame();
        localSingle.overrideObserver(this);
        localSingle.getError().addObserver(this);
    }

    @Override
    public synchronized void draw(){
        System.out.println("Please wait");
    }

    @Override
    public synchronized void notifyUpdate(){
        if(localSingle.getState() == LocalGameState.PREP_LEADERS){
            localSingle.removeObservers();
            localSingle.getError().removeObserver();
            ui.setState(new BoardView(ui, localSingle.getMainPlayer()));
            ui.getState().draw();
        }
        else draw();
    }

    @Override
    public synchronized void notifyError() {
    }

    @Override
    public synchronized void handleCommand(String ans){
    }
}
