package it.polimi.ingsw.client;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public abstract class UI {
    private static final Logger logger = LogManager.getLogger(UI.class);

    protected GameHandler gameHandler;
    protected LocalGame<?> localGame;
    protected Scanner input;
    protected boolean gameOver;

    public GameHandler getGameHandler() {
        return gameHandler;
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
        gameHandler.setLocalGame(localGame);
    }

    /**
     * set local game to new single player.
     * Override this method and execute its base functionalities before setting up the view
     */
    public void newSinglePlayer(){
        localGame = new LocalSingle();
        gameHandler.setLocalGame(localGame);
    }

    /**
     * set up the local server and both the server listener and a new game.
     * Unfortunately it is not considered valid for the exam to use this method.
     *
     * @return true if everything went fine, false otherwise (the server could not start for some reason)
     */
    @Deprecated
    protected boolean setUpLocalServer(){
        boolean valid = true;
        int port = LocalServer.getInstance().getPort();
        try {
            setServerListener("localhost", port);
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
    public void newMultiPlayer() {
        localGame = new LocalMulti();
        gameHandler.setLocalGame(localGame);
    }

    protected void setServerListener(String ip, int port) throws IOException {
        gameHandler = new ServerListener(ip, port);
        new Thread(gameHandler).start();
    }
}
