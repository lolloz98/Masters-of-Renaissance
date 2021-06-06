package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.Observer;

public abstract class View<T extends UI> implements Observer {
    protected boolean waiting;
    protected T ui;

    public abstract void notifyUpdate();

    public abstract void notifyError();

    public abstract void handleCommand(String ans);

    public abstract void draw();

    public void quit() {
        if (ui.getGameHandler() instanceof ServerListener)
            ((ServerListener) ui.getGameHandler()).closeConnection();
        ui.getGameHandler().removeObservers();
        ui.setQuit(true);
    }
}
