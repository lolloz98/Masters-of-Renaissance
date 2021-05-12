package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class JoinGameView extends View{
    private LocalMulti localMulti;
    private CLI cli;

    public JoinGameView(CLI cli, LocalMulti localMulti){
        this.cli = cli;
        this.localMulti = localMulti;
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
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

    @Override
    public void handleCommand(int ans) {

    }

    @Override
    public void draw() {

    }
}
