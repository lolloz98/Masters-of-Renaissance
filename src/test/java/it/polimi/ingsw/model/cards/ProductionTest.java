package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exception.InvalidResourcesByPlayerException;
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
    public void setUp() {
        toGive = new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD, 2);
            put(Resource.ROCK, 1);
            put(Resource.ANYTHING, 2);
        }};


        toGain = new TreeMap<Resource, Integer>() {{
            put(Resource.SHIELD, 2);
            put(Resource.ANYTHING, 1);
        }};

        production = new Production(toGive, toGain);
    }

    @Test
    public void testWhatResourceToGive() {
        assertEquals(toGive, production.whatResourceToGive());
    }

    @Test
    public void testWhatResourceToGain() {
        assertEquals(toGain, production.whatResourceToGain());
    }

    @Test
    public void testApplyProduction() {
        // TODO: waiting for board to be implemented
    }

    @Test
    public void testCheckResToGiveForActivation() {
        try {
            assertTrue(production.checkResToGiveForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 3);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertFalse(production.checkResToGiveForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 4);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertTrue(production.checkResToGiveForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 2);
                        put(Resource.ROCK, 2);
                        put(Resource.SHIELD, 1);
                    }})
            );

            assertTrue(production.checkResToGiveForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 2);
                        put(Resource.ROCK, 1);
                        put(Resource.SHIELD, 1);
                        put(Resource.SERVANT, 1);
                    }})
            );
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }
    }

    @Test
    public void testCheckResToGainForActivation() {
        try {
            assertFalse(production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 3);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertFalse(production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 4);
                        put(Resource.ROCK, 2);
                    }})
            );

            assertTrue(production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.ROCK, 1);
                        put(Resource.SHIELD, 2);
                    }})
            );

            assertTrue(production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                        put(Resource.GOLD, 1);
                        put(Resource.SHIELD, 2);
                    }})
            );
        } catch (InvalidResourcesByPlayerException e) {
            fail();
        }
    }

    @Test
    public void testCheckResException(){
        try{
            production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 1);
                put(Resource.FAITH, 2);
            }});
            fail();
        }catch (InvalidResourcesByPlayerException ignored){ }

        try{
            production.checkResToGainForActivation(new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 1);
                put(Resource.SHIELD, 2);
                put(Resource.ANYTHING, 1);
            }});
            fail();
        }catch (InvalidResourcesByPlayerException ignored){ }

        try{
            production.checkResToGiveForActivation(new TreeMap<Resource, Integer>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 1);
                put(Resource.SHIELD, 1);
                put(Resource.ANYTHING, 1);
            }});
            fail();
        }catch (InvalidResourcesByPlayerException ignored){ }
    }

    @Test
    public void testFlushGainedToBoard() {
        // TODO: after implementation of board
    }

    @Test
    public void testGetGainedResources() {
        // TODO: after implementation of board
    }
}