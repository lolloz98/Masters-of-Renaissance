package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DevelopmentGridPrinter {
    public static ArrayList<String> toStringBlock(LocalDevelopmentGrid localDevelopmentGrid) {
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
        CLIutils.append(out, 1, LegendPrinter.toStringBlock());
        return out;
    }

    private static void appendCard(ArrayList<String> out, int x, int y, int developCardNumber, LocalDevelopCard topDevelopCard) {
        ArrayList<String> cardBlock = new ArrayList<>();
        if (developCardNumber == 0) {
            for (int i = 0; i < 8; i++) cardBlock.add("                 ");
            for (int i = 0; i < 3; i++) {
                CLIutils.append(cardBlock, 0, " ");
                CLIutils.append(cardBlock, 1, " ");
                CLIutils.append(cardBlock, 2, " ");
                CLIutils.append(cardBlock, 3, " ");
                CLIutils.append(cardBlock, 4, " ");
                CLIutils.append(cardBlock, 5, " ");
                CLIutils.append(cardBlock, 6, " ");
                CLIutils.append(cardBlock, 7, " ");
            }
        } else {
            cardBlock = DevelopmentCardPrinter.toStringBlock(topDevelopCard);
            for (int i = 0; i < 3; i++) {
                if (i < developCardNumber - 1) {
                    CLIutils.append(cardBlock, 0, "┓");
                    CLIutils.append(cardBlock, 1, "┃");
                    CLIutils.append(cardBlock, 2, "┃");
                    CLIutils.append(cardBlock, 3, "┃");
                    CLIutils.append(cardBlock, 4, "┃");
                    CLIutils.append(cardBlock, 5, "┃");
                    CLIutils.append(cardBlock, 6, "┃");
                    CLIutils.append(cardBlock, 7, "┛");
                } else {
                    CLIutils.append(cardBlock, 0, " ");
                    CLIutils.append(cardBlock, 1, " ");
                    CLIutils.append(cardBlock, 2, " ");
                    CLIutils.append(cardBlock, 3, " ");
                    CLIutils.append(cardBlock, 4, " ");
                    CLIutils.append(cardBlock, 5, " ");
                    CLIutils.append(cardBlock, 6, " ");
                    CLIutils.append(cardBlock, 7, " ");
                }
            }
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
    }
}
