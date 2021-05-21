package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class JoinGameView extends View<CLI> {
    private final LocalMulti localMulti;
    private final String nickname;

    public JoinGameView(CLI cli, LocalMulti localMulti) {
        this.ui = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
        localMulti.getError().addObserver(this);
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        this.nickname = input.nextLine(); // todo: check characters limit
        boolean valid;
        do {
            System.out.println("Enter the game id:\n");
            String idString = input.nextLine();
            try {
                int idNumber = Integer.parseInt(idString);
                try {
                    cli.getGameHandler().dealWithMessage(new JoinGameMessage(idNumber, nickname));
                    valid = true;
                } catch (IOException e) {
                    System.out.println("no connection from server");
                    valid = false;
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid id, try again:");
                valid = false;
            }
        } while (!valid);
    }

    @Override
    public synchronized void notifyUpdate() {
        if (localMulti.getState() == LocalGameState.PREP_LEADERS) {
            localMulti.removeObserver();
            localMulti.getError().removeObserver();
            ui.setState(new BoardView(ui, localMulti, localMulti.getMainPlayer()));
            ui.getState().draw();
        } else draw();
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        System.out.println("Insert another id:");
    }

    @Override
    public synchronized void handleCommand(String ans) {
        try {
            int port = Integer.parseInt(ans);
            ui.getGameHandler().dealWithMessage(new JoinGameMessage(port, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server");
            e.printStackTrace();
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id, try again:");
        }
    }

    @Override
    public synchronized void draw() {
        if (localMulti.getState() == LocalGameState.NEW) {
            System.out.println("Please wait");
        } else if (localMulti.getState() == LocalGameState.WAITINGPLAYERS) {
            System.out.println("The id of the game is\n" + localMulti.getGameId());
            System.out.println("Players currently connected:");
            for (LocalPlayer p : localMulti.getLocalPlayers()) {
                System.out.println(p.getName());
            }
        }
    }
}