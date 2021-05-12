package it.polimi.ingsw.client.localmodel.localcards;

public class LocalCard {
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
