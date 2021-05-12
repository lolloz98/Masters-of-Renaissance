package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.Observable;

public class LocalCard extends Observable {
    protected int id;

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public LocalCard(int id) {
        this.id = id;
    }
}
