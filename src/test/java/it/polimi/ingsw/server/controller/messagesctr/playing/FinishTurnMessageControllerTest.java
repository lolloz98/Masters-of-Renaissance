package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.ManipulateGameUiTestHelper;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.MainActionNotOccurredControllerException;
import it.polimi.ingsw.server.controller.exception.ProductionsResourcesNotFlushedControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class FinishTurnMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(FinishTurnMessageControllerTest.class);

    int gameId;
    ControllerActionsServerMulti ca;
    ControllerActionsServerSingle cas;
    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }

    @Test
    public void doActionMulti() throws ControllerException, ResourceNotDiscountableException, InvalidArgumentException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        try {
            MessageControllerTestHelper.doFinishTurn(gameId, player);
            fail();
        } catch (MainActionNotOccurredControllerException ignore) {}

        // perform main action
        Color color = Color.GOLD;
        int level = 1;
        TreeMap<Resource, Integer> cost = ManipulateGameUiTestHelper.setResourcesInStrongBoxForDevelop(mp, player, color, level);
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> rightCost = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
        }};
        MessageControllerTestHelper.doBuyDevelopCard(gameId, player, level, color, 2, rightCost);

        MessageControllerTestHelper.doFinishTurn(gameId, player);
        assertEquals(mp.getPlayers().get(1), mp.getTurn().getCurrentPlayer());
    }

    @Test
    public void doActionSingle() throws ControllerException, ResourceNotDiscountableException, InvalidArgumentException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException {
        gameId = MessageControllerTestHelper.toReadySingle();
        cas = (ControllerActionsServerSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
        SinglePlayer sp = cas.getGame();
        Player player = sp.getPlayer();
        try {
            MessageControllerTestHelper.doFinishTurn(gameId, player);
            fail();
        } catch (MainActionNotOccurredControllerException ignore) {}

        // perform main action
        Color color = Color.GOLD;
        int level = 1;
        int whichSlot = 2;
        TreeMap<Resource, Integer> cost = ManipulateGameUiTestHelper.setResourcesInStrongBoxForDevelop(sp, player, color, level);
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> rightCost = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
        }};
        MessageControllerTestHelper.doBuyDevelopCard(gameId, player, level, color, whichSlot, rightCost);

        MessageControllerTestHelper.doFinishTurn(gameId, player);
        assertFalse(sp.getTurn().isLorenzoPlaying());

        // perform main action
        level = 2;
        color = Color.BLUE;
        MessageControllerTestHelper.setPlayerAndDoActivateProduction(gameId, player, color, level, whichSlot);
        try {
            MessageControllerTestHelper.doFinishTurn(gameId, player);
            fail();
        }catch (ProductionsResourcesNotFlushedControllerException ignore){}
        MessageControllerTestHelper.doFlushProductionResources(gameId, player);

        MessageControllerTestHelper.doFinishTurn(gameId, player);
        assertFalse(sp.getTurn().isLorenzoPlaying());
    }
}