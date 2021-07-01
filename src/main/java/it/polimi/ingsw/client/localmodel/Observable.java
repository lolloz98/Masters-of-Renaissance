package it.polimi.ingsw.client.localmodel;

import it.polimi.ingsw.client.cli.Observer;

import java.io.Serializable;

public class Observable implements Serializable  {
    private static final long serialVersionUID = 23L;

    private Observer obs = null;

    /**
     * clear local list of observers and add observer
     *
     * @param observer to be added to the list
     */
    public void overrideObserver(Observer observer){
        obs = observer;
    }

    public void removeObservers(){
        obs = null;
    }

    public void notifyObservers(){
        if(obs != null)
            obs.notifyUpdate();
    }

    public Observer getObserver(){
        return obs;
    }
}
