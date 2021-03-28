package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class ProductionTest {

    TreeMap<Resource, Integer> toGive;
    TreeMap<Resource, Integer> toGain;
    Production production;

    @Before
    public void setUp(){
        toGive = new TreeMap<Resource, Integer>(){{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};


        toGain = new TreeMap<Resource, Integer>(){{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        production = new Production(toGive, toGain);
    }

    @Test
    public void whatResourceToGive() {
        assertEquals(toGive, production.whatResourceToGive());
    }

    @Test
    public void whatResourceToGain() {
        assertEquals(toGain, production.whatResourceToGain());
    }

    @Test
    public void applyProduction() {
        // TODO: waiting for board to be implemented
    }

    @Test
    public void checkResForActivation() {
        assertTrue(production.checkResForActivation(new TreeMap<Resource, Integer>(){{
                    put(Resource.GOLD, 3);
                    put(Resource.ROCK, 2);
                }})
        );

        assertFalse(production.checkResForActivation(new TreeMap<Resource, Integer>(){{
                    put(Resource.GOLD, 4);
                    put(Resource.ROCK, 2);
                }})
        );

        assertTrue(production.checkResForActivation(new TreeMap<Resource, Integer>(){{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 2);
                    put(Resource.SHIELD, 1);
                }})
        );

        assertTrue(production.checkResForActivation(new TreeMap<Resource, Integer>(){{
                    put(Resource.GOLD, 2);
                    put(Resource.ROCK, 1);
                    put(Resource.SHIELD, 1);
                    put(Resource.SERVANT, 1);
                }})
        );
    }
}