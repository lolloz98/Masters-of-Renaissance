package it.polimi.ingsw.client.cli;

public interface Observer {

    /**
     * Gets called when the observer must be updated, and no error occurred
     */
    void notifyUpdate();

    /**
     * Gets called when the view must be updated, and an error occurred
     */
    void notifyError();
}
