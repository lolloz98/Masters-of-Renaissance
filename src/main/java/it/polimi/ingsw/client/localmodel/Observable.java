package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.cli.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Observable implements Serializable  {
    protected List<Observer> observers = new ArrayList<>();

    /**
     * clear local list of observers and add observer
     *
     * @param observer to be added to the list
     */
    public void overrideObserver(Observer observer){
        observers.clear();
        observers.add(observer);
    }

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void removeObservers(){
        observers.clear();
    }

    public void notifyObservers(){
        for(Observer o: observers){
            o.notifyUpdate();
        }
    }
}
