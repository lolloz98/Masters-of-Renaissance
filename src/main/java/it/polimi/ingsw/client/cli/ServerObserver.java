package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.states.RejoinView;

/**
 * observer class for serverListener
 * gets updated if the server drops the connection
 */
public class ServerObserver implements Observer{
    private final CLI cli;

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
    }
}
