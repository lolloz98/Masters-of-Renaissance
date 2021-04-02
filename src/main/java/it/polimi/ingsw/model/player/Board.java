package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.cards.Production;
import it.polimi.ingsw.model.cards.VictoryPointCalculator;
import it.polimi.ingsw.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.model.cards.leader.ProductionLeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class Board  {
    private FaithTrack faithtrack;
    private StrongBox strongbox;
    private ArrayList<DevelopCardSlot> DevelopCardSlots;
    private ArrayList<ProductionLeaderCard> LeaderCardSlots;
    private Production normalProduction;
    private ArrayList<DepotLeaderCard> depotLeaders;
    private ArrayList<Depot> depots;


    public FaithTrack getFaithtrack() {
        return faithtrack;
    }

    //public int calcPoints() {}

   /* public void activateProduction(Game game, int whichprod){
        //TODO:controllo su whichprod
        TreeMap<Resource,Integer> resToGive= new TreeMap<>();
        resToGive.putAll(DevelopCardSlots.get(whichprod - 1).getCostOfProduction());
        if(enoughResToActivate(resToGive)) {
            spendResources(resToGive);
            DevelopCardSlots.get(whichprod-1).applyProduction(game);
        }
        */


    }



