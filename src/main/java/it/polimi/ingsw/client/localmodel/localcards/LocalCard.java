package it.polimi.ingsw.client.localmodel.localcards;

import it.polimi.ingsw.client.localmodel.Observable;

public class LocalCard extends Observable {
    protected final int id;

    public int getId() {
        return id;
    }

    public LocalCard(int id) {
        this.id = id;
    }
}
