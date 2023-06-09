package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.enums.Resource;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Test
    public void isDiscountable() {
        assertFalse(Resource.isDiscountable(Resource.ANYTHING));
        assertTrue(Resource.isDiscountable(Resource.ROCK));
        assertTrue(Resource.isDiscountable(Resource.SERVANT));
        assertTrue(Resource.isDiscountable(Resource.SHIELD));
        assertTrue(Resource.isDiscountable(Resource.GOLD));
        assertFalse(Resource.isDiscountable(Resource.FAITH));
    }
}