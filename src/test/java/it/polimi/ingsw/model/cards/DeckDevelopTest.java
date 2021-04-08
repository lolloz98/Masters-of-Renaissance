package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class DeckDevelopTest {

    DeckDevelop deck;
    ArrayList<DevelopCard> cards;

    @Before
    public void setUp() throws Exception {
        // TODO: create files to setup the cards.
        // TODO: test more this class
        TreeMap<Resource, Integer> toGive = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};

        TreeMap<Resource, Integer> toGain = new TreeMap<>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        Production production = new Production(toGive, toGain);
        cards = new ArrayList<>() {
            {
                add(new DevelopCard(new TreeMap<>() {{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 1);
                }}, production, Color.GREEN, 2, 4, 0));
            }
        };

        deck = new DeckDevelop(cards, 2, Color.GREEN);
    }

    @Test
    public void testApplyDiscount() {
        deck.applyDiscount(Resource.GOLD, 1);
        while(!deck.isEmpty()){
            DevelopCard tmp = deck.drawCard();
            if(tmp.getCurrentCost().get(Resource.GOLD) != null) assertTrue(tmp.isDiscounted());
            else assertFalse(tmp.isDiscounted());
        }
        deck.applyDiscount(Resource.SHIELD, 2); // Nothing happens -> no cards in deck
    }

    @Test
    public void testRemoveDiscounts() {
        deck.removeDiscounts(); // nothing happens: no discounts applied
        assertFalse(deck.isDiscounted());
        deck.applyDiscount(Resource.GOLD, 1);
        assertTrue(deck.isDiscounted());
        deck.removeDiscounts();
        assertFalse(deck.isDiscounted());
        while(!deck.isEmpty()){
            assertFalse(deck.drawCard().isDiscounted());
        }
    }

    @Test
    public void testRemoveDiscount() {
        assertFalse(deck.isDiscounted());
        deck.removeDiscount(Resource.GOLD); // nothing happens: no discounts applied
        assertFalse(deck.isDiscounted());

        deck.applyDiscount(Resource.GOLD, 1);
        assertTrue(deck.isDiscounted());

        deck.removeDiscount(Resource.ROCK);
        assertTrue(deck.isDiscounted());

        deck.removeDiscount(Resource.GOLD);
        assertFalse(deck.isDiscounted());
    }
}