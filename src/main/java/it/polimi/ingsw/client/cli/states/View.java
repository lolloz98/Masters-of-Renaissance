package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.localmodel.LocalGame;

import java.util.ArrayList;

public abstract class View<T extends UI> implements Observer {
    protected boolean waiting;
    protected T ui;

    public abstract void notifyUpdate();
    public abstract void notifyError();
    public abstract void handleCommand(String ans);
    public abstract void draw();
}
