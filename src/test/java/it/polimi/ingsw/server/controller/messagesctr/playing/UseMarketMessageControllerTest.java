package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.requests.actions.UseMarketMessage;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class UseMarketMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(UseMarketMessageControllerTest.class);

    UseMarketMessageController useMarketMessageController;
    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;

    @Test
    public void testDoAction() throws ControllerException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        try {
            MessageControllerTestHelper.doPushMarble(gameId, player, true, 3);
            fail();
        } catch (ControllerException ignore){
        }

        MessageControllerTestHelper.doPushMarble(gameId, player, true, 2);

        assertEquals(1, mp.getMarketTray().getResCombinations().size());

        try{
            MessageControllerTestHelper.doPushMarble(gameId, player, false, 1);
            fail();
        } catch (InvalidActionControllerException ignore){}
    }
}