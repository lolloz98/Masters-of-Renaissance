package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.ServerListener;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.ServerObserver;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.RejoinMessage;

import java.io.IOException;
import java.net.ConnectException;

public class RejoinView extends View<CLI> {

    public RejoinView(CLI cli) {
        this.ui = cli;
        ui.getLocalGame().overrideObserver(this);
        ui.getLocalGame().getError().addObserver(this);
        System.out.println("Connection with server dropped!");
    }

    @Override
    public void notifyUpdate() {
        ui.getLocalGame().removeAllObservers();
        if (ui.getLocalGame() == null || ui.getLocalGame().getState() == LocalGameState.WAITING_PLAYERS || ui.getLocalGame().getState() == LocalGameState.OVER) {
            // nothing to do, please close
            // todo end application
        } else {
            ui.getLocalGame().removeObservers();
            ui.getLocalGame().getError().removeObserver();
            ui.setState(new BoardView(ui, ui.getLocalGame(), ui.getLocalGame().getMainPlayer()));
            ui.getState().draw();
        }
    }

    @Override
    public void notifyError() {
        System.out.println(ui.getLocalGame().getError().getErrorMessage());
        waiting = false;
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
                // nothing to do, please close
                // todo end application
            } else {
                System.out.println("Type 'rejoin' to retry to connect to the server or 'quit' to exit");
            }
        }
    }

    private void quit() {
        // todo end application
    }

    private void rejoin() {
        ServerListener old = (ServerListener) ui.getGameHandler();
        try {
            ui.setGameHandler(new ServerListener(old.getAddress(), old.getPort()), new ServerObserver(ui));
            ui.getGameHandler().setLocalGame(ui.getLocalGame());
            ui.getGameHandler().dealWithMessage(new RejoinMessage(ui.getLocalGame().getGameId(), ui.getLocalGame().getMainPlayer().getId()));
            waiting = true;
        } catch (ConnectException e){
            System.out.println("The server is still offline.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}