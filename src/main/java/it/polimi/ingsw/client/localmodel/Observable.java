package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.cli.Observer;

import java.io.Serializable;

public class Observable implements Serializable  {
    protected Observer observer = null;

    public void addObserver(Observer observer){
        this.observer = observer;
    }

    public void removeObserver(){
        this.observer = null;
    }

    public void notifyObserver(){
        if (observer != null) observer.notifyUpdate();
    }
}
