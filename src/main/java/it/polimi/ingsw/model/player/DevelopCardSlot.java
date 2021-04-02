package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

public class DevelopCardSlot {
    private ArrayList<DevelopCard> cards;
    private DevelopCard lastCard;

    public DevelopCardSlot() {
        this.cards = new ArrayList<>();
        lastCard=null;
    }

    public void addDevelopCard(DevelopCard card){
        //fare check sui livelli
        //fare check sulla pienezza
    }

    //public TreeMap<Resource,Integer> getCostOfProduction(){}


    public void applyProduction(Game game){
        TreeMap<Resource,Integer> resToGive= new TreeMap<>();
        resToGive.putAll(lastCard.getProduction().whatResourceToGive());
        //game.getTurn().getCurrentPlayer()


    }
}
