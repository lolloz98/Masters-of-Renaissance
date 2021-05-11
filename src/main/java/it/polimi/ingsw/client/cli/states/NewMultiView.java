package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class NewMultiView extends View{
    private LocalMulti localMulti;
    private CLI cli;

    public NewMultiView(CLI cli, LocalMulti localMulti, int numberOfPlayers){
        this.cli = cli;
        this.localMulti = localMulti;
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
        localMulti.addObserver(this);
        for (LocalPlayer p : localMulti.getLocalPlayers()){
            p.addObserver(this);
        }
        try {
            cli.getClient().sendMessage(new CreateGameMessage(numberOfPlayers, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    public void draw(){
        System.out.println("The id of the game is\n" + localMulti.getGameId());
        System.out.println("Players currently connected:");
        for (LocalPlayer p : localMulti.getLocalPlayers()) {
            if (p.isConnected()) System.out.println(p.getName());
        }
    }

    public void notifyAction(){
        if (localMulti.isReady()){
            localMulti.removeObserver();
            for (LocalPlayer p : localMulti.getLocalPlayers()){
                p.removeObserver();
            }
            // todo: change cli.setState();
        }
        else draw();
    }

    public void handleCommand(int ans){
        // todo quit command
    }
}
