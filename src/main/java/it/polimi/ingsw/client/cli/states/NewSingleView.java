package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class NewSingleView extends View{
    private CLI cli;
    private LocalSingle localSingle;

    public NewSingleView(CLI cli, LocalSingle localSingle){
        this.cli = cli;
        this.localSingle = localSingle;
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
        localSingle.addObserver(this);
        // todo ask nickname
        try {
            cli.getClient().sendMessage(new CreateGameMessage(1, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    public void draw(){
        System.out.println("Please wait");
    }

    public void notifyAction(){

    }

    public void handleCommand(int ans){
        // todo quit command
    }
}
