package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class LeaderPrinter {

    public static ArrayList<String> toStringBlock(LocalCard localCard) {
        ArrayList<String> out = new ArrayList<>();
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        out.add("");
        if(localCard instanceof LocalConcealedCard)
            return localConcealedCardPrint();
        if(localCard instanceof LocalProductionLeader)
            return localProductionLeaderPrint((LocalProductionLeader) localCard);
        if(localCard instanceof LocalDepotLeader)
            return localDepotLeaderPrint((LocalDepotLeader) localCard);
        if(localCard instanceof LocalMarbleLeader)
            return localMarbleLeaderPrint((LocalMarbleLeader) localCard);
        if(localCard instanceof LocalDiscountLeader)
            return localDiscountLeaderPrint((LocalDiscountLeader) localCard);
        return out;
    }

    private static ArrayList<String> localDiscountLeaderPrint(LocalDiscountLeader localCard) {
        ArrayList<String> out = new ArrayList<>();
        TreeMap<Color, Integer> req = new TreeMap<Color, Integer>(localCard.getProdRequirement());
        out.add("┏━━━━━━━━━━━━━━━┓");
        out.add("┃requirement");
        for(Color c : req.keySet()) {
            CLIutils.append(out, 1, " " + CLIutils.colorToAnsi(c) + req.get(c) + CLIutils.ANSI_WHITE );
        }
        CLIutils.append(out, 1, "┃");
        out.add("┃               ┃");
        out.add("┃  discount: -");
        CLIutils.append(out, 3, CLIutils.resourceToAnsi(localCard.getDiscountedRes())+"1"+CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE+" ┃");
        out.add("┃               ┃");
        out.add("┃ victory pts:");
        if(localCard.getVictoryPoints()<10){
            CLIutils.append(out, 5, localCard.getVictoryPoints() + " ┃");
        } else {
            CLIutils.append(out, 5, localCard.getVictoryPoints() + "┃");
        }
        out.add("┗━━━━━━━━━━━━━━━┛");
        return out;
    }

    private static ArrayList<String> localMarbleLeaderPrint(LocalMarbleLeader localCard) {
        ArrayList<String> out = new ArrayList<>();
        TreeMap<Color, Integer> req = new TreeMap<Color, Integer>(localCard.getProdRequirement());
        out.add("┏━━━━━━━━━━━━━━━┓");
        out.add("┃requirement");
        for(Color c : req.keySet()) {
            CLIutils.append(out, 1, " " + CLIutils.colorToAnsi(c) + req.get(c) + CLIutils.ANSI_WHITE );
        }
        CLIutils.append(out, 1, "┃");
        out.add("┃               ┃");
        out.add("┃  substitute   ┃");
        out.add("┃    "+CLIutils.resourceToAnsi(Resource.ANYTHING)+"1"+CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE+" -> ");
        CLIutils.append(out, 4, CLIutils.resourceToAnsi(localCard.getMarbleResource())+"1"+CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE+"     ┃");
        out.add("┃ victory pts:");
        if(localCard.getVictoryPoints()<10){
            CLIutils.append(out, 5, localCard.getVictoryPoints() + " ┃");
        } else {
            CLIutils.append(out, 5, localCard.getVictoryPoints() + "┃");
        }
        out.add("┗━━━━━━━━━━━━━━━┛");
        return out;
    }

    private static ArrayList<String> localDepotLeaderPrint(LocalDepotLeader localCard) {
        ArrayList<String> out = new ArrayList<>();
        out.add("┏━━━━━━━━━━━━━━━┓");
        out.add("┃ requirement ");
        CLIutils.append(out, 1, CLIutils.resourceToAnsi(localCard.getResRequirement()) + localCard.getReqQuantity() + CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + " ┃");
        out.add("┃               ┃");
        out.add("┃  deposit: ");
        CLIutils.append(out, 3, CLIutils.resourceToAnsi(localCard.getResType()) + "2" + CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + "   ┃");
        out.add("┃ current res: ");
        CLIutils.append(out, 4, CLIutils.resourceToAnsi(localCard.getResType()) + localCard.getNumberOfRes() + CLIutils.BLACK_BACKGROUND + CLIutils.ANSI_WHITE + "┃");
        out.add("┃ victory pts:");
        if(localCard.getVictoryPoints()<10){
            CLIutils.append(out, 5, localCard.getVictoryPoints() + " ┃");
        } else {
            CLIutils.append(out, 5, localCard.getVictoryPoints() + "┃");
        }
        out.add("┗━━━━━━━━━━━━━━━┛");
        return out;
    }

    private static ArrayList<String> localProductionLeaderPrint(LocalProductionLeader localCard) {
        ArrayList<String> out = new ArrayList<>();
        out.add("┏━━━━━━━━━━━━━━━┓");
        out.add("┃ requirement ");
        CLIutils.append(out, 1, CLIutils.colorToAnsi(localCard.getColorRequirement()) + "1" + CLIutils.ANSI_WHITE + " ┃");
        out.add("┃   at level 2  ┃");
        out.add("┃");
        CLIutils.append(out, 3, DevelopmentPrinter.toStringBlock(localCard.getProduction()) + " ┃");
        out.add("┃to flush:");
        CLIutils.append(out, 4, DevelopmentPrinter.toStringBlock(localCard.getProduction().getResToFlush()) + "┃");
        out.add("┃ victory pts:");
        if(localCard.getVictoryPoints()<10){
            CLIutils.append(out, 5, localCard.getVictoryPoints() + " ┃");
        } else {
            CLIutils.append(out, 5, localCard.getVictoryPoints() + "┃");
        }
        out.add("┗━━━━━━━━━━━━━━━┛");
        return out;
    }

    private static ArrayList<String> localConcealedCardPrint() {
        ArrayList<String> out = new ArrayList<>();
        out.add("┏━━━━━━━━━━━━━━━┓");
        out.add("┃▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┃");
        out.add("┃▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┃");
        out.add("┃▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┃");
        out.add("┃▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┃");
        out.add("┃▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┃");
        out.add("┗━━━━━━━━━━━━━━━┛");
        return out;
    }
}
