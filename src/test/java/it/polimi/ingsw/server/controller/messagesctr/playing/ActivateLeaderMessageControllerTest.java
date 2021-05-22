package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.exception.ModelException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.utility.CollectionsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActivateLeaderMessageControllerTest {
    private static final Logger logger = LogManager.getLogger(ActivateLeaderMessageControllerTest.class);

    ActivateLeaderMessageController activateLeaderMessageController;
    int gameId;
    ControllerActionsServerMulti ca;
    ControllerActionsServerSingle cas;

    @BeforeClass
    public static void setUp(){
        CollectionsHelper.setTest();
    }

    public void activateLeader(LeaderCard<?> card, Player player, Game<?> game) throws ModelException, ControllerException {
        // logger.debug("activating card with id: " + card.getId() + ", " + card);
        if (!card.checkRequirement(player)) {
            try {
                activateLeaderMessageController.doAction(ca);
                fail();
            } catch (RequirementNotSatisfiedControllerException ignore) {
            }
            try {
                MessageControllerTestHelper.doActivateLeader(card, player, gameId);
            } catch (RequirementNotSatisfiedControllerException ignore) {
            }
        }
        else{
            MessageControllerTestHelper.doActivateLeader(card, player, gameId);
        }
        assertTrue(card.isActive());
    }

    public void doActionMultiBaseTest() throws ControllerException, ModelException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        LeaderCard<?> card = player.getBoard().getLeaderCards().get(0);
        activateLeaderMessageController = new ActivateLeaderMessageController(new ActivateLeaderMessage(gameId, player.getPlayerId(), card.getId()));

        activateLeader(card, player, mp);
        try {
            activateLeader(card, player, mp);
            fail();
        }catch (AlreadyActiveLeaderControllerException ignore){}

        player = mp.getPlayers().get(0);
        card = player.getBoard().getLeaderCards().get(1);
        activateLeaderMessageController = new ActivateLeaderMessageController(new ActivateLeaderMessage(gameId, player.getPlayerId(), card.getId()));

        activateLeader(card, player, mp);
    }

    @Test
    public void testDoActionMulti1() throws ControllerException, ModelException {
        CollectionsHelper.setSeedForTest(0);
        doActionMultiBaseTest();
    }

    @Test
    public void testDoActionMulti2() throws ControllerException, ModelException {
        CollectionsHelper.setSeedForTest(1);
        doActionMultiBaseTest();
    }

    @Test
    public void testDoActionMulti3() throws ControllerException, ModelException {
        CollectionsHelper.setSeedForTest(2);
        doActionMultiBaseTest();
    }

    @Test
    public void testDoActionMulti4() throws ControllerException, ModelException {
        CollectionsHelper.setSeedForTest(3);
        doActionMultiBaseTest();
    }

    @Test
    public void testDoActionMulti5() throws ControllerException, ModelException {
        CollectionsHelper.setSeedForTest(4);
        doActionMultiBaseTest();
    }
}