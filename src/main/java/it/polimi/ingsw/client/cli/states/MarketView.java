package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMarket;

public class MarketView extends GameView {
    private LocalMarket localMarket;
    private CLI cli;

    public MarketView(CLI cli, LocalGame localGame, LocalMarket localMarket){
        this.cli = cli;
        this.localMarket = localMarket;
        this.localGame = localGame;
    }

    @Override
    public void draw(){
        CLI.clearScreen();
        System.out.println("Market:");
        System.out.println("");
        System.out.println("Free marble: "+ localMarket.getFreeMarble());
        System.out.println("");
        for(int i = 0; i<3; i++){
            for(int j = 0; j<4; j++){
                System.out.print(localMarket.getMarbleMatrix()[i][j] + " ");
            }
            System.out.print("\r\n");
        }
        System.out.println("");
        super.drawTurn();
    }

    @Override
    public void notifyAction(){
        draw();
    }

    @Override
    public void handleCommand(int ans){
        switch (ans){
            // todo handle push command
            default:
                System.out.println("not valid");
        }
    }
}
