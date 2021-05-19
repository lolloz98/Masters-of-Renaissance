package it.polimi.ingsw.client.cli.states.printers;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.client.localmodel.localcards.*;

import java.util.ArrayList;

public class BoardPrinter {

    public static ArrayList<String> toStringBlock(LocalGame localGame, LocalPlayer localPlayer) {
        ArrayList<String> out = new ArrayList<>();
        // todo make this good looking
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
        out.add("Base production: res to give: " + localPlayer.getLocalBoard().getBaseProduction().getResToGive());
        out.add(", res to gain: " + localPlayer.getLocalBoard().getBaseProduction().getResToGain());
        out.add(", res to flush: " + localPlayer.getLocalBoard().getBaseProduction().getResToFlush() + "\n");
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
        for (LocalCard c : localPlayer.getLocalBoard().getLeaderCards()) {
            if (c instanceof LocalConcealedCard) {
                out.add("This card is not activated yet");
            } else {
                LocalLeaderCard localLeaderCard = (LocalLeaderCard) c;
                if (c instanceof LocalDiscountLeader) {
                    LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                    out.add("DiscountLeader" +
                    ", prod requirement: " + localDiscountLeader.getProdRequirement() +
                    ", discounted res: " + localDiscountLeader.getQuantityToDiscount() + " " + localDiscountLeader.getDiscountedRes());
                } else if (c instanceof LocalMarbleLeader) {
                    LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                    out.add("MarbleLeader" +
                    ", prod requirement: " + localMarbleLeader.getProdRequirement() +
                    ", marble: " + localMarbleLeader.getMarbleResource());
                } else if (c instanceof LocalDepotLeader) {
                    LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                    out.add("DepotLeader" +
                    ", requirement: " + localDepotLeader.getReqQuantity() + " " + localDepotLeader.getResRequirement() +
                    ", depot: " + localDepotLeader.getNumberOfRes() + " " + localDepotLeader.getResType());
                } else if (c instanceof LocalProductionLeader) {
                    LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                    out.add("ProductionLeader" +
                    ", prod requirement: " + localProductionLeader.getColorRequirement() + " at level " + localProductionLeader.getLevelReq() +
                    ", production: " + localProductionLeader.getProduction().getResToGive() + " -> " + localProductionLeader.getProduction().getResToGain() + ", res to flush: " + localProductionLeader.getProduction().getResToFlush());
                }
                if (localLeaderCard.isDiscarded()) {
                    out.add(", this card is discarded");
                } else if (localLeaderCard.isActive()) {
                    out.add(", this card is active");
                } else {
                    out.add(", this card is not active");
                }
            }
        }
        return out;
    }
}
