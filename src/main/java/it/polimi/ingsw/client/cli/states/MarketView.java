package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMarket;
import it.polimi.ingsw.client.localmodel.LocalModelAbstract;

public class MarketView extends View{
    private LocalMarket localMarket;

    public MarketView(LocalMarket localMarket, LocalGame localGame){
        this.localMarket = localMarket;
        this.localGame = localGame;
        draw();
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
    public void notifyAction(LocalModelAbstract localModelAbstract){
        if (localModelAbstract == this.localMarket || localModelAbstract instanceof LocalGame) {
            draw();
        }
    }

    @Override
    public void handleCommand(String line){
        switch (line){
            // todo handle push command
            default:
                System.out.println("not valid");
        }
    }
}
