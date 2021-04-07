package it.polimi.ingsw.model.utility;

import it.polimi.ingsw.model.game.Resource;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class UtilityTest {

    @Test
    public void testCheckTreeMapEquality() {
        TreeMap<Resource, Integer> t1 = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
        }};
        TreeMap<Resource, Integer> t2 = new TreeMap<>() {{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
        }};
        assertTrue(Utility.checkTreeMapEquality(t1, t2));

        t1 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
        }};
        t2 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
        }};
        assertFalse(Utility.checkTreeMapEquality(t1, t2));

        t1 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
            put(Resource.ROCK, 0);
        }};
        t2 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
        }};
        assertTrue(Utility.checkTreeMapEquality(t1, t2));

        t1 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
        }};
        t2 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
            put(Resource.FAITH, 6);
            put(Resource.SHIELD, 9);
            put(Resource.ANYTHING, 0);
        }};
        assertTrue(Utility.checkTreeMapEquality(t1, t2));

        t1 = new TreeMap<>();
        t2 = new TreeMap<>(){{
            put(Resource.SHIELD, 0);
        }};
        assertTrue(Utility.checkTreeMapEquality(t1, t2));

        t1 = new TreeMap<>(){{
            put(Resource.GOLD, 1);
        }};
        t2 = new TreeMap<>(){{
            put(Resource.GOLD, 2);
        }};
        assertFalse(Utility.checkTreeMapEquality(t1, t2));
    }
}