package it.polimi.ingsw.client.localmodel;

public class LocalPlayer extends Observable{
    private LocalBoard localBoard;
    private int id;
    private String name;
    private boolean connected;

    public synchronized boolean isConnected() {
        return connected;
    }

    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
        notifyObserver();
    }

    public LocalBoard getLocalBoard() {
        return localBoard;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public LocalPlayer(){
        localBoard = new LocalBoard();
    }
}
