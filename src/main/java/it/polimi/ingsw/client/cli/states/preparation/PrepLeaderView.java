package it.polimi.ingsw.client.cli.states.preparation;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.states.View;
import it.polimi.ingsw.client.cli.states.playing.BoardView;
import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.localcards.*;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.server.model.game.Resource;

import java.io.IOException;
import java.util.ArrayList;

public class PrepLeaderView extends View {
    private CLI cli;
    private LocalMulti localMulti;
    /**
     * Indicates the id of the leader cards to be removed
     */
    private ArrayList<Integer> leaderCardIds;

    public PrepLeaderView(CLI cli, LocalMulti localMulti) {
        this.cli = cli;
        this.localMulti = localMulti;
        this.localMulti.addObserver(this);
        this.localMulti.getError().addObserver(this);
        leaderCardIds = new ArrayList<>();
    }

    @Override
    public synchronized void notifyUpdate() {
        if(localMulti.getState() == LocalGameState.READY){
            localMulti.removeObserver();
            localMulti.getError().removeObserver();
            // go to local board view
            cli.setState(new BoardView(cli, localMulti, localMulti.getMainPlayer()));
            cli.getState().draw();
        }
        else draw();
    }

    @Override
    public synchronized void notifyError() {
        System.out.println(localMulti.getError().getErrorMessage());
        leaderCardIds = new ArrayList<>();
        System.out.println("Try again:");
    }


    @Override
    public synchronized void handleCommand(String ansString) {
        if (leaderCardIds.size()<2) { // there are still leaders to be picked
            try{
                int ans = Integer.parseInt(ansString);
                if(ans<5 && ans>0) {
                    leaderCardIds.add(localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(ans-1).getId());
                }
                else {
                    System.out.println("Invalid choice, try again:");
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid choice, try again:");
            }
            if(leaderCardIds.size()==2){
                try {
                    cli.getClient().sendMessage(new RemoveLeaderPrepMessage(
                            localMulti.getGameId(),
                            localMulti.getMainPlayerId(),
                            new ArrayList<>(){{
                                add(leaderCardIds.get(0));
                                add(leaderCardIds.get(1));
                            }}
                    ));
                } catch (IOException e) {
                    System.out.println("no connection from server"); // fixme
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public synchronized void draw() {
        if (leaderCardIds.size()==0) {
            System.out.println("Pick two leader cards to discard:");
            LocalCard c;
            for (int i = 1; i < 5; i++) {
                c = localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i-1);
                if (c instanceof LocalDiscountLeader){
                    LocalDiscountLeader localDiscountLeader = (LocalDiscountLeader) c;
                    System.out.print(i + ") " + localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i-1).getClass().getSimpleName());
                    System.out.print(", prod requirement: "+localDiscountLeader.getProdRequirement());
                    System.out.print(", discounted res: "+localDiscountLeader.getQuantityToDiscount()+" "+localDiscountLeader.getDiscountedRes());
                } else if (c instanceof LocalMarbleLeader){
                    LocalMarbleLeader localMarbleLeader = (LocalMarbleLeader) c;
                    System.out.print(i + ") " + localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i-1).getClass().getSimpleName());
                    System.out.print(", prod requirement: "+localMarbleLeader.getProdRequirement());
                    System.out.print(", marble: "+localMarbleLeader.getMarbleResource());
                } else if (c instanceof LocalDepotLeader){
                    LocalDepotLeader localDepotLeader = (LocalDepotLeader) c;
                    System.out.print(i + ") " + localMulti.getMainPlayer().getLocalBoard().getLeaderCards().get(i-1).getClass().getSimpleName());
                    System.out.print(", requirement: "+localDepotLeader.getReqQuantity()+" "+localDepotLeader.getResRequirement());
                    System.out.print(", depot: "+localDepotLeader.getNumberOfRes()+" "+localDepotLeader.getResType());
                } else if (c instanceof LocalProductionLeader){
                    LocalProductionLeader localProductionLeader = (LocalProductionLeader) c;
                    System.out.print(", prod requirement: "+localProductionLeader.getColorRequirement()+" at level "+localProductionLeader.getLevelReq());
                    System.out.print(", production: "+localProductionLeader.getProduction());
                }
                System.out.print("\n");
            }
        }
        else if (leaderCardIds.size()==1) {
            System.out.println("Pick another one:");
        }
        else { // already picked two cards
            System.out.println("Please wait");
        }
    }
}
