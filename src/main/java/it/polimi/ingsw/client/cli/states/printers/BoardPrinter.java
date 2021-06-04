package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.cli.CLIutils;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;

import java.util.ArrayList;

public class BoardPrinter {

    public static ArrayList<String> toStringBlock(LocalGame<?> localGame, LocalPlayer localPlayer) {
        ArrayList<String> out = new ArrayList<>();
        out.add("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃                                                                               ");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┃");
        out.add("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        CLIutils.append(out, 1, FaithTrackPrinter.toStringBlock(localGame, localPlayer));
        CLIutils.append(out, 5, DepotPrinter.toStringBlock(localPlayer));
        CLIutils.append(out, 14, "                        ");
        CLIutils.append(out, 15, StrongBoxPrinter.toStringBlock(localPlayer));
        CLIutils.append(out, 5, "     Base production:");
        CLIutils.append(out, 5, DevelopmentPrinter.toStringBlock(localPlayer.getLocalBoard().getBaseProduction()));
        CLIutils.append(out, 5, "to flush: ");
        CLIutils.append(out, 5, DevelopmentPrinter.toStringBlock(localPlayer.getLocalBoard().getBaseProduction().getResToFlush()));
        CLIutils.append(out, 5, "    ");
        CLIutils.append(out, 6, "                                                       ");
        CLIutils.append(out, 7, DevelopSlotPrinter.toStringBlock(localPlayer.getLocalBoard().getDevelopCards().get(0)));
        for(int i = 0; i<14; i++){
            CLIutils.append(out, i+7, "  ");
        }
        CLIutils.append(out, 7, DevelopSlotPrinter.toStringBlock(localPlayer.getLocalBoard().getDevelopCards().get(1)));
        for(int i = 0; i<14; i++){
            CLIutils.append(out, i+7, "  ");
        }
        CLIutils.append(out, 7, DevelopSlotPrinter.toStringBlock(localPlayer.getLocalBoard().getDevelopCards().get(2)));
        CLIutils.append(out, 21, "                                                       ");
        for(int i = 1; i<22; i++){
            CLIutils.append(out, i, "┃");
        }
        if(localPlayer.getLocalBoard().getLeaderCards().size() == 4){
            CLIutils.append(out, 0, "1)               ");
            CLIutils.append(out, 1, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(0)));
            CLIutils.append(out, 0, "2)               ");
            CLIutils.append(out, 1, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(1)));
            CLIutils.append(out, 8, "3)               ");
            CLIutils.append(out, 9, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(2)));
            CLIutils.append(out, 8, "4)               ");
            CLIutils.append(out, 9, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(3)));
        } else if(localPlayer.getLocalBoard().getLeaderCards().size() == 2){
            CLIutils.append(out, 0, "1) ");
            if(localPlayer.getLocalBoard().getLeaderCards().get(0) instanceof LocalConcealedCard){
                CLIutils.append(out, 0, "              ");
            } else {
                LocalLeaderCard localLeaderCard = (LocalLeaderCard) localPlayer.getLocalBoard().getLeaderCards().get(0);
                if(localLeaderCard.isActive()){
                    CLIutils.append(out, 0, "activated     ");
                } else if (localLeaderCard.isDiscarded()){
                    CLIutils.append(out, 0, "discarded     ");
                } else {
                    CLIutils.append(out, 0, "not active    ");
                }
            }
            CLIutils.append(out, 1, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(0)));

            CLIutils.append(out, 0, "2) ");
            if(localPlayer.getLocalBoard().getLeaderCards().get(1) instanceof LocalConcealedCard){
                CLIutils.append(out, 0, "              ");
            } else {
                LocalLeaderCard localLeaderCard = (LocalLeaderCard) localPlayer.getLocalBoard().getLeaderCards().get(1);
                if(localLeaderCard.isActive()){
                    CLIutils.append(out, 0, "activated     ");
                } else if (localLeaderCard.isDiscarded()){
                    CLIutils.append(out, 0, "discarded     ");
                } else {
                    CLIutils.append(out, 0, "not active    ");
                }
            }
            CLIutils.append(out, 1, LeaderPrinter.toStringBlock(localPlayer.getLocalBoard().getLeaderCards().get(1)));
        }
        CLIutils.append(out, 16, LegendPrinter.toStringBlock());
        out.add(0, localPlayer.getName() + "'s board:");
        return out;
    }
}
