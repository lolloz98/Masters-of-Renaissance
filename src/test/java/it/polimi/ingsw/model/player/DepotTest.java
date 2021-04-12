package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.DifferentResourceForDepotException;
import it.polimi.ingsw.model.exception.InvalidResourceQuantityToDepotException;
import it.polimi.ingsw.model.exception.InvalidTypeOfResourceToDepotExeption;
import it.polimi.ingsw.model.exception.TooManyResourcesToAddExeption;
import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class DepotTest {
    private Depot normalDepot1, normalDepot2, leaderDepot;

    @Before
    public void setUp(){
        normalDepot1 =new Depot(2, true);
        assertEquals(2, normalDepot1.getMaxToStore());
        assertEquals(Resource.NOTHING, normalDepot1.getTypeOfResource());
        assertTrue(normalDepot1.isEmpty());

        normalDepot2=new Depot(3, true);
        assertEquals(3, normalDepot2.getMaxToStore());
        assertEquals(Resource.NOTHING, normalDepot2.getTypeOfResource());
        assertTrue(normalDepot2.isEmpty());

        leaderDepot=new Depot(Resource.GOLD);
        assertEquals(2,leaderDepot.getMaxToStore());
        assertEquals(Resource.GOLD, leaderDepot.getTypeOfResource());
        assertTrue(leaderDepot.isEmpty());
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void addResourceExceptionTest1(){
        normalDepot1.addResource(Resource.SHIELD, 0);
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void addResourceExceptionTest1_1(){
        normalDepot1.addResource(Resource.SHIELD, -1);
    }

    @Test(expected = DifferentResourceForDepotException.class)
    public void addResourceExceptionTest2(){
        normalDepot1.addResource(Resource.ROCK,1);
        normalDepot1.addResource(Resource.GOLD,1);
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void addResourceExceptionTest3(){
        normalDepot1.addResource(Resource.ROCK,1);
        normalDepot1.addResource(Resource.ROCK,1);
        normalDepot1.addResource(Resource.ROCK,1);
    }

    @Test(expected = InvalidTypeOfResourceToDepotExeption.class)
    public void addResourceExceptionTest4(){
        normalDepot1.addResource(Resource.NOTHING,1);
    }

    @Test
    public void addResourceNormalDepotTest1() {
        normalDepot1.addResource(Resource.GOLD, 1);
        assertFalse(normalDepot1.isEmpty());
        assertTrue(normalDepot1.contains(Resource.GOLD));
        assertEquals(1, normalDepot1.getStored());
        assertEquals(1, normalDepot1.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot1.getTypeOfResource());
        assertFalse(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 1);
                     }},
                normalDepot1.getStoredResources());

        normalDepot1.addResource(Resource.GOLD,1);
        assertFalse(normalDepot1.isEmpty());
        assertTrue(normalDepot1.contains(Resource.GOLD));
        assertEquals(2, normalDepot1.getStored());
        assertEquals(0, normalDepot1.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot1.getTypeOfResource());
        assertTrue(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 2);
                     }},
                normalDepot1.getStoredResources());

        int oldstored= normalDepot1.clear();
        assertEquals(2, oldstored);
        assertTrue(normalDepot1.isEmpty());

        normalDepot1.addResource(Resource.ROCK,2);
        assertFalse(normalDepot1.isEmpty());
        assertTrue(normalDepot1.contains(Resource.ROCK));
        assertEquals(2, normalDepot1.getStored());
        assertEquals(0, normalDepot1.getFreeSpace());
        assertEquals(Resource.ROCK, normalDepot1.getTypeOfResource());
        assertTrue(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.ROCK, 2);
                     }},
                normalDepot1.getStoredResources());
    }

    @Test
    public void addResourceNormalDepotTest2(){
        normalDepot2.addResource(Resource.GOLD, 1);
        assertFalse(normalDepot2.isEmpty());
        assertTrue(normalDepot2.contains(Resource.GOLD));
        assertEquals(1, normalDepot2.getStored());
        assertEquals(2, normalDepot2.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot2.getTypeOfResource());
        assertFalse(normalDepot2.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 1);
                     }},
                normalDepot2.getStoredResources());

        normalDepot2.addResource(Resource.GOLD,2);
        assertFalse(normalDepot2.isEmpty());
        assertTrue(normalDepot2.contains(Resource.GOLD));
        assertEquals(3, normalDepot2.getStored());
        assertEquals(0, normalDepot2.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot2.getTypeOfResource());
        assertTrue(normalDepot2.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 3);
                     }},
                normalDepot2.getStoredResources());

        int oldstored= normalDepot2.clear();
        assertEquals(3, oldstored);
        assertTrue(normalDepot2.isEmpty());

        normalDepot2.addResource(Resource.ROCK,2);
        assertFalse(normalDepot2.isEmpty());
        assertTrue(normalDepot2.contains(Resource.ROCK));
        assertEquals(2, normalDepot2.getStored());
        assertEquals(1, normalDepot2.getFreeSpace());
        assertEquals(Resource.ROCK, normalDepot2.getTypeOfResource());
        assertFalse(normalDepot2.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.ROCK, 2);
                     }},
                normalDepot2.getStoredResources());
    }

    @Test(expected = DifferentResourceForDepotException.class)
    public void addResourceLeaderDepotExceptionTest1(){
        leaderDepot.addResource(Resource.ROCK, 1);
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void addResourceLeaderDepotExceptionTest2(){
        leaderDepot.addResource(Resource.GOLD,3);
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

        leaderDepot.addResource(Resource.SERVANT,2);//should throws exception
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void spendResourcesNormalDepot1(){
        normalDepot1.addResource(Resource.SERVANT,2);
        normalDepot1.spendResources(2);
        assertTrue(normalDepot1.isEmpty());
        assertFalse(normalDepot1.contains(Resource.SERVANT));
        assertTrue(normalDepot1.contains(Resource.NOTHING));
        assertEquals(0, normalDepot1.getStored());
        assertEquals(2, normalDepot1.getFreeSpace());
        assertEquals(Resource.NOTHING, normalDepot1.getTypeOfResource());
        assertFalse(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() ,
                normalDepot1.getStoredResources());

        normalDepot1.addResource(Resource.SHIELD,1);
        normalDepot1.spendResources(1);
        assertTrue(normalDepot1.isEmpty());
        assertFalse(normalDepot1.contains(Resource.SERVANT));
        assertTrue(normalDepot1.contains(Resource.NOTHING));
        assertEquals(0, normalDepot1.getStored());
        assertEquals(2, normalDepot1.getFreeSpace());
        assertEquals(Resource.NOTHING, normalDepot1.getTypeOfResource());
        assertFalse(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() ,
                normalDepot1.getStoredResources());

        normalDepot1.addResource(Resource.GOLD,2);
        normalDepot1.spendResources(1);
        assertFalse(normalDepot1.isEmpty());
        assertTrue(normalDepot1.contains(Resource.GOLD));
        assertEquals(1, normalDepot1.getStored());
        assertEquals(1, normalDepot1.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot1.getTypeOfResource());
        assertFalse(normalDepot1.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
            put(Resource.GOLD,1);
                     }} ,
                normalDepot1.getStoredResources());

        normalDepot1.spendResources(2);
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void spendResourcesNormalDepot2(){
        normalDepot2.addResource(Resource.SERVANT,3);
        normalDepot2.spendResources(3);
        assertTrue(normalDepot2.isEmpty());
        assertFalse(normalDepot2.contains(Resource.SERVANT));
        assertEquals(0, normalDepot2.getStored());
        assertEquals(3, normalDepot2.getFreeSpace());
        assertEquals(Resource.NOTHING, normalDepot2.getTypeOfResource());
        assertFalse(normalDepot2.isFull());
        assertEquals(new TreeMap<Resource, Integer>() ,
                normalDepot2.getStoredResources());

        normalDepot2.addResource(Resource.GOLD, 2);
        normalDepot2.spendResources(1);
        assertFalse(normalDepot2.isEmpty());
        assertTrue(normalDepot2.contains(Resource.GOLD));
        assertEquals(1, normalDepot2.getStored());
        assertEquals(2, normalDepot2.getFreeSpace());
        assertEquals(Resource.GOLD, normalDepot2.getTypeOfResource());
        assertFalse(normalDepot2.isFull());
        assertEquals(new TreeMap<Resource, Integer>() {{
                         put(Resource.GOLD, 1);
                     }},
                normalDepot2.getStoredResources());

        normalDepot2.spendResources(0);//should throws exception
    }

    @Test(expected = InvalidResourceQuantityToDepotException.class)
    public void spendResourcesLeaderDepot1(){
        leaderDepot.addResource(Resource.GOLD,1);
        leaderDepot.spendResources(1);
        assertTrue(leaderDepot.isEmpty());
        assertEquals(0, leaderDepot.getStored());
        assertEquals(2, leaderDepot.getFreeSpace());
        assertEquals(Resource.GOLD, leaderDepot.getTypeOfResource());
        assertFalse(leaderDepot.isFull());
        assertEquals(new TreeMap<Resource, Integer>() ,
                leaderDepot.getStoredResources());

        leaderDepot.addResource(Resource.GOLD,2);
        leaderDepot.spendResources(3);
    }

}