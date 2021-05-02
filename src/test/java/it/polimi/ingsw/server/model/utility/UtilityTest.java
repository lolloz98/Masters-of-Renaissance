package it.polimi.ingsw.server.model.utility;

import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.WarehouseType;
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

    @Test
    public void testGetTotalResources() {
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> toGive;
        TreeMap<Resource, Integer> expected;

        toGive = new TreeMap<>(){{
            put(WarehouseType.STRONGBOX, new TreeMap<>(){{
                put(Resource.ROCK, 2);
                put(Resource.GOLD, 1);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>(){{
                put(Resource.ROCK, 1);
                put(Resource.SHIELD, 3);
            }});
            put(WarehouseType.LEADER, new TreeMap<>(){{
                put(Resource.SERVANT, 2);
            }});
        }};
        expected = new TreeMap<>(){{
            put(Resource.ROCK, 3);
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 3);
            put(Resource.SERVANT, 2);
        }};

        assertEquals(expected, Utility.getTotalResources(toGive));

        toGive = new TreeMap<>(){{
            put(WarehouseType.STRONGBOX, new TreeMap<>(){{
                put(Resource.ROCK, 2);
                put(Resource.GOLD, 1);
            }});
            put(WarehouseType.NORMAL, new TreeMap<>(){{
                put(Resource.ROCK, 1);
                put(Resource.SHIELD, 3);
            }});
            put(WarehouseType.LEADER, new TreeMap<>(){{
                put(Resource.SERVANT, 2);
            }});
        }};
        expected = new TreeMap<>(){{
            put(Resource.ROCK, 3);
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 2);
        }};

        assertNotEquals(expected, Utility.getTotalResources(toGive));

        toGive = new TreeMap<>(){{
            put(WarehouseType.STRONGBOX, new TreeMap<>(){{
                put(Resource.SERVANT, 2);
            }});
            put(WarehouseType.LEADER, new TreeMap<>(){{
                put(Resource.SERVANT, 2);
                put(Resource.GOLD, 1);
            }});
        }};
        expected = new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.SERVANT, 4);
        }};

        assertEquals(expected, Utility.getTotalResources(toGive));

        toGive = new TreeMap<>(){{
            put(WarehouseType.NORMAL, new TreeMap<>(){{
                put(Resource.SHIELD, 1);
            }});
        }};
        expected = new TreeMap<>(){{
            put(Resource.SHIELD, 1);
        }};

        assertEquals(expected, Utility.getTotalResources(toGive));

        toGive = new TreeMap<>();
        expected = new TreeMap<>();

        assertEquals(expected, Utility.getTotalResources(toGive));
    }
}