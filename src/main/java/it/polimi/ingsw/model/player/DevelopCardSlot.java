package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevelopCard;
import it.polimi.ingsw.model.exception.FullDevelopSlotException;
import it.polimi.ingsw.model.exception.InvalidDevelopCardToSlotException;
import it.polimi.ingsw.model.exception.InvalidProductionChosenException;
import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * class that models the three production slots of each player
 */
public class DevelopCardSlot {
    private ArrayList<DevelopCard> cards;
    private DevelopCard lastCard;

    public DevelopCardSlot() {
        this.cards = new ArrayList<>();
        lastCard=null;
    }

    public void addDevelopCard(DevelopCard card){
        int howmanycards=cards.size();
        switch (howmanycards){
            case 0:{
                if(card.getLevel()!=1)throw new InvalidDevelopCardToSlotException();
                lastCard=card;
                cards.add(card);
            }
            case 1:{
                if(card.getLevel()!=2)throw new InvalidDevelopCardToSlotException();
                lastCard=card;
                cards.add(card);
            }
            case 2:{
                if(card.getLevel()!=3)throw new InvalidDevelopCardToSlotException();
                lastCard=card;
                cards.add(card);
            }
            case 3:{
                throw new FullDevelopSlotException();
            }
        }

    }

    public void applyProduction(TreeMap<Resource,Integer> resToGive, TreeMap<Resource,Integer> resToGain, Board board) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if(lastCard==null) throw new InvalidProductionChosenException();
        lastCard.getProduction().applyProduction(resToGive,resToGain,board);
    }

    public boolean emptySlot(){
        return lastCard==null;
    }
}
