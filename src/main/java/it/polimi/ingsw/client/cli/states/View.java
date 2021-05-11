package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.Observer;

public abstract class View implements Observer {
    public abstract void notifyAction();
    public abstract void handleCommand(int ans);
    public abstract void draw();
}
