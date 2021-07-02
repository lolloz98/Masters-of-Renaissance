package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.Observer;
import it.polimi.ingsw.client.cli.ClosingConnectionListenerCLI;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.gui.ClosingConnectionListenerGUI;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.RejoinMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;

/**
 * CLI state for when the connection with the server drops
 */
public class RejoinView extends View<CLI> {
    private static final Logger logger = LogManager.getLogger(RejoinView.class);

    public RejoinView(CLI cli) {
        this.ui = cli;
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        CLIutils.clearScreen();
        System.out.println("Connection with server dropped!");
        System.out.println(" ");
        waiting = false;
    }

    @Override
    public void notifyUpdate() {
        ui.getLocalGame().removeAllObservers();
        if (ui.getLocalGame() == null || ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS || ui.getLocalGame().getState() == LocalGameState.OVER) {
            quit();
        } else {
            ui.getLocalGame().removeObservers();
            ui.getLocalGame().getError().removeObserver();
            ui.setState(new BoardView(ui, ui.getLocalGame().getMainPlayer()));
            ui.getState().draw();
        }
    }

    @Override
    public void notifyError() {
        System.out.println(ui.getLocalGame().getError().getErrorMessage());
        waiting = false;
        // todo if i recieve error i must print that the game is destroyed
    }

    @Override
    public void handleCommand(String s) {
        if (!waiting) {
            String ans = s.toUpperCase();
            switch (ans) {
                case "REJOIN":
                    rejoin();
                    break;
                case "QUIT":
                    quit();
                    break;
                default:
                    System.out.println("Invalid command, type 'rejoin' to retry to connect to the server or 'quit' to exit");
            }
        }
    }

    @Override
    public void draw() {
        if (waiting) {
            System.out.println("Please wait");
        } else {
            if (ui.getLocalGame() == null || ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS || ui.getLocalGame().getState() == LocalGameState.OVER) {
                System.out.println("Type 'quit' to go back to the main menu");
            } else {
                System.out.println("Type 'rejoin' to retry to connect to the server or 'quit' to exit");
            }
        }
    }

    /**
     * send the rejoin message to the server
     */
    private void rejoin() {
        waiting = true;
        ServerListener old = (ServerListener) ui.getGameHandler();
        new Thread(() -> {
            try {
                ui.setGameHandler(new ServerListener(old.getAddress(), old.getPort(), new Observer() {
                    @Override
                    public void notifyUpdate() {
                        try {
                            ui.getGameHandler().dealWithMessage(new RejoinMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId()));
                        } catch (IOException e) {
                            connectionFailed(e);
                        }
                    }

                    @Override
                    public void notifyError() {
                    }
                }), new ClosingConnectionListenerCLI(ui));
                ui.getGameHandler().setLocalGame(ui.getLocalGame());
            } catch (IOException e) {
                connectionFailed(e);
            }
        }).start();
    }

    private void connectionFailed(IOException e) {
        logger.error("error while connecting to the server: " + e);
        System.out.println("The server is still offline.");
        waiting = false;
    }

}