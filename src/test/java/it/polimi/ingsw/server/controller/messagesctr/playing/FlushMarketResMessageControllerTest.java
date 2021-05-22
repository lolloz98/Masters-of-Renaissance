package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class FlushMarketResMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(FlushMarketResMessageControllerTest.class);

    int gameId;
    ControllerActionsServerMulti ca;
    ControllerActionsServerSingle cas;
    TreeMap<Resource, Integer> chosenRes;
    TreeMap<WarehouseType, TreeMap<Resource, Integer>> toKeep;

    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }

    public void doActionTest() throws ControllerException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        chosenRes = new TreeMap<>();
        toKeep = new TreeMap<>();
        try {
            MessageControllerTestHelper.doFlushMarket(gameId, player, chosenRes, toKeep);
            fail();
        }
        catch (InvalidActionControllerException ignore){}

        MessageControllerTestHelper.doPushMarble(gameId, player, false, 3);

        // both toKeep and gain resources are wrong
        TreeMap<Resource, Integer> combination = new TreeMap<>(mp.getMarketTray().getResCombinations().get(0));
        combination.put(Resource.ROCK, combination.getOrDefault(Resource.ROCK, 0) + 1);

        try{
            TreeMap<Resource, Integer> finalCombination2 = combination;
            MessageControllerTestHelper.doFlushMarket(gameId, player, combination,
                    new TreeMap<>(){
                        {
                            put(WarehouseType.NORMAL, new TreeMap<>((Map<Resource, Integer>) finalCombination2));
                        }
                    });
            fail();
        }catch (InvalidArgumentControllerException ignore){}

        // combination is right but tookeep wrong
        combination = new TreeMap<>(mp.getMarketTray().getResCombinations().get(0));
        try{
            TreeMap<Resource, Integer> finalCombination1 = combination;
            MessageControllerTestHelper.doFlushMarket(gameId, player, new TreeMap<>(combination),
                    new TreeMap<>(){
                        {
                            finalCombination1.put(Resource.ROCK, finalCombination1.getOrDefault(Resource.ROCK, 0) + 1);
                            put(WarehouseType.NORMAL, new TreeMap<>((Map<Resource, Integer>) finalCombination1));
                        }
                    });
            fail();
        }catch (InvalidArgumentControllerException ignore){}


        TreeMap<Resource, Integer> finalCombination =  new TreeMap<>(mp.getMarketTray().getResCombinations().get(0));
        MessageControllerTestHelper.doFlushMarket(gameId, player, finalCombination,
                new TreeMap<>(){
                    {
                        put(WarehouseType.NORMAL, new TreeMap<>(finalCombination));
                    }
                });
        assertTrue(mp.getTurn().isMainActionOccurred());
        assertFalse(mp.getTurn().isMarketActivated());
        assertEquals(0, mp.getMarketTray().getResCombinations().size());
    }

    @Test
    public void testDoAction1() throws ControllerException {
        CollectionsHelper.setSeedForTest(0);
        doActionTest();
    }
    @Test
    public void testDoAction2() throws ControllerException {
        CollectionsHelper.setSeedForTest(1);
        doActionTest();
    }
    @Test
    public void testDoAction3() throws ControllerException {
        CollectionsHelper.setSeedForTest(2);
        doActionTest();
    }
    @Test
    public void testDoAction4() throws ControllerException {
        CollectionsHelper.setSeedForTest(3);
        doActionTest();
    }
    @Test
    public void testDoAction5() throws ControllerException {
        CollectionsHelper.setSeedForTest(4);
        doActionTest();
    }
}