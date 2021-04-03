package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.exception.InvalidProductionChosenException;
import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
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

    public void applyProduction(TreeMap<Resource,Integer> resToGive, TreeMap<Resource,Integer> resToGain, Board board) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if(lastCard==null) throw new InvalidProductionChosenException();
        lastCard.getProduction().applyProduction(resToGive,resToGain,board);
    }
}
