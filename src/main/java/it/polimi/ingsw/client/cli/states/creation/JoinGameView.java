package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.preparation.PrepResFirstView;
import it.polimi.ingsw.client.cli.states.preparation.PrepResFourthView;
import it.polimi.ingsw.client.cli.states.preparation.PrepResSecondView;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JoinGameView extends View<CLI> {
    private final LocalMulti localMulti;
    // private CLI cli; // todo remove
    private final String nickname;

    public JoinGameView(CLI cli, LocalMulti localMulti) {
        this.ui = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
        localMulti.getError().addObserver(this);
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        this.nickname = input.nextLine(); // todo: check characters limit
        System.out.println("Enter the game id:\n");
        int id = input.nextInt();
        try {
            cli.getServerListener().sendMessage(new JoinGameMessage(id, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void notifyUpdate() { // todo as in newgameview
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
                switch(localMulti.getMainPlayerPosition()){
                    case 0: ui.setState(new PrepResFirstView(ui, localMulti)); break;
                    case 1: ui.setState(new PrepResSecondView(ui, localMulti, localMulti.getMainPlayer().getLocalBoard())); break;
                    case 2: ui.setState(new PrepResSecondView(ui, localMulti, localMulti.getMainPlayer().getLocalBoard())); break;
                    case 3: ui.setState(new PrepResFourthView(ui, localMulti, localMulti.getMainPlayer().getLocalBoard())); break; // todo
                }
                ui.getState().draw();
            }
        }
        else draw();
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        System.out.println("Insert another id:");
    }

    @Override
    public synchronized void handleCommand(String ans) {
        try{
            int port = Integer.parseInt(ans);
            ui.getServerListener().sendMessage(new JoinGameMessage(port, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server");
            e.printStackTrace();
        }
        catch (NumberFormatException ex){
            ex.printStackTrace(); // todo ask again
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