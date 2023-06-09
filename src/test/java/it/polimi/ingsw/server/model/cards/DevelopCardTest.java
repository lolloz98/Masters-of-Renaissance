package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.model.exception.AlreadyAppliedDiscountForResException;
import it.polimi.ingsw.server.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DevelopCardTest {

    DevelopCard developCard;
    TreeMap<Resource, Integer> cost;
    Production production;

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

        production = new Production(toGive, toGain);

        cost = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
        }};

        developCard = new DevelopCard(cost, production, Color.BLUE, 2, 4, 0);
    }

    private void applyDiscount() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        assertFalse(developCard.isDiscounted());
        assertEquals(cost, developCard.getCurrentCost());
        developCard.applyDiscount(Resource.ROCK, 1);
    }


    @Test
    public void testApplyDiscount1() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        applyDiscount();
    }

    @Test
    public void testApplyDiscount2() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        developCard.applyDiscount(Resource.ROCK, 2);
        assertTrue(developCard.isDiscounted());
        assertEquals(0, (int) developCard.getCurrentCost().get(Resource.ROCK));

        developCard.applyDiscount(Resource.GOLD, 1);
        assertTrue(developCard.isDiscounted());
        assertEquals((cost.get(Resource.GOLD) - 1), (int) developCard.getCurrentCost().get(Resource.GOLD));
    }

    @Test
    public void testGetCurrentCost() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        applyDiscount();
        assertTrue(developCard.isDiscounted());
        assertEquals((cost.get(Resource.ROCK) - 1), (int) developCard.getCurrentCost().get(Resource.ROCK));
    }

    @Test
    public void testIsDiscounted() {
        assertFalse(developCard.isDiscounted());
    }

    @Test
    public void testRemoveDiscounts() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        developCard.removeDiscounts();
        assertFalse(developCard.isDiscounted());
        applyDiscount();
        developCard.removeDiscounts();
        assertFalse(developCard.isDiscounted());
        assertEquals(cost, developCard.getCurrentCost());
    }

    @Test
    public void testGetProduction() {
        assertEquals(production, developCard.getProduction());
    }

    @Test
    public void testRemoveDiscount() throws ResourceNotDiscountableException, AlreadyAppliedDiscountForResException {
        assertFalse(developCard.isDiscounted());
        applyDiscount();
        assertTrue(developCard.isDiscounted());
        developCard.removeDiscount(Resource.GOLD);
        assertTrue(developCard.isDiscounted());

        developCard.removeDiscount(Resource.ROCK);
        assertFalse(developCard.isDiscounted());
        assertEquals(cost, developCard.getCurrentCost());
    }
}