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
    private final ArrayList<DevelopCard> cards;

    public DevelopCardSlot() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<DevelopCard> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Add card to this
     *
     * @param card card to be added
     * @throws InvalidDevelopCardToSlotException if the level of the card to be added is not right
     */
    public void addDevelopCard(DevelopCard card) {
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
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Apply the production of the develop card on top of this slot
     *
     * @param resToGive resource to give to apply the production
     * @param resToGain resource to gain after the application of the production
     * @param board     board of the player who is applying the production
     * @throws InvalidResourcesByPlayerException if resToGive or resToGain contains invalid resources
     * @throws InvalidProductionChosenException  if this does not contain any develop card
     */
    public void applyProduction(TreeMap<Resource, Integer> resToGive, TreeMap<Resource, Integer> resToGain, Board board) throws InvalidResourcesByPlayerException, InvalidProductionChosenException {
        if (isEmpty()) throw new InvalidProductionChosenException();
        cards.get(cards.size() - 1).getProduction().applyProduction(resToGive, resToGain, board);
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }
}
