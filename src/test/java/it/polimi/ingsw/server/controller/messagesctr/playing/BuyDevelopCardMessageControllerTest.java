package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.messages.requests.actions.BuyDevelopCardMessage;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class BuyDevelopCardMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(BuyDevelopCardMessageControllerTest.class);

    BuyDevelopCardMessageController buyDevelopCardMessageController;
    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;

    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }
    @Test
    public void doAction() throws ControllerException, EmptyDeckException, FigureAlreadyDiscardedException, ResourceNotDiscountableException, InvalidArgumentException, FigureAlreadyActivatedException, InvalidStepsException, EndAlreadyReachedException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Color color = Color.PURPLE;
        int level = 1;
        int whichDeck = 1;
        int whichSlot = 1;
        Player player = mp.getPlayers().get(0);
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> rightCost = new TreeMap<>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(mp.getDecksDevelop().get(color).get(level).topCard().getCost()));
        }};
        try {
            // Not enough resources to perform the desired action
            MessageControllerTestHelper.doBuyDevelopCard(gameId, player, level, color, whichDeck, whichSlot, rightCost);
            fail();
        }catch (InvalidArgumentControllerException ignore){}

        player.getBoard().flushGainedResources(new TreeMap<>(mp.getDecksDevelop().get(color).get(level).topCard().getCost()), mp);
        MessageControllerTestHelper.doBuyDevelopCard(gameId, player, level, color, whichDeck, whichSlot, rightCost);

        try {
            // Cannot perform main action safter a main action
            MessageControllerTestHelper.doBuyDevelopCard(gameId, player, level, color, whichDeck, whichSlot, rightCost);
            fail();
        }catch (InvalidActionControllerException ignore){}
    }
}