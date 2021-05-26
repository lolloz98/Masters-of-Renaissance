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
                CLIutils.append(out, 1, "              ");
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
                CLIutils.append(out, 1, "              ");
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




/*


        String line;
        out.add(localPlayer.getName() + "'s board:");
        out.add("Resources in depot:" + localPlayer.getLocalBoard().getResInNormalDepot());
        out.add("Resources in box:" + localPlayer.getLocalBoard().getResInStrongBox());
        if (localGame instanceof LocalSingle) {
            LocalSingle localSingle = (LocalSingle) localGame;
            out.add("Lorenzo's faith points:" + localSingle.getLorenzoTrack().getFaithTrackScore());
            out.add("Your faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
        } else
            out.add("Faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
        // base production
        out.add("Base production: res to give: " + localPlayer.getLocalBoard().getBaseProduction().getResToGive() +
                ", res to gain: " + localPlayer.getLocalBoard().getBaseProduction().getResToGain() +
                ", res to flush: " + localPlayer.getLocalBoard().getBaseProduction().getResToFlush());
        int i;
        for (i = 0; i < 3; i++) {
            if (localPlayer.getLocalBoard().getDevelopCards().get(i).size() == 0) {
                out.add("No cards in " + (i + 1) + "° slot");
            } else {
                LocalProduction localProduction = localPlayer.getLocalBoard().getDevelopCards().get(i).get(localPlayer.getLocalBoard().getDevelopCards().get(i).size() - 1).getProduction();
                out.add((i + 1) + "° production slot: res to give: "
                        + localProduction.getResToGive()
                        + ", res to gain: " + localProduction.getResToGain() +
                        ", res to flush: " + localProduction.getResToFlush());
                int sum = 0;
                for (LocalDevelopCard c : localPlayer.getLocalBoard().getDevelopCards().get(i))
                    sum = sum + c.getVictoryPoints();
                out.add(", total vp in this slot: " + sum + "\n");
            }
        }
        out.add("Leader cards:");
        int count = 1;
        for (LocalCard c : localPlayer.getLocalBoard().getLeaderCards()) {
            line = count + ". ";
            if (c instanceof LocalConcealedCard) {
                if (((LocalConcealedCard) c).isDiscarded()) line = line + ("This card has been discarded");
                else line = line + ("This card is not activated yet");
            } else {
                LocalLeaderCard localLeaderCard = (LocalLeaderCard) c;
                if (c instanceof LocalDiscountLeader) {
                    LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                    line = line + ("DiscountLeader" +
                            ", prod requirement: " + localDiscountLeader.getProdRequirement() +
                            ", discounted res: " + localDiscountLeader.getQuantityToDiscount() + " " + localDiscountLeader.getDiscountedRes());
                } else if (c instanceof LocalMarbleLeader) {
                    LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                    line = line + ("MarbleLeader" +
                            ", prod requirement: " + localMarbleLeader.getProdRequirement() +
                            ", marble: " + localMarbleLeader.getMarbleResource());
                } else if (c instanceof LocalDepotLeader) {
                    LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                    line = line + ("DepotLeader" +
                            ", requirement: " + localDepotLeader.getReqQuantity() + " " + localDepotLeader.getResRequirement() +
                            ", depot: " + localDepotLeader.getNumberOfRes() + " " + localDepotLeader.getResType());
                } else if (c instanceof LocalProductionLeader) {
                    LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                    line = line + ("ProductionLeader" +
                            ", prod requirement: " + localProductionLeader.getColorRequirement() + " at level " + localProductionLeader.getLevelReq() +
                            ", production: " + localProductionLeader.getProduction().getResToGive() + " -> " + localProductionLeader.getProduction().getResToGain() + ", res to flush: " + localProductionLeader.getProduction().getResToFlush());
                }
                if (localLeaderCard.isDiscarded()) {
                    line = line + ", this card is discarded";
                } else if (localLeaderCard.isActive()) {
                    line = line + (", this card is active");
                } else {
                    line = line + (", this card is not active");
                }
                out.add(line);
                count++;
            }
        }*/
        return out;
    }
}
