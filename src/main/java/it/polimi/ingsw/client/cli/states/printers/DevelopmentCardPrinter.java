package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DevelopmentCardPrinter {

    public static ArrayList<String> toStringBlock(LocalDevelopCard localDevelopCard) {
        ArrayList<String> cardBlock = new ArrayList<>();
        String colorChar = "N";
        switch ((localDevelopCard.getColor())) {
            case BLUE:
                colorChar = "B";
                break;
            case GOLD:
                colorChar = "G";
                break;
            case GREEN:
                colorChar = "G";
                break;
            case PURPLE:
                colorChar = "P";
                break;
        }
        for (int i = 0; i < 9; i++) cardBlock.add("");
        // first row
        CLIutils.append(cardBlock, 0, "┏━━━━━━━━━━━━━━━┓");

        // second row
        CLIutils.append(cardBlock, 1, "┃ lvl:" + localDevelopCard.getLevel());
        CLIutils.append(cardBlock, 1, " color:" + CLIutils.colorToAnsi(localDevelopCard.getColor()) + colorChar + CLIutils.ANSI_WHITE + " ┃");

        // third row
        CLIutils.append(cardBlock, 2, "┃  cost:");
        int size = localDevelopCard.getCost().size();
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_BLACK);
        TreeMap<Resource, Integer> cost = new TreeMap<>(localDevelopCard.getCost());
        for (int i = 0; i < size; i++) {
            Map.Entry<Resource, Integer> entry = cost.pollFirstEntry();
            CLIutils.append(cardBlock, 2, " " + CLIutils.resourceToAnsi(entry.getKey()) + entry.getValue() + CLIutils.BLACK_BACKGROUND);
        }
        CLIutils.append(cardBlock, 2, CLIutils.ANSI_WHITE);
        CLIutils.appendSpaces(cardBlock, 2, -2 * size + 8);
        CLIutils.append(cardBlock, 2, "┃");

        // fourth row
        CLIutils.append(cardBlock, 3, "┃               ┃");

        // fifth row
        TreeMap<Resource, Integer> toGain = new TreeMap<>(localDevelopCard.getProduction().getResToGain());
        TreeMap<Resource, Integer> toGive = new TreeMap<>(localDevelopCard.getProduction().getResToGive());
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

        // sixth row
        CLIutils.append(cardBlock, 5, "┃               ┃");

        // seventh row
        if (localDevelopCard.getVictoryPoints() >= 10) {
            CLIutils.append(cardBlock, 6, "┃ victory pts:" + localDevelopCard.getVictoryPoints() + "┃");
        } else {
            CLIutils.append(cardBlock, 6, "┃ victory pts:" + localDevelopCard.getVictoryPoints() + " ┃");
        }

        // eight row
        CLIutils.append(cardBlock, 7, "┗━━━━━━━━━━━━━━━┛");

        return cardBlock;
    }
}
