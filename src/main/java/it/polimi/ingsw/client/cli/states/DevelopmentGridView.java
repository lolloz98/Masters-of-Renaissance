package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalModelAbstract;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import java.util.ArrayList;

public class DevelopmentGridView extends View{
    private LocalDevelopmentGrid localDevelopmentGrid;
    private ArrayList<String> out;
    private LocalDevelopCard[][] topDevelopCards;
    private int[][] developCardsNumber;

    public DevelopmentGridView(LocalDevelopmentGrid localDevelopmentGrid, LocalGame localGame){
        this.localDevelopmentGrid = localDevelopmentGrid;
        this.localGame = localGame;
        draw();
    }

    @Override
    public void draw(){
        ArrayList<String> out = new ArrayList<>();
        CLI.clearScreen();
        topDevelopCards = localDevelopmentGrid.getTopDevelopCards();
        developCardsNumber = localDevelopmentGrid.getDevelopCardsNumber();
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓");
        super.drawTurn();
    }

    @Override
    public void notifyAction(LocalModelAbstract localModelAbstract) {
        if (localModelAbstract == this.localDevelopmentGrid || localModelAbstract instanceof LocalGame) {
            draw();
        }
    }

    @Override
    public void handleCommand(String line){
        switch (line){
            // todo handle buy command
            default:
                System.out.println("not valid");
        }
    }

    private void appendCard(int posx, int posy, int index){

    }
}
