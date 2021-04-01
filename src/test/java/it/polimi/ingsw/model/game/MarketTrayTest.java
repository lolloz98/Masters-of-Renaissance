package it.polimi.ingsw.model.game;

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
                assertEquals(marbleMatrix[i][j].getResource(), mat[i][j].getResource());
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
        ArrayList<TreeMap<Resource, Integer>> resComb = new ArrayList<>();
        resComb.add(new TreeMap<>(){{
            put(Resource.GOLD, 1);
            put(Resource.ROCK, 1);
        }});
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(marbleMatrix[i][j].getResource(), mat[i][j].getResource());
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
        mat[0][0] = new Marble(Resource.NOTHING); mat[0][1] = new Marble(Resource.NOTHING); mat[0][2] = new Marble(Resource.NOTHING); mat[0][3] = new Marble(Resource.NOTHING);
        mat[1][0] = new Marble(Resource.SHIELD); mat[1][1] = new Marble(Resource.ROCK); mat[1][2] = new Marble(Resource.ROCK); mat[1][3] = new Marble(Resource.FAITH);
        mat[2][0] = new Marble(Resource.SERVANT); mat[2][1] = new Marble(Resource.SERVANT); mat[2][2] = new Marble(Resource.GOLD); mat[2][3] = new Marble(Resource.GOLD);
        Marble freeMarble = new Marble(Resource.SHIELD);
        marketTray.pushMarble(true, 1);
        Marble[][] marbleMatrix = marketTray.getMarbleMatrix();
        // test the correct disposition of marbles at the end of the method
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<4; j++) {
                assertEquals(marbleMatrix[i][j].getResource(), mat[i][j].getResource());
            }
        }
        // test the correctness of the free marble
        assertEquals(freeMarble.getResource(), marketTray.getFreeMarble().getResource());
        // TODO: test the correctness of the return
    }


}