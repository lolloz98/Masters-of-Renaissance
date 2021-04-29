package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalModelAbstract;
import it.polimi.ingsw.model.cards.Color;

public class DevelopmentGridView extends View{
    private LocalDevelopmentGrid localDevelopmentGrid;

    public DevelopmentGridView(LocalDevelopmentGrid localDevelopmentGrid, LocalGame localGame){
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localGame = localGame;
    }

    @Override
    public void draw(){
        CLI.clearScreen();
        // todo make this good looking
        for(Color c : Color.values()){
            for(int lvl = 1; lvl<4; lvl++){
                System.out.print(localDevelopmentGrid.getDevelopDeck(c, lvl).topCard().getId() + ", ");
                System.out.print(localDevelopmentGrid.getDevelopDeck(c, lvl).howManyCards()-1 + " cards below    ");
            }
            System.out.print("\r\n");
        }
        System.out.println("");
        super.drawTurn();
    }

    @Override
    public void notifyAction(LocalModelAbstract localModelAbstract) {
        if (localModelAbstract == this.localDevelopmentGrid || localModelAbstract instanceof LocalGame) {
            draw();
        }
    }
}
