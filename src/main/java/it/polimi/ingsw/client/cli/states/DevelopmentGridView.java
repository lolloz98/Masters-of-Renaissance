package it.polimi.ingsw.client.cli.states;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.server.model.game.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DevelopmentGridView extends GameView {
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
        out = new ArrayList<>();
        for(int i = 0; i<29; i++) out.add(new String());
        topDevelopCards = localDevelopmentGrid.getTopDevelopCards();
        developCardsNumber = localDevelopmentGrid.getDevelopCardsNumber();
        buildFrame();
        for(int y=0; y<3; y++){
            for(int x=0; x<4; x++) {
                appendCard(x ,y);
            }
        }
        super.drawTurn();
        CLI.clearScreen();
        CLI.print(out);
    }

    @Override
    public void notifyAction() {
        draw();
    }

    @Override
    public void handleCommand(int ans){
        switch (ans){
            // todo handle buy command
            default:
                System.out.println("not valid");
        }
    }

    private void appendCard(int x, int y){
        ArrayList<String> cardBlock = new ArrayList<>();
        for(int i = 0; i<9; i++) cardBlock.add(new String());
        // first row
        CLIutils.append(cardBlock, 0 ,"┏━━━━━━━━━━━━━━━┓");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 0 ,"┓");
            else CLIutils.append(cardBlock, 0 ," ");
        }
        // second row
        CLIutils.append(cardBlock, 1 ,"┃ lvl:" + topDevelopCards[x][y].getLevel());
        CLIutils.append(cardBlock, 1 ," color:" + CLIutils.colorToAnsi(topDevelopCards[x][y].getColor()) +"█" + CLIutils.ANSI_WHITE + " ┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 1 ,"┃");
            else CLIutils.append(cardBlock, 1 ," ");
        }
        // third row
        CLIutils.append(cardBlock, 2 ,"┃  cost:");
        int size = topDevelopCards[x][y].getCost().size();
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_BLACK);
        for(int i = 0; i < size; i++){
            Map.Entry<Resource, Integer> entry = topDevelopCards[x][y].getCost().pollFirstEntry();
            CLIutils.append(cardBlock, 2 ,  " " +CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND);
        }
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_WHITE);
        CLIutils.appendSpaces(cardBlock, 2, -2*size+8);
        CLIutils.append(cardBlock, 2, "┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 2 ,"┃");
            else CLIutils.append(cardBlock, 2 ," ");
        }
        // fourth row
        CLIutils.append(cardBlock, 3 ,"┃               ┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 3 ,"┃");
            else CLIutils.append(cardBlock, 3 ," ");
        }
        // fifth row
        TreeMap<Resource, Integer> toGain = topDevelopCards[x][y].getResToGain();
        TreeMap<Resource, Integer> toGive = topDevelopCards[x][y].getResToGive();
        CLIutils.append(cardBlock, 4, "┃ ");
        CLIutils.appendSpaces(cardBlock, 4, -2*size+6);
        size = toGive.size();
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_BLACK);
        for(int i = 0; i < size; i++){
            Map.Entry<Resource, Integer> entry = toGive.pollFirstEntry();
            CLIutils.append(cardBlock, 4 ,  CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_WHITE);
        CLIutils.append(cardBlock, 4 ,  "} ");
        size = toGain.size();
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_BLACK);
        for(int i = 0; i < size; i++){
            Map.Entry<Resource, Integer> entry = toGain.pollFirstEntry();
            CLIutils.append(cardBlock, 4 ,  CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.appendSpaces(cardBlock, 4, -2*size+6);
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_WHITE);
        CLIutils.append(cardBlock, 4, "┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 4 ,"┃");
            else CLIutils.append(cardBlock, 4 ," ");
        }
        // sixth row
        CLIutils.append(cardBlock, 5 ,"┃               ┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 5 ,"┃");
            else CLIutils.append(cardBlock, 5 ," ");
        }
        // seventh row
        CLIutils.append(cardBlock, 6 ,"┃ victory pts:" + topDevelopCards[x][y].getVictoryPoints() + " ┃");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 6 ,"┃");
            else CLIutils.append(cardBlock, 6 ," ");
        }
        // eight row
        CLIutils.append(cardBlock, 7 ,"┗━━━━━━━━━━━━━━━┛");
        for(int i = 0; i < 3; i++){
            if (i<developCardsNumber[x][y]-1) CLIutils.append(cardBlock, 7 ,"┛");
            else CLIutils.append(cardBlock, 7 ," ");
        }
        CLIutils.append(out, 1 + y * 8, cardBlock);
    }

    private void buildFrame() {
        out.set(0, "           A                   B                   C                   D");
        out.set(1, "  ");
        out.set(2, "  ");
        out.set(3, "  ");
        out.set(4, "  ");
        out.set(5, "1 ");
        out.set(6, "  ");
        out.set(7, "  ");
        out.set(8, "  ");
        out.set(9, "  ");
        out.set(10, "  ");
        out.set(11, "  ");
        out.set(12, "  ");
        out.set(13, "2 ");
        out.set(14, "  ");
        out.set(15, "  ");
        out.set(16, "  ");
        out.set(17, "  ");
        out.set(18, "  ");
        out.set(19, "  ");
        out.set(20, "  ");
        out.set(21, "3 ");
        out.set(22, "  ");
        out.set(23, "  ");
        out.set(24, "  ");
        out.set(25, "  ");
        out.set(26, "  ");
        out.set(27, "  ");
        out.set(28, "  ");
    }
}
