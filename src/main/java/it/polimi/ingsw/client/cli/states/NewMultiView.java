package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class NewMultiView extends View{
    private LocalMulti localMulti;
    private CLI cli;

    public NewMultiView(CLI cli, LocalMulti localMulti, int numberOfPlayers){
        this.cli = cli;
        this.localMulti = localMulti;
        localMulti.addObserver(this);
        localMulti.getError().addObserver(this);
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
        try {
            cli.getClient().sendMessage(new CreateGameMessage(numberOfPlayers, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    public void draw(){
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

    public void notifyAction(){
        if(localMulti.getState() == LocalGameState.READY){
            // todo change cli state
        }
        else draw();
    }

    public void handleCommand(int ans){
        // todo quit command
    }
}
