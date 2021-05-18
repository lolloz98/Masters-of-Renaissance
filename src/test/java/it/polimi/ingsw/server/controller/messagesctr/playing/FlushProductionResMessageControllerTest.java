package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlushProductionResMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(FlushMarketResMessageControllerTest.class);

    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;

    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }

    @Test
    public void doAction() throws ControllerException, ResourceNotDiscountableException, InvalidArgumentException, FullDevelopSlotException, InvalidDevelopCardToSlotException, EmptyDeckException, InvalidStepsException, InvalidResourceQuantityToDepotException, NotEnoughResourcesException, EndAlreadyReachedException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);

        MessageControllerTestHelper.setPlayerAndDoActivateProduction(gameId, player, Color.PURPLE, 1, 0);
        MessageControllerTestHelper.setPlayerAndDoActivateProduction(gameId, player, Color.BLUE, 1, 1);

        MessageControllerTestHelper.doFlushProductionResources(gameId, player);
        assertTrue(mp.getTurn().isMainActionOccurred());
    }
}