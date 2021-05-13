package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.ErrorType;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JoinGameView extends View {
    private LocalMulti localMulti;
    private CLI cli;
    private String nickname;
    private Scanner input;

    public JoinGameView(CLI cli, LocalMulti localMulti) {
        this.cli = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
        localMulti.getError().addObserver(this);
        this.input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        this.nickname = input.nextLine(); // todo: check characters limit
        System.out.println("Enter the game id:\n");
        int id = input.nextInt();
        try {
            cli.getClient().sendMessage(new JoinGameMessage(id, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    @Override
    public void notifyUpdate() { // todo as in newgameview
        if (localMulti.getState() == LocalGameState.PREP_RESOURCES) {
            ArrayList<LocalPlayer> localPlayers = localMulti.getLocalPlayers();
            LocalPlayer mainPlayer = null;
            for (LocalPlayer p : localPlayers) {
                if (p.getId() == localMulti.getMainPlayerId()) mainPlayer = p;
            }
            if (mainPlayer == null) {
                System.out.println("There was an error creating the game");// fixme
            } else {
                localMulti.removeObserver();
                localMulti.getError().removeObserver();
                cli.setState(new BoardView(cli, localMulti, mainPlayer));
                cli.getState().draw();
            }
        }
    }

    @Override
    public void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        System.out.println("Insert another id:");// fixme
    }

    @Override
    public void handleCommand(String ans) {
        int id = input.nextInt();
        try {
            cli.getClient().sendMessage(new JoinGameMessage(id, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
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