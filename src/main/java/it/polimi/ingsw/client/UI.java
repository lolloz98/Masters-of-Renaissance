package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.creation.NewSingleView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public abstract class UI {
    private static final Logger logger = LogManager.getLogger(UI.class);

    protected ServerListener serverListener;
    protected LocalGame<?> localGame;
    protected Scanner input;
    protected boolean gameOver;


    public ServerListener getServerListener() {
        return serverListener;
    }

    public LocalGame<?> getLocalGame() {
        return localGame;
    }

    public void setLocalGame(LocalGame<?> localGame) {
        this.localGame = localGame;
    }

    /**
     * set local game to new multiPlayer.
     * Override this method and execute its base functionalities before setting up the view
     */
    protected void joinGame() {
        this.localGame = new LocalMulti();
        serverListener.setLocalGame(localGame);
    }

    /**
     * set local game to new single player.
     * Override this method and execute its base functionalities before setting up the view
     */
    protected void newSinglePlayer(){
        localGame = new LocalSingle();
        serverListener.setLocalGame(localGame);
    }

    protected abstract void choseNumberOfPlayers();

    /**
     * set up the local server and both the server listener and a new game.
     *
     * @return true if everything went fine, false otherwise (the server could not start for some reason)
     */
    protected boolean setUpLocalServer(){
        boolean valid = true;
        int port = LocalServer.getInstance().getPort();
        try {
            serverListener = new ServerListener("localhost", port);
            new Thread(serverListener).start();
            newSinglePlayer();
        }catch (IOException e){
            logger.error("error connecting to localhost, port: " + port);
            System.out.println("Error creating a new SinglePlayer locally");
            valid = false;
        }
        return valid;
    }
    /**
     * set local game to new multi player.
     * Override this method and execute its base functionalities before setting up the view
     */
    protected void newMultiPlayer(int numberOfPlayers) {
        localGame = new LocalMulti();
        serverListener.setLocalGame(localGame);
    }
}
