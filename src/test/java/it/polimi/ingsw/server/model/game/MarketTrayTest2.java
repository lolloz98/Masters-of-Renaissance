package it.polimi.ingsw.server.model.game;

import com.google.gson.Gson;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MarketTrayTest2 {
    MarketTray marketTray;
    Marble[][] matrixOriginal;
    Marble freeMarbleOriginal;
    Gson gson;

    String afterString;
    Marble free;
    ArrayList<TreeMap<Resource, Integer>> combinations;

    @Before
    public void setUp() {
        CollectionsHelper.setTest();
        marketTray = new MarketTray(new MarbleDispenserCollection());
        String originalMatrixString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SHIELD'}]]";
        gson = new Gson();
        matrixOriginal = gson.fromJson(originalMatrixString, Marble[][].class);
        freeMarbleOriginal = new Marble(Resource.SHIELD);
    }

    private void checkMatrixEq(Marble[][] m1, Marble[][] m2) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++) {
                assertEquals(m1[i][j], m2[i][j]);
            }
    }

    private void checkResources(ArrayList<TreeMap<Resource, Integer>> combinations){
        for(TreeMap<Resource, Integer> i: combinations){
            assertTrue(marketTray.checkResources(i));
        }
    }

    private void checkAfterPush(String afterString, Marble free, ArrayList<TreeMap<Resource, Integer>> combinations){
        Marble[][] afterMatrix = new Gson().fromJson(afterString, Marble[][].class);
        assertEquals(free, marketTray.getFreeMarble());
        checkMatrixEq(afterMatrix, marketTray.getMarbleMatrix());
        assertEquals(combinations.size(), marketTray.getResCombinations().size());
        assertEquals(combinations, marketTray.getResCombinations());
        checkResources(combinations);
    }

    @Test
    public void testGetMarbleMatrix() {
        TreeMap<Resource, Integer> resources = new TreeMap<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++) {
                assertEquals(matrixOriginal[i][j], marketTray.getMarbleMatrix()[i][j]);
                Resource r = matrixOriginal[i][j].getResource();
                resources.put(r, resources.getOrDefault(r, 0) + 1);
            }

        assertEquals(freeMarbleOriginal, marketTray.getFreeMarble());
        Resource r = freeMarbleOriginal.getResource();
        resources.put(r, resources.getOrDefault(r, 0) + 1);

        assertEquals(1, (int) resources.get(Resource.FAITH));
        assertEquals(2, (int) resources.get(Resource.GOLD));
        assertEquals(2, (int) resources.get(Resource.ROCK));
        assertEquals(2, (int) resources.get(Resource.SERVANT));
        assertEquals(4, (int) resources.get(Resource.NOTHING));
        assertEquals(2, (int) resources.get(Resource.SHIELD));
    }

    @Test
    public void testPushMarbleRowNoLeader() throws MarketTrayNotEmptyException, MatrixIndexOutOfBoundException {
        marketTray.pushMarble(true, 2);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SHIELD'}, {'resource':'SHIELD'},]]";
        free = new Marble(Resource.GOLD);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 1);
                put(Resource.GOLD, 1);
                put(Resource.SERVANT, 1);
                put(Resource.ROCK, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        try {
            marketTray.pushMarble(true, 0);
            fail();
        } catch (MarketTrayNotEmptyException ignore) {
        }

        marketTray.removeResources();
        marketTray.pushMarble(true, 0);

        afterString =
                "[[{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}, {'resource':'GOLD'}]," +
                        "[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SHIELD'}, {'resource':'SHIELD'},]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.FAITH, 1);
                put(Resource.ROCK, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();
        assertEquals(0, marketTray.getResCombinations().size());
        try {
            marketTray.pushMarble(true, 3);
        } catch (MatrixIndexOutOfBoundException ignore) {
        }
        try {
            marketTray.pushMarble(true, -1);
        } catch (MatrixIndexOutOfBoundException ignore) {
        }

        marketTray.pushMarble(true, 1);

        afterString =
                "[[{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}, {'resource':'GOLD'}]," +
                        "[{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}, {'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SHIELD'}, {'resource':'SHIELD'},]]";
        free = new Marble(Resource.GOLD);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SERVANT, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);
    }

    @Test
    public void testPushMarbleColumnNoLeader() throws MarketTrayNotEmptyException, MatrixIndexOutOfBoundException {
        marketTray.pushMarble(false, 2);
        String afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SHIELD'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.ROCK);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 2);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        try {
            marketTray.pushMarble(false, 0);
            fail();
        } catch (MarketTrayNotEmptyException ignore) {
        }
        marketTray.removeResources();

        marketTray.pushMarble(false, 0);
        afterString =
                "[[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'ROCK'},{'resource':'SHIELD'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.GOLD, 2);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();
        try {
            marketTray.pushMarble(false, 4);
            fail();
        } catch (MatrixIndexOutOfBoundException ignore) {
        }
        try {
            marketTray.pushMarble(false, -1);
        } catch (MatrixIndexOutOfBoundException ignore) {
        }

        marketTray.pushMarble(false, 1);
        afterString =
                "[[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'NOTHING'},{'resource':'SHIELD'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();
        marketTray.pushMarble(false, 3);
        afterString =
                "[[{'resource':'GOLD'},{'resource':'NOTHING'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'ROCK'},{'resource':'NOTHING'},{'resource':'SHIELD'},{'resource':'NOTHING'}]]";
        free = new Marble(Resource.FAITH);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.FAITH, 1);
                put(Resource.SHIELD, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);
    }

    @Test
    public void testPushMarbleOneLeader() throws AlreadyPresentLeaderResException, TooManyLeaderResourcesException, MarketTrayNotEmptyException, MatrixIndexOutOfBoundException, NoSuchResourceException {
        marketTray.addLeaderResource(Resource.GOLD);
        marketTray.pushMarble(false, 1);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'SERVANT'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();
        marketTray.removeLeaderResource(Resource.GOLD);

        marketTray.addLeaderResource(Resource.ROCK);
        marketTray.pushMarble(true, 1);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'NOTHING'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'SERVANT'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.GOLD);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.ROCK, 2);
                put(Resource.SERVANT, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();
        marketTray.removeLeaderResource(Resource.ROCK);

        marketTray.addLeaderResource(Resource.SHIELD);
        marketTray.pushMarble(false, 2);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.ROCK);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 1);
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(false, 3);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.FAITH);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.FAITH, 1);
                put(Resource.SHIELD, 2);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(true, 0);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 4);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(true, 0);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>(){{
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 3);
                put(Resource.FAITH, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);
    }

    @Test
    public void testPushMarbleTwoLeaders() throws AlreadyPresentLeaderResException, TooManyLeaderResourcesException, MarketTrayNotEmptyException, MatrixIndexOutOfBoundException {
        marketTray.addLeaderResource(Resource.GOLD);
        try {
            marketTray.addLeaderResource(Resource.GOLD);
            fail();
        } catch (AlreadyPresentLeaderResException ignore) {
        }

        marketTray.addLeaderResource(Resource.SERVANT);
        try {
            marketTray.addLeaderResource(Resource.ROCK);
            fail();
        } catch (TooManyLeaderResourcesException ignore) {
        }


        marketTray.pushMarble(false, 1);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'GOLD'},{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'SERVANT'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 2);
                put(Resource.ROCK, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.GOLD, 1);
                put(Resource.SERVANT, 1);
                put(Resource.ROCK, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.GOLD, 2);
                put(Resource.ROCK, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(true, 1);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'ROCK'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'NOTHING'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'SERVANT'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.GOLD);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 2);
                put(Resource.GOLD, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 1);
                put(Resource.GOLD, 2);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(false, 2);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'NOTHING'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'SHIELD'}]]";
        free = new Marble(Resource.ROCK);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 2);
            }});
            add(new TreeMap<>() {{
                put(Resource.ROCK, 1);
                put(Resource.SERVANT, 1);
                put(Resource.GOLD, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(false, 3);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.FAITH);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 1);
                put(Resource.FAITH, 1);
                put(Resource.SERVANT, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.SHIELD, 1);
                put(Resource.FAITH, 1);
                put(Resource.GOLD, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(true, 0);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 4);
            }});
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 3);
                put(Resource.GOLD, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 2);
                put(Resource.GOLD, 2);
            }});
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 1);
                put(Resource.GOLD, 3);
            }});
            add(new TreeMap<>() {{
                put(Resource.GOLD, 4);
            }});
        }};
        checkAfterPush(afterString, free, combinations);

        marketTray.removeResources();

        marketTray.pushMarble(true, 0);
        afterString =
                "[[{'resource':'NOTHING'},{'resource':'NOTHING'},{'resource':'FAITH'},{'resource':'NOTHING'}]," +
                        "[{'resource':'ROCK'},{'resource':'SERVANT'},{'resource':'SERVANT'},{'resource':'SHIELD'}]," +
                        "[{'resource':'GOLD'},{'resource':'SHIELD'},{'resource':'GOLD'},{'resource':'ROCK'}]]";
        free = new Marble(Resource.NOTHING);
        combinations = new ArrayList<>() {{
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 3);
                put(Resource.FAITH, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 2);
                put(Resource.GOLD, 1);
                put(Resource.FAITH, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.SERVANT, 1);
                put(Resource.GOLD, 2);
                put(Resource.FAITH, 1);
            }});
            add(new TreeMap<>() {{
                put(Resource.GOLD, 3);
                put(Resource.FAITH, 1);
            }});
        }};
        checkAfterPush(afterString, free, combinations);
    }
}