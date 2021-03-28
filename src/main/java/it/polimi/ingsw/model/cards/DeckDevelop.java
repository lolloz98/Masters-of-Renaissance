package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.WrongColorDeckException;
import it.polimi.ingsw.model.exception.WrongLevelDeckException;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public final class DeckDevelop extends Deck<DevelopCard> {
    private final int level;
    private final Color color;

    /**
     * @param cards cards to be inserted in the deck
     * @param level level of the cards in the deck
     * @param color color of the cards in the deck
     * @throws WrongLevelDeckException if there is a card with different level
     * @throws WrongColorDeckException if there is a card with different color
     */
    public DeckDevelop(ArrayList<DevelopCard> cards, int level, Color color) {
        super(cards);
        this.level = level;
        this.color = color;
        for (DevelopCard card : cards) {
            if(card.getLevel() != level) throw new WrongLevelDeckException();
            if(card.getColor() != color) throw new WrongColorDeckException();
        }
    }

    public int getLevel(){
        return level;
    }

    public Color getColor(){ return color; }

    public void applyDiscount(Resource r, int quantity){
        cards.forEach(card -> card.applyDiscount(r, quantity));
    }

    public void removeDiscounts(){
        cards.forEach(DevelopCard::removeDiscounts);
    }
}
