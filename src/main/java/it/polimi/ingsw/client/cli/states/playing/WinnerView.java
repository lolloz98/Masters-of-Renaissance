package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.server.model.utility.PairId;

public class WinnerView extends GameView {

    public WinnerView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        this.localGame = localGame;
    }

    @Override
    public void draw() {
        CLIutils.clearScreen();
        System.out.println("The game is over.");
        System.out.println(" ");
        if(localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            if(localMulti.getWinners().size() == 1){
                System.out.println("The winner is "+localMulti.getWinners().get(0).getName());
            } else {
                System.out.print("It's a tie between ");
                for(LocalPlayer l : localMulti.getWinners()){
                    System.out.print(l.getName()+ " ");
                }
            }
            System.out.print(" ");
            System.out.print("Leader board: ");
            for(PairId<LocalPlayer, Integer> pair : localMulti.getLocalLeaderBoard()){
                System.out.print(pair.getSecond() + " " + pair.getFirst());
            }
        } else {
            LocalSingle localSingle = (LocalSingle) localGame;
            if(localSingle.isMainPlayerWinner()){
                System.out.print("You won!");
            } else {
                System.out.print("You lost!");
            }
        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("You can still move through the game with 'sb', 'sm', 'sd'");
        System.out.println(" ");
        System.out.println("Go back to the main screen by typing 'quit'");
    }

    @Override
    public void removeObserved() {
    }
}
