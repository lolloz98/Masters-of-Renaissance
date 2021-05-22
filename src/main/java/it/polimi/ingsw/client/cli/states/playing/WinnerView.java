package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;

public class WinnerView extends GameView {

    public WinnerView(CLI cli, LocalGame<?> localGame) {
        this.ui = cli;
        waiting = false;
        localGame.getError().addObserver(this);
        localGame.getLocalTurn().overrideObserver(this);
        localGame.overrideObserver(this);
    }

    @Override
    public void draw() {
        System.out.println("The game is over");
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
        } else {
            LocalSingle localSingle = (LocalSingle) localGame;
            if(localSingle.isMainPlayerWinner()){
                System.out.print("You won!");
            } else {
                System.out.print("You lost!");
            }
        }
        System.out.print(" ");
        System.out.print("You can still move through the game with 'sb', 'sm', 'sd'");
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localGame.getLocalTurn().removeObservers();
        localGame.removeObservers();
    }
}
