package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DevelopmentGridPrinter {
    public static ArrayList<String> toStringBlock(LocalDevelopmentGrid localDevelopmentGrid){
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < 29; i++) out.add("");
        LocalDevelopCard[][] topDevelopCards = localDevelopmentGrid.getTopDevelopCards();
        int[][] developCardsNumber = localDevelopmentGrid.getDevelopCardsNumber();
        buildFrame(out);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                appendCard(out, x, y, developCardsNumber[x][y], topDevelopCards[x][y]);
            }
        }
        CLIutils.clearScreen();
        CLIutils.printBlock(out);
        return out;
    }

    private static void appendCard(ArrayList<String> out, int x, int y, int developCardNumber, LocalDevelopCard topDevelopCard) {
        ArrayList<String> cardBlock = new ArrayList<>();
        for (int i = 0; i < 9; i++) cardBlock.add("");
        // first row
        CLIutils.append(cardBlock, 0, "┏━━━━━━━━━━━━━━━┓");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 0, "┓");
            else CLIutils.append(cardBlock, 0, " ");
        }
        // second row
        CLIutils.append(cardBlock, 1, "┃ lvl:" + topDevelopCard.getLevel());
        CLIutils.append(cardBlock, 1, " color:" + CLIutils.colorToAnsi(topDevelopCard.getColor()) + "█" + CLIutils.ANSI_WHITE + " ┃");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 1, "┃");
            else CLIutils.append(cardBlock, 1, " ");
        }
        // third row
        CLIutils.append(cardBlock, 2, "┃  cost:");
        int size = topDevelopCard.getCost().size();
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_BLACK);
        TreeMap<Resource, Integer> cost = new TreeMap<>(topDevelopCard.getCost());
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = cost.pollFirstEntry();
            CLIutils.append(cardBlock, 2, " " + CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND);
        }
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_WHITE);
        CLIutils.appendSpaces(cardBlock, 2, -2 * size + 8);
        CLIutils.append(cardBlock, 2, "┃");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 2, "┃");
            else CLIutils.append(cardBlock, 2, " ");
        }
        // fourth row
        CLIutils.append(cardBlock, 3, "┃               ┃");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 3, "┃");
            else CLIutils.append(cardBlock, 3, " ");
        }
        // fifth row
        TreeMap<Resource, Integer> toGain = new TreeMap<>(topDevelopCard.getProduction().getResToGain());
        TreeMap<Resource, Integer> toGive =  new TreeMap<>(topDevelopCard.getProduction().getResToGive());
        CLIutils.append(cardBlock, 4, "┃ ");
        size = toGive.size();
        CLIutils.appendSpaces(cardBlock, 4, -2 * size + 6);
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_BLACK);
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = toGive.pollFirstEntry();
            CLIutils.append(cardBlock, 4, CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_WHITE);
        CLIutils.append(cardBlock, 4, "} ");
        size = toGain.size();
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_BLACK);
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = toGain.pollFirstEntry();
            CLIutils.append(cardBlock, 4, CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND + " ");
        }
        CLIutils.appendSpaces(cardBlock, 4, -2 * size + 6);
        CLIutils.append(cardBlock, 4, CLIutils.ANSI_WHITE);
        CLIutils.append(cardBlock, 4, "┃");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 4, "┃");
            else CLIutils.append(cardBlock, 4, " ");
        }
        // sixth row
        CLIutils.append(cardBlock, 5, "┃               ┃");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 5, "┃");
            else CLIutils.append(cardBlock, 5, " ");
        }
        // seventh row
        if(topDevelopCard.getVictoryPoints()>=10) {
            CLIutils.append(cardBlock, 6, "┃ victory pts:" + topDevelopCard.getVictoryPoints() + "┃");
        } else {
            CLIutils.append(cardBlock, 6, "┃ victory pts:" + topDevelopCard.getVictoryPoints() + " ┃");
        }
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 6, "┃");
            else CLIutils.append(cardBlock, 6, " ");
        }
        // eight row
        CLIutils.append(cardBlock, 7, "┗━━━━━━━━━━━━━━━┛");
        for (int i = 0; i < 3; i++) {
            if (i < developCardNumber - 1) CLIutils.append(cardBlock, 7, "┛");
            else CLIutils.append(cardBlock, 7, " ");
        }
        CLIutils.append(out, 1 + y * 8, cardBlock);
    }

    private static void buildFrame(ArrayList<String> out) {
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
