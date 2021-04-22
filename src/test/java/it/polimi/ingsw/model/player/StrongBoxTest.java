package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.model.exception.ResourceNotDiscountableException;
import it.polimi.ingsw.model.game.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class StrongBoxTest {
    private StrongBox sb;

    @Before
    public void setUp(){
        sb=new StrongBox();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addResourcesTest1(){
        TreeMap<Resource,Integer> illegalMap=new TreeMap<>(){{
           put(Resource.GOLD,-1);
        }};
        sb.addResources(illegalMap);
    }

    @Test(expected = ResourceNotDiscountableException.class)
    public void addResourcesTest2(){
        TreeMap<Resource,Integer> illegalMap=new TreeMap<>(){{
            put(Resource.FAITH,3);
        }};
        sb.addResources(illegalMap);
    }

    @Test
    public void addResourcesTest3(){
        TreeMap<Resource,Integer> map1=new TreeMap<>(){{
            put(Resource.GOLD,3);
            put(Resource.ROCK,1);
            put(Resource.SERVANT,2);
        }};
        sb.addResources(map1);
        assertEquals(map1, sb.getResources());
        assertFalse(sb.getResources().containsKey(Resource.SHIELD));
        TreeMap<Resource,Integer> map2=new TreeMap<>(){{
            put(Resource.ROCK,1);
            put(Resource.SERVANT,1);
            put(Resource.SHIELD,1);
        }};
        sb.addResources(map2);
        assertEquals(3,(int)sb.getResources().get(Resource.GOLD));
        assertEquals(2,(int)sb.getResources().get(Resource.ROCK));
        assertEquals(3,(int)sb.getResources().get(Resource.SERVANT));
        assertEquals(1,(int)sb.getResources().get(Resource.SHIELD));
    }

    @Test
    public void hasResourcesTest(){
        TreeMap<Resource,Integer> inStrongBox1=new TreeMap<>(){{
            put(Resource.GOLD,2);
            put(Resource.ROCK,5);
            put(Resource.SERVANT,8);
        }};
        sb.addResources(inStrongBox1);

        TreeMap<Resource,Integer> toSpend0=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,7);
            put(Resource.SERVANT,2);
            put(Resource.SHIELD,1);
        }};
        assertFalse(sb.hasResources(toSpend0));

        TreeMap<Resource,Integer> inStrongBox2=new TreeMap<>(){{
            put(Resource.SHIELD,6);
        }};
        sb.addResources(inStrongBox2);

        TreeMap<Resource,Integer> toSpend1=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,3);
            put(Resource.SERVANT,2);
        }};
        assertTrue(sb.hasResources(toSpend1));

        TreeMap<Resource,Integer> toSpend2=new TreeMap<>(){{
            put(Resource.GOLD,2);
            put(Resource.ROCK,8);
            put(Resource.SERVANT,2);
            put(Resource.SHIELD,2);
        }};
        assertFalse(sb.hasResources(toSpend2));

        TreeMap<Resource,Integer> toSpend3=new TreeMap<>(){{
            put(Resource.GOLD,2);
            put(Resource.ROCK,5);
            put(Resource.SERVANT,8);
            put(Resource.SHIELD,2);
        }};
        assertTrue(sb.hasResources(toSpend3));
    }

    @Test (expected = IllegalArgumentException.class)
    public void hasResourcesTestIllegal(){
        TreeMap<Resource,Integer> illegalToSpend=new TreeMap<>(){{
            put(Resource.GOLD,-2);
            put(Resource.ROCK,1);
            put(Resource.SERVANT,1);
        }};
        assertFalse(sb.hasResources(illegalToSpend));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSpendResourcesIllegal(){
        TreeMap<Resource,Integer> inStrongBox1=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,1);
            put(Resource.SERVANT,1);
        }};
        sb.addResources(inStrongBox1);
        TreeMap<Resource,Integer> illegalToSpend=new TreeMap<>(){{
            put(Resource.GOLD,-1);
        }};
        sb.spendResources(illegalToSpend);
    }

    @Test (expected = ResourceNotDiscountableException.class)
    public void testSpendResourcesIllegal2(){
        TreeMap<Resource,Integer> illegalToSpend=new TreeMap<>(){{
            put(Resource.FAITH,2);
        }};
        sb.spendResources(illegalToSpend);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHasResourcesIllegal(){
        TreeMap<Resource,Integer> inStrongBox1=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,1);
            put(Resource.SERVANT,1);
        }};
        sb.addResources(inStrongBox1);
        TreeMap<Resource,Integer> illegalToSpend=new TreeMap<>(){{
            put(Resource.GOLD,-1);
        }};
        sb.hasResources(illegalToSpend);
    }

    @Test (expected = ResourceNotDiscountableException.class)
    public void testHasResourcesIllegal2(){
        TreeMap<Resource,Integer> illegalToSpend=new TreeMap<>(){{
            put(Resource.FAITH,2);
        }};
        sb.hasResources(illegalToSpend);
    }

    @Test
    public void spendResourcesTest(){
        TreeMap<Resource,Integer> inStrongBox1=new TreeMap<>(){{
            put(Resource.GOLD,2);
            put(Resource.ROCK,5);
            put(Resource.SERVANT,8);
            put(Resource.SHIELD,7);
        }};
        sb.addResources(inStrongBox1);

        TreeMap<Resource,Integer> toSpend1=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,2);
            put(Resource.SERVANT,3);
        }};
        try{
            sb.spendResources(toSpend1);
        }catch(NotEnoughResourcesException | IllegalArgumentException e){
            fail();
        }

        assertEquals(1,(int)sb.getResources().get(Resource.GOLD));
        assertEquals(3,(int)sb.getResources().get(Resource.ROCK));
        assertEquals(5,(int)sb.getResources().get(Resource.SERVANT));
        assertEquals(7,(int)sb.getResources().get(Resource.SHIELD));

        TreeMap<Resource,Integer> toSpend2=new TreeMap<>(){{
            put(Resource.GOLD,1);
            put(Resource.ROCK,2);
            put(Resource.SERVANT,3);
            put(Resource.SHIELD,6);
        }};
        try{
            sb.spendResources(toSpend2);
        }catch(NotEnoughResourcesException | IllegalArgumentException e){
            fail();
        }

        assertFalse(sb.getResources().containsKey(Resource.GOLD));
        assertEquals(1,(int)sb.getResources().get(Resource.ROCK));
        assertEquals(2,(int)sb.getResources().get(Resource.SERVANT));
        assertEquals(1,(int)sb.getResources().get(Resource.SHIELD));
    }
}