package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.enums.WarehouseType;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.model.cards.DevelopCard;
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

public class ApplyProductionMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(BuyDevelopCardMessageControllerTest.class);

    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;

    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }

    @Test
    public void doAction() throws ControllerException, ResourceNotDiscountableException, InvalidArgumentException, EmptyDeckException, InvalidStepsException, EndAlreadyReachedException, FullDevelopSlotException, InvalidDevelopCardToSlotException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        try {
            MessageControllerTestHelper.doApplyProduction(gameId, player, 1, new TreeMap<>(), new TreeMap<>());
            fail();
        }catch (InvalidArgumentControllerException ignore){}

        Color color = Color.BLUE;
        int level = 1;
        TreeMap<Resource, Integer> cost = MessageControllerTestHelper.setResourcesInStrongBoxForDevelop(mp, player, color, level);

        DevelopCard card = ca.getGame().getDecksDevelop().get(color).get(level).topCard();
        TreeMap<Resource, Integer> toGive = card.getProduction().whatResourceToGive();
        TreeMap<Resource, Integer> toGain = card.getProduction().whatResourceToGain();
        TreeMap<WarehouseType, TreeMap<Resource, Integer>> rightToGive = new TreeMap<>(){{
            put(WarehouseType.STRONGBOX, new TreeMap<>(toGive));
        }};
        try {
            // Not yet bought a card
            MessageControllerTestHelper.doApplyProduction(gameId, player, 1, rightToGive, toGain);
            fail();
        }catch(InvalidArgumentControllerException ignore){}

        // buy the card
        player.getBoard().buyDevelopCard(mp, color, level, 1, new TreeMap<WarehouseType, TreeMap<Resource, Integer>>() {{
            put(WarehouseType.STRONGBOX, new TreeMap<>(cost));
        }});

        player.getBoard().flushGainedResources(new TreeMap<>(toGive), mp);

        MessageControllerTestHelper.doApplyProduction(gameId, player, 2, rightToGive, toGain);
        assertTrue(mp.getTurn().isProductionsActivated());
        assertFalse(card.getProduction().getGainedResources().isEmpty());
    }
}