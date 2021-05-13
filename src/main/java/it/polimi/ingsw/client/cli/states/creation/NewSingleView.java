package it.polimi.ingsw.client.cli.states.creation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.requests.CreateGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class NewSingleView extends View {
    private CLI cli;
    private LocalSingle localSingle;

    public NewSingleView(CLI cli, LocalSingle localSingle){
        this.cli = cli;
        this.localSingle = localSingle;
        localSingle.addObserver(this);
        localSingle.getError().addObserver(this);
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname:\n");
        String nickname = input.nextLine(); // todo: check characters limit
        try {
            cli.getClient().sendMessage(new CreateGameMessage(1, nickname));
        } catch (IOException e) {
            System.out.println("no connection from server"); // fixme
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void draw(){
        System.out.println("Please wait");
    }

    @Override
    public synchronized void notifyUpdate(){
        if(localSingle.getState() == LocalGameState.READY){
            localSingle.addObserver(this);
            localSingle.getError().addObserver(this);
            cli.setState(new BoardView(cli, localSingle, localSingle.getMainPlayer()));
            localSingle.removeObserver();
            localSingle.getError().removeObserver();
            cli.getState().draw();
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
