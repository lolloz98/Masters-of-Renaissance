package it.polimi.ingsw.client.cli;

public interface Observer {
    void notifyUpdate();
    void notifyError();
}
