package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.Observer;

public abstract class View implements Observer {
    protected CLI cli;

    public abstract void notifyUpdate();
    public abstract void notifyError();
    public abstract void handleCommand(String ans);
    public abstract void draw();
}
