package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MatrixIndexOutOfBoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MarketTrayTest {
    MarketTray marketTray;

    @Before
    public void setUp() {
        marketTray = new MarketTray(new MarbleDispenserTester());
    }

    @Test
    public void TestPushMarbleRowNoLeader() {
        // given the arrangement of the marble matrix, pushing index 1 onRow true shoud result in:
        Marble[][] mat = new Marble[3][4];
        mat[0][0] = new Marble(Resource.NOTHING); mat[0][1] = new Marble(Resource.NOTHING); mat[0][2] = new Marble(Resource.NOTHING); mat[0][3] = new Marble(Resource.NOTHING);
        mat[1][0] = new Marble(Resource.SHIELD); mat[1][1] = new Marble(Resource.ROCK); mat[1][2] = new Marble(Resource.ROCK); mat[1][3] = new Marble(Resource.FAITH);
        mat[2][0] = new Marble(Resource.SERVANT); mat[2][1] = new Marble(Resource.SERVANT); mat[2][2] = new Marble(Resource.GOLD); mat[2][3] = new Marble(Resource.GOLD);
        Marble freeMarble = new Marble(Resource.SHIELD);
        // the return should be
        ArrayList<TreeMap<Resource, Integer>> resComb = new ArrayList<>();
        resComb.add(new TreeMap<>(){{
            put(Resource.ROCK, 2);
            put(Resource.SHIELD, 2);
        }});
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(true, 1);
        Marble[][] marbleMatrix = marketTray.getMarbleMatrix();
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(mat[i][j].getResource(), marbleMatrix[i][j].getResource());
            }
        }
        // test the correctness of the free marble
        assertEquals(freeMarble.getResource(), marketTray.getFreeMarble().getResource());
        // test the correctness of the return
        assertEquals(resComb, result);
    }

    @Test
    public void TestPushMarbleColumnNoLeader() {
        // given the arrangement of the marble matrix, pushing index 1 onRow true shoud result in:
        Marble[][] mat = new Marble[3][4];
        mat[0][0] = new Marble(Resource.NOTHING); mat[0][1] = new Marble(Resource.NOTHING); mat[0][2] = new Marble(Resource.ROCK); mat[0][3] = new Marble(Resource.NOTHING);
        mat[1][0] = new Marble(Resource.SHIELD); mat[1][1] = new Marble(Resource.SHIELD); mat[1][2] = new Marble(Resource.GOLD); mat[1][3] = new Marble(Resource.ROCK);
        mat[2][0] = new Marble(Resource.SERVANT); mat[2][1] = new Marble(Resource.SERVANT); mat[2][2] = new Marble(Resource.FAITH); mat[2][3] = new Marble(Resource.GOLD);
        Marble freeMarble = new Marble(Resource.NOTHING);
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(false, 2);
        Marble[][] marbleMatrix = marketTray.getMarbleMatrix();
        // the return should be
        ArrayList<TreeMap<Resource, Integer>> resComb = new ArrayList<>();
        resComb.add(new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 1);
        }});
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(mat[i][j].getResource(), marbleMatrix[i][j].getResource());
            }
        }
        // test the correctness of the free marble
        assertEquals(freeMarble.getResource(), marketTray.getFreeMarble().getResource());
        // test the correctness of the return
        assertEquals(resComb, result);
    }

    @Test
    public void TestPushMarbleOneLeader() {
        marketTray.addLeaderResource(Resource.SHIELD);
        // given the arrangement of the marble matrix, pushing index 1 onRow true shoud result in:
        Marble[][] mat = new Marble[3][4];
        mat[0][0] = new Marble(Resource.NOTHING); mat[0][1] = new Marble(Resource.NOTHING); mat[0][2] = new Marble(Resource.NOTHING); mat[0][3] = new Marble(Resource.ROCK);
        mat[1][0] = new Marble(Resource.SHIELD); mat[1][1] = new Marble(Resource.SHIELD); mat[1][2] = new Marble(Resource.ROCK); mat[1][3] = new Marble(Resource.GOLD);
        mat[2][0] = new Marble(Resource.SERVANT); mat[2][1] = new Marble(Resource.SERVANT); mat[2][2] = new Marble(Resource.GOLD); mat[2][3] = new Marble(Resource.FAITH);
        Marble freeMarble = new Marble(Resource.NOTHING);
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(false, 3);
        // the return should be
        ArrayList<TreeMap<Resource, Integer>> resComb = new ArrayList<>();
        resComb.add(new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.SHIELD, 1);
            put(Resource.ROCK, 1);
        }});
        Marble[][] marbleMatrix = marketTray.getMarbleMatrix();
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(mat[i][j].getResource(), marbleMatrix[i][j].getResource());
            }
        }
        // test the correctness of the free marble
        assertEquals(freeMarble.getResource(), marketTray.getFreeMarble().getResource());
        // test the correctness of the return
        assertEquals(resComb, result);
    }

    @Test
    public void TestPushMarbleTwoLeaders() {
        // worst case scenario, two leader resources and 4 white marbles in line
        marketTray.addLeaderResource(Resource.SHIELD);
        marketTray.addLeaderResource(Resource.SERVANT);
        // given the arrangement of the marble matrix, pushing index 1 onRow true shoud result in:
        Marble[][] mat = new Marble[3][4];
        mat[0][0] = new Marble(Resource.NOTHING); mat[0][1] = new Marble(Resource.NOTHING); mat[0][2] = new Marble(Resource.NOTHING); mat[0][3] = new Marble(Resource.FAITH);
        mat[1][0] = new Marble(Resource.SHIELD); mat[1][1] = new Marble(Resource.SHIELD); mat[1][2] = new Marble(Resource.ROCK); mat[1][3] = new Marble(Resource.ROCK);
        mat[2][0] = new Marble(Resource.SERVANT); mat[2][1] = new Marble(Resource.SERVANT); mat[2][2] = new Marble(Resource.GOLD); mat[2][3] = new Marble(Resource.GOLD);
        Marble freeMarble = new Marble(Resource.NOTHING);
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(true, 0);
        // the return should be
        ArrayList<TreeMap<Resource, Integer>> resComb = new ArrayList<>();
        resComb.add(new TreeMap<>(){{
            put(Resource.SHIELD, 4);
        }});
        resComb.add(new TreeMap<>(){{
            put(Resource.SHIELD, 3);
            put(Resource.SERVANT, 1);
        }});
        resComb.add(new TreeMap<>(){{
            put(Resource.SHIELD, 2);
            put(Resource.SERVANT, 2);
        }});
        resComb.add(new TreeMap<>(){{
            put(Resource.SHIELD, 1);
            put(Resource.SERVANT, 3);
        }});
        resComb.add(new TreeMap<>(){{
            put(Resource.SERVANT, 4);
        }});
        Marble[][] marbleMatrix = marketTray.getMarbleMatrix();
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(mat[i][j].getResource(), marbleMatrix[i][j].getResource());
            }
        }
        // test the correctness of the free marble
        assertEquals(freeMarble.getResource(), marketTray.getFreeMarble().getResource());
        // test the correctness of the return
        assertTrue(result.containsAll(resComb) && resComb.containsAll(result));
    }

    @Test(expected = MatrixIndexOutOfBoundException.class)
    public void TestPushMarbleMinusOne() {
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(true, -1);
    }

    @Test(expected = MatrixIndexOutOfBoundException.class)
    public void TestPushMarbleOutOfBound() {
        ArrayList<TreeMap<Resource, Integer>> result = marketTray.pushMarble(false, 4);
    }

}