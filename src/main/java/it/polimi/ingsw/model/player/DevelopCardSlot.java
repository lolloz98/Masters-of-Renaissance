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

    public DevelopCardSlot() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<DevelopCard> getCards() {
        ArrayList<DevelopCard> copy=new ArrayList<>();
        copy.addAll(cards);
        return copy;
    }

    public void addDevelopCard(DevelopCard card){
        int howmanycards=cards.size();
        switch (howmanycards){
            case 0:{
                if(card.getLevel()!=1)throw new InvalidDevelopCardToSlotException();
                cards.add(card);
            }
            case 1:{
                if(card.getLevel()!=2)throw new InvalidDevelopCardToSlotException();
                cards.add(card);
            }
            case 2:{
                if(card.getLevel()!=3)throw new InvalidDevelopCardToSlotException();
                cards.add(card);
            }
            case 3:{
                throw new FullDevelopSlotException();
            }
        }

    }

    public void applyProduction(TreeMap<Resource,Integer> resToGive, TreeMap<Resource,Integer> resToGain, Board board) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if(isEmpty()) throw new InvalidProductionChosenException();
        cards.get(cards.size()-1).getProduction().applyProduction(resToGive,resToGain,board);
    }

    public boolean isEmpty(){
        return cards.size()==0;
    }
}
