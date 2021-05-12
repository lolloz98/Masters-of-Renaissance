package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class JoinGameView extends View {
    private LocalMulti localMulti;
    private CLI cli;
    String nickname;
    Scanner input;

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
    public void notifyAction() {
        if (localMulti.getError().getErrorId() == 0) {
            if (localMulti.getState() == LocalGameState.READY) {
                // todo change cli state
            }
        } else {
            System.out.println("Game not available in this server, enter another id:\n");
            int id = input.nextInt();
            try {
                cli.getClient().sendMessage(new JoinGameMessage(id, nickname));
            } catch (IOException e) {
                System.out.println("no connection from server"); // fixme
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleCommand(int ans) {
        // todo quit command
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