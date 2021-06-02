package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;

import java.util.ArrayList;

public class DevelopSlotPrinter {
    public static ArrayList<String> toStringBlock(ArrayList<LocalDevelopCard> slot) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < 14; i++) out.add("");
        if (slot.size() == 0) {
            CLIutils.append(out, 0, "                 ");
            CLIutils.append(out, 1, "                 ");
            CLIutils.append(out, 2, "                 ");
            CLIutils.append(out, 3, "                 ");
            CLIutils.append(out, 4, "                 ");
            CLIutils.append(out, 5, "   Empty slot    ");
            CLIutils.append(out, 6, "                 ");
            CLIutils.append(out, 7, "                 ");
            CLIutils.append(out, 8, "                 ");
            CLIutils.append(out, 9, "                 ");
            CLIutils.append(out, 10, "                 ");
            CLIutils.append(out, 11, "                 ");
            CLIutils.append(out, 12, "                 ");
            CLIutils.append(out, 13, "                 ");
        } else{
            if (slot.size() == 1) {
                CLIutils.append(out, 0, "                 ");
                CLIutils.append(out, 1, "                 ");
                CLIutils.append(out, 2, "                 ");
                CLIutils.append(out, 3, "                 ");
                CLIutils.append(out, 4, DevelopmentCardPrinter.toStringBlock(slot.get(0)));
            } else if (slot.size() == 2) {
                CLIutils.append(out, 0, "                 ");
                CLIutils.append(out, 1, "                 ");
                ArrayList<String> card = DevelopmentCardPrinter.toStringBlock(slot.get(0));
                CLIutils.append(out, 2, card.get(0));
                CLIutils.append(out, 3, card.get(1));
                CLIutils.append(out, 4, DevelopmentCardPrinter.toStringBlock(slot.get(1)));
            } else if (slot.size() == 3) {
                ArrayList<String> card = DevelopmentCardPrinter.toStringBlock(slot.get(0));
                CLIutils.append(out, 0, card.get(0));
                CLIutils.append(out, 1, card.get(1));
                ArrayList<String> card2 = DevelopmentCardPrinter.toStringBlock(slot.get(1));
                CLIutils.append(out, 2, card2.get(0));
                CLIutils.append(out, 3, card2.get(1));
                CLIutils.append(out, 4, DevelopmentCardPrinter.toStringBlock(slot.get(2)));
            }
            CLIutils.append(out, 12, " to flush: ");
            if(slot.get(slot.size()-1).getProduction().getResToFlush().size() == 0){
                CLIutils.append(out, 12, "      ");
            } else {
                CLIutils.append(out, 12, DevelopmentPrinter.toStringBlock((slot.get(slot.size()-1).getProduction().getResToFlush())));
            }
            CLIutils.append(out, 13, " total vps:  ");
            int sum = 0;
            for (LocalDevelopCard c : slot)
                sum = sum + c.getVictoryPoints();
            if (sum >=10){
                CLIutils.append(out, 13, sum + "  ");
            } else {
                CLIutils.append(out, 13, sum + "   ");
            }
        }

        return out;
    }
}
