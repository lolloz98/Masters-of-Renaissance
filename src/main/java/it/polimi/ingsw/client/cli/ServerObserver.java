package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.states.RejoinView;
import it.polimi.ingsw.client.gui.controllergui.BuildGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;

public class ServerObserver implements Observer{
    private CLI cli;

    public ServerObserver(CLI cli){
        this.cli = cli;
    }

    @Override
    public void notifyUpdate() {
        cli.getLocalGame().removeAllObservers();
        cli.setState(new RejoinView(cli));
        cli.getState().draw();
    }

    @Override
    public void notifyError() {
        // nothing to do here
    }
}
