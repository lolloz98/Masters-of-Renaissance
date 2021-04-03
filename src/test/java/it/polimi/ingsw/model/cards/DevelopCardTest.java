package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DevelopCardTest {

    DevelopCard developCard;
    TreeMap<Resource, Integer> cost;

    @Before
    public void setUp(){
        TreeMap<Resource, Integer> toGive = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};

        TreeMap<Resource, Integer> toGain = new TreeMap<>(){{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        Production production = new Production(toGive, toGain);

        cost = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
        }};

        developCard = new DevelopCard(cost, production, Color.BLUE, 2, 4, 0);
    }

    @Test
    public void testApplyDiscount() {
        assertFalse(developCard.isDiscounted());
        assertEquals(cost, developCard.getCurrentCost());
        developCard.applyDiscount(Resource.ROCK, 1);
    }

    @Test
    public void testGetCurrentCost() {
        testApplyDiscount();
        assertTrue(developCard.isDiscounted());
        assertEquals((cost.get(Resource.ROCK) - 1), (int) developCard.getCurrentCost().get(Resource.ROCK));
    }

    @Test
    public void testIsDiscounted() {
        assertFalse(developCard.isDiscounted());
    }

    @Test
    public void testRemoveDiscounts() {
        developCard.removeDiscounts();
        assertFalse(developCard.isDiscounted());
        testApplyDiscount();
        developCard.removeDiscounts();
        assertFalse(developCard.isDiscounted());
        assertEquals(cost, developCard.getCurrentCost());
    }

    @Test
    public void testGetProduction() {
        // TODO: do this when Board implementation is completed
    }
}