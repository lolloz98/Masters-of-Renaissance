package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.preparation.PrepLeaderView;
import it.polimi.ingsw.client.cli.states.preparation.PrepResFirstView;
import it.polimi.ingsw.client.cli.states.preparation.PrepResFourthView;
import it.polimi.ingsw.client.cli.states.preparation.PrepResSecondView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class NewMultiView extends View<CLI> {
    private final LocalMulti localMulti;

    public NewMultiView(CLI cli, LocalMulti localMulti, int numberOfPlayers){
        this.ui = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
        localMulti.getError().addObserver(this);
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
        try {
            cli.getServerListener().sendMessage(new CreateGameMessage(numberOfPlayers, nickname));
        } catch (IOException e) {
            System.out.println("No connection from server");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void draw(){
        if(localMulti.getState() == LocalGameState.NEW){
            System.out.println("Please wait");
        }
        else if(localMulti.getState() == LocalGameState.WAITINGPLAYERS){
            System.out.println("The id of the game is\n" + localMulti.getGameId());
            System.out.println("Players currently connected:");
            for (LocalPlayer p : localMulti.getLocalPlayers()) {
                System.out.println(p.getName());
            }
        }
    }

    @Override
    public synchronized void notifyUpdate(){
        if(localMulti.getState() == LocalGameState.PREP_LEADERS){
            localMulti.removeObserver();
            localMulti.getError().removeObserver();
            ui.setState(new PrepLeaderView(ui, localMulti));
            ui.getState().draw();
        }
        else draw();
    }

    @Override
    public synchronized void notifyError() {
        // there is no error associated with the new game
    }

    @Override
    public synchronized void handleCommand(String ans){
    }
}
