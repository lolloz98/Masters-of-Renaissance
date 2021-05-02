package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.DifferentResourceForDepotException;
import it.polimi.ingsw.server.model.exception.InvalidResourceQuantityToDepotException;
import it.polimi.ingsw.server.model.exception.InvalidTypeOfResourceToDepotExeption;
import it.polimi.ingsw.server.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DepotTest {
    private Depot normalDepot, leaderDepot;

    @Before
    public void setUp(){
        normalDepot=new Depot(2);
        leaderDepot=new Depot(Resource.GOLD);
    }

    @Test
    public void testNormalDepot(){
        assertEquals(2,normalDepot.getMaxToStore());
        assertEquals(Resource.NOTHING, normalDepot.getTypeOfResource());
        assertTrue(normalDepot.isEmpty());
    }

    @Test
    public void testLeaderDepot(){
        assertEquals(2,leaderDepot.getMaxToStore());
        assertEquals(Resource.GOLD, leaderDepot.getTypeOfResource());
        assertTrue(leaderDepot.isEmpty());
    }

    @Test
    public void testAddResource(){
        assertTrue(normalDepot.isResModifiable());
        assertEquals(2, normalDepot.getFreeSpace());
        assertTrue(normalDepot.isEmpty());
        normalDepot.addResource(Resource.GOLD, 2);
        assertTrue(normalDepot.contains(Resource.GOLD));
        assertTrue(normalDepot.isFull());
        assertEquals(0, normalDepot.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot.getTypeOfResource());
        normalDepot.clear();
        normalDepot.addResource(Resource.ROCK,1);
        assertTrue(normalDepot.contains(Resource.ROCK));
        assertEquals(Resource.ROCK, normalDepot.getTypeOfResource());
        assertEquals(1, normalDepot.getFreeSpace());
    }

    @Test
    public void testEnoughResources(){
        normalDepot.addResource(Resource.GOLD, 2);
        assertTrue(normalDepot.enoughResources(1));
        assertTrue(normalDepot.enoughResources(2));
        assertFalse(normalDepot.enoughResources(3));
    }

    @Test (expected = InvalidResourceQuantityToDepotException.class)
    public void testAddResourceExceptionTest1(){
        normalDepot.addResource(Resource.GOLD, 0);
    }

    @Test (expected = InvalidResourceQuantityToDepotException.class)
    public void testAddResourceExceptionTest2(){
        normalDepot.addResource(Resource.GOLD, -1);
    }

    @Test (expected = DifferentResourceForDepotException.class)
    public void testAddResourceExceptionTest3(){
        normalDepot.addResource(Resource.GOLD, 1);
        normalDepot.addResource(Resource.ROCK, 1);
    }

    @Test (expected = InvalidResourceQuantityToDepotException.class)
    public void testAddResourceExceptionTest4(){
        normalDepot.addResource(Resource.GOLD, 1);
        normalDepot.addResource(Resource.GOLD, 1);
        normalDepot.addResource(Resource.GOLD, 1);
    }

    @Test (expected = InvalidTypeOfResourceToDepotExeption.class)
    public void testAddResourceExceptionTest5(){
        normalDepot.addResource(Resource.NOTHING, 1);
    }

    @Test
    public void addResourceNormalDepotTest1() {
        normalDepot.addResource(Resource.GOLD, 1);
        assertFalse(normalDepot.isEmpty());
        assertTrue(normalDepot.contains(Resource.GOLD));
        assertEquals(1, normalDepot.getStored());
        assertEquals(1, normalDepot.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot.getTypeOfResource());
        assertFalse(normalDepot.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 1);
                     }},
                normalDepot.getStoredResources());

        normalDepot.addResource(Resource.GOLD,1);
        assertFalse(normalDepot.isEmpty());
        assertTrue(normalDepot.contains(Resource.GOLD));
        assertEquals(2, normalDepot.getStored());
        assertEquals(0, normalDepot.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot.getTypeOfResource());
        assertTrue(normalDepot.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 2);
                     }},
                normalDepot.getStoredResources());

        int oldstored=normalDepot.clear();
        assertEquals(2, oldstored);
        assertTrue(normalDepot.isEmpty());

        normalDepot.addResource(Resource.ROCK,2);
        assertFalse(normalDepot.isEmpty());
        assertTrue(normalDepot.contains(Resource.ROCK));
        assertEquals(2, normalDepot.getStored());
        assertEquals(0, normalDepot.getFreeSpace());
        assertEquals(Resource.ROCK, normalDepot.getTypeOfResource());
        assertTrue(normalDepot.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.ROCK, 2);
                     }},
                normalDepot.getStoredResources());
    }

    @Test(expected = DifferentResourceForDepotException.class)
    public void addResourceLeaderDepotExceptionTest1(){
        assertFalse(leaderDepot.isResModifiable());
        leaderDepot.addResource(Resource.ROCK, 1);
    }

    @Test
    public void addResourceLeaderDepotTest1(){
        leaderDepot.addResource(Resource.GOLD, 1);
        assertFalse(leaderDepot.isEmpty());
        assertTrue(leaderDepot.contains(Resource.GOLD));
        assertEquals(1, leaderDepot.getStored());
        assertEquals(1, leaderDepot.getFreeSpace());
        assertEquals(Resource.GOLD, leaderDepot.getTypeOfResource());
        assertFalse(leaderDepot.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 1);
                     }},
                leaderDepot.getStoredResources());

        int oldstored=leaderDepot.clear();
        assertEquals(1, oldstored);
    }

    @Test(expected = DifferentResourceForDepotException.class)
    public void testAddResourceLeaderDepotTest2() {
        leaderDepot.addResource(Resource.SERVANT,2);
    }
}