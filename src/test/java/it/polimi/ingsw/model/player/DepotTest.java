package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.DifferentResourceForDepotException;
import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DepotTest {
    private Depot normalDepot, leaderDepot;

    @Before
    public void setUp(){
        normalDepot=new Depot(2, true);
        assertEquals(2,normalDepot.getMaxToStore());
        assertEquals(Resource.NOTHING, normalDepot.getTypeOfResource());
        assertTrue(normalDepot.isEmpty());

        leaderDepot=new Depot(Resource.GOLD);
        assertEquals(2,leaderDepot.getMaxToStore());
        assertEquals(Resource.GOLD, leaderDepot.getTypeOfResource());
        assertTrue(leaderDepot.isEmpty());
    }

    @Test
    public void addResourceExceptionTest1(){
        //howmany<0
    }

    @Test
    public void addResourceExceptionTest2(){
        //different resource to the depot
    }

    @Test
    public void addResourceExceptionTest3(){
        //overload of the depot
    }

    @Test
    public void addResourceExceptionTest4(){
        //resource not discountable
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
        leaderDepot.addResource(Resource.ROCK, 1);
    }

    @Test(expected = DifferentResourceForDepotException.class)
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

        leaderDepot.addResource(Resource.SERVANT,2);//should throw exception
    }

}