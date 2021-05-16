package it.polimi.ingsw.client.cli.states.playing;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.GameView;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalProduction;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.server.model.player.StrongBox;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardView extends GameView {
    private LocalPlayer localPlayer;
    // private CLI cli; // todo remove

    public BoardView(CLI cli, LocalGame localGame, LocalPlayer localPlayer){
        this.localGame = localGame;
        this.cli = cli;
        this.localPlayer = localPlayer;
        localGame.getError().addObserver(this);
        localPlayer.getLocalBoard().addObserver(this);
        localGame.getLocalTurn().addObserver(this);
    }

    @Override
    public synchronized void draw(){
        CLI.clearScreen();
        // todo make this good looking
        System.out.println(localPlayer.getName() + "'s board:");
        System.out.println("Resources in depot:" + localPlayer.getLocalBoard().getResInNormalDepot());
        System.out.println("Resources in box:" + localPlayer.getLocalBoard().getResInStrongBox());
        System.out.println("Faith points:" + localPlayer.getLocalBoard().getLocalTrack().getFaithTrackScore());
        // base production
        System.out.print("Base production: res to give: "+localPlayer.getLocalBoard().getBaseProduction().getResToGive());
        System.out.print(", res to gain: "+localPlayer.getLocalBoard().getBaseProduction().getResToGain());
        System.out.print(", res to flush: "+localPlayer.getLocalBoard().getBaseProduction().getResToFlush()+"\n");
        int i;
        // first production slot
        for(i = 0; i<3; i++) {
            if(localPlayer.getLocalBoard().getDevelopCards().get(i).size() == 0){
                System.out.println("No cards in "+i+"° slot");
            } else {
                LocalProduction localProduction = localPlayer.getLocalBoard().getDevelopCards().get(i).get(localPlayer.getLocalBoard().getDevelopCards().get(i).size() - 1).getProduction();
                System.out.print((i+1)+"° production slot: res to give: " + localProduction.getResToGive());
                System.out.print(", res to gain: " + localProduction.getResToGain());
                System.out.print(", res to flush: " + localProduction.getResToFlush());
                int sum = 0;
                for (LocalDevelopCard c : localPlayer.getLocalBoard().getDevelopCards().get(i))
                    sum = sum + c.getVictoryPoints();
                System.out.print(", total vp in this slot: " + sum + "\n");
            }
        }
        System.out.println("Leader cards:");
        for(LocalCard c : localPlayer.getLocalBoard().getLeaderCards()){
            if (c instanceof LocalConcealedCard){
                System.out.print("This card is not activated yet");
            } else {
                LocalLeaderCard localLeaderCard = (LocalLeaderCard) c;
                if (c instanceof LocalDiscountLeader) {
                    LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                    System.out.print("DiscountLeader");
                    System.out.print(", prod requirement: " + localDiscountLeader.getProdRequirement());
                    System.out.print(", discounted res: " + localDiscountLeader.getQuantityToDiscount() + " " + localDiscountLeader.getDiscountedRes());
                } else if (c instanceof LocalMarbleLeader) {
                    LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                    System.out.print("MarbleLeader");
                    System.out.print(", prod requirement: " + localMarbleLeader.getProdRequirement());
                    System.out.print(", marble: " + localMarbleLeader.getMarbleResource());
                } else if (c instanceof LocalDepotLeader) {
                    LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                    System.out.print("DepotLeader");
                    System.out.print(", requirement: " + localDepotLeader.getReqQuantity() + " " + localDepotLeader.getResRequirement());
                    System.out.print(", depot: " + localDepotLeader.getNumberOfRes() + " " + localDepotLeader.getResType());
                } else if (c instanceof LocalProductionLeader) {
                    LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                    System.out.print("ProductionLeader");
                    System.out.print(", prod requirement: " + localProductionLeader.getColorRequirement() + " at level " + localProductionLeader.getLevelReq());
                    System.out.print(", production: " + localProductionLeader.getProduction());
                }
                if (localLeaderCard.isActive()){
                    System.out.print(", this card is active");
                } else {
                    System.out.print(", this card is not active");
                }
            }
            System.out.print("\n");
        }
        super.drawTurn();
    }

    @Override
    public void removeObserved() {
        localGame.getError().removeObserver();
        localPlayer.getLocalBoard().removeObserver();
        localGame.getLocalTurn().removeObserver();
    }

    @Override
    public synchronized void notifyUpdate(){
        draw();
    }

    @Override
    public void helpScreen() {
        // todo
    }

    @Override
    public synchronized void notifyError() {}

    @Override
    public synchronized void handleCommand(String ans){
        String delim = "[ ]+";
        ArrayList<String> ansList = (ArrayList<String>) Arrays.asList(ans.split(delim));
        switch (ansList.get(0)){
            // todo handle activate production (only if loadBoard.getPlayerId() == playerId)
            // todo activate leader
            default:
                super.handleCommand(ansList);
        }
    }
}
