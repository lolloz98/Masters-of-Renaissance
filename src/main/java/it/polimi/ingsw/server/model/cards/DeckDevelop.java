package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedDiscountForResException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.server.model.exception.WrongColorDeckException;
import it.polimi.ingsw.server.model.exception.WrongLevelDeckException;
import it.polimi.ingsw.enums.Resource;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Deck for DevelopCards.
 */
public final class DeckDevelop extends Deck<DevelopCard> {
    private static final long serialVersionUID = 1013L;

    private final int level;
    private final Color color;
    private final TreeSet<Resource> resDiscounted = new TreeSet<>();

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DeckDevelop){
            DeckDevelop t = (DeckDevelop) obj;
            return super.equals(obj) && level == t.level && color == t.color && resDiscounted.equals(t.resDiscounted);
        }
        return false;
    }

    /**
     * @param cards cards to be inserted in the deck
     * @param level level of the cards in the deck
     * @param color color of the cards in the deck
     * @throws WrongLevelDeckException if there is a card with different level
     * @throws WrongColorDeckException if there is a card with different color
     */
    public DeckDevelop(ArrayList<DevelopCard> cards, int level, Color color) throws WrongLevelDeckException, WrongColorDeckException {
        super(cards);
        this.level = level;
        this.color = color;
        for (DevelopCard card : cards) {
            if (card.getLevel() != level) throw new WrongLevelDeckException();
            if (card.getColor() != color) throw new WrongColorDeckException();
        }
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    /**
     * @param r        type of resources to be discounted
     * @param quantity quantity of resources to discount
     * @throws AlreadyAppliedDiscountForResException if a discount has already been applied on that resource
     */
    public void applyDiscount(Resource r, int quantity) throws AlreadyAppliedDiscountForResException, ResourceNotDiscountableException {
        if (resDiscounted.contains(r)) throw new AlreadyAppliedDiscountForResException(r);
        for(DevelopCard card: cards){
            card.applyDiscount(r, quantity);
        }
        resDiscounted.add(r);
    }

    public void removeDiscounts() {
        cards.forEach(DevelopCard::removeDiscounts);
        resDiscounted.clear();
    }

    public boolean isDiscounted() {
        return !resDiscounted.isEmpty();
    }

    /**
     * remove discount done on resource r
     *
     * @param res type of res on which the discount must be removed
     */
    public void removeDiscount(Resource res) {
        cards.forEach(x -> x.removeDiscount(res));
        resDiscounted.remove(res);
    }
}
