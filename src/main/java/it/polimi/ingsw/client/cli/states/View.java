package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.cli.Observer;

public abstract class View<T extends UI> implements Observer {
    /**
     * Indicates if the player is waiting for a response from the server.
     * Gets set to true everytime a message is sent to the server and to false when the response is received
     */
    protected boolean waiting;
    protected T ui;

    /**
     * Takes the string inserted by the user and calls the correct function
     */
    public abstract void handleCommand(String ans);

    /**
     * Takes the string inserted by the user and calls the correct function
     */
    public abstract void draw();

    /**
     * Handles the quit command
     */
    public void quit() {
        if (ui.getGameHandler() instanceof ServerListener)
            ((ServerListener) ui.getGameHandler()).closeConnection();
        ui.getGameHandler().removeObservers();
        ui.setQuit(true);
    }
}
