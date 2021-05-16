package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.model.cards.DevelopCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * class that models the three production slots of each player
 */
public class DevelopCardSlot implements Serializable {
    private static final long serialVersionUID = 1029L;

    private final ArrayList<DevelopCard> cards;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DevelopCardSlot){
            DevelopCardSlot t = (DevelopCardSlot) obj;
            return cards.equals(t.cards);
        }
        return false;
    }

    public DevelopCardSlot() {
        this.cards = new ArrayList<>();
    }

    public DevelopCard lastCard(){return cards.get(cards.size()-1);}

    public ArrayList<DevelopCard> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Add card to this
     *
     * @param card card to be added
     * @throws InvalidDevelopCardToSlotException if the level of the card to be added is not right
     * @throws FullDevelopSlotException          if the slot has reached the maximum capacity
     */
    public void addDevelopCard(DevelopCard card) throws InvalidDevelopCardToSlotException, FullDevelopSlotException {
        int howmanycards = cards.size();
        switch (howmanycards) {
            case 0: {
                if (card.getLevel() != 1) throw new InvalidDevelopCardToSlotException();
                cards.add(card);
                break;
            }
            case 1: {
                if (card.getLevel() != 2) throw new InvalidDevelopCardToSlotException();
                cards.add(card);
                break;
            }
            case 2: {
                if (card.getLevel() != 3) throw new InvalidDevelopCardToSlotException();
                cards.add(card);
                break;
            }
            case 3: {
                throw new FullDevelopSlotException();
            }
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Apply the production of the develop card on top of this slot
     *
     * @param resToGive resource to give to apply the production
     * @param resToGain resource to gain after the application of the production
     * @param board     board of the player who is applying the production
     * @throws InvalidResourcesByPlayerException    if resToGive or resToGain contains invalid resources
     * @throws InvalidProductionSlotChosenException if this does not contain any develop card
     */
    public void applyProduction(TreeMap<WarehouseType, TreeMap<Resource, Integer>> resToGive, TreeMap<Resource, Integer> resToGain, Board board) throws InvalidResourcesByPlayerException, InvalidProductionSlotChosenException, ProductionAlreadyActivatedException, ResourceNotDiscountableException, NotEnoughResourcesException, InvalidArgumentException, InvalidResourceQuantityToDepotException {
        if (isEmpty()) throw new InvalidProductionSlotChosenException();
        cards.get(cards.size() - 1).getProduction().applyProduction(resToGive, resToGain, board);
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }
}
