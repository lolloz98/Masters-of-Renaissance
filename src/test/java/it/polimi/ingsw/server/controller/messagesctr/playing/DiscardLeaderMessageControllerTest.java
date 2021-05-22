package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.AlreadyDiscardedLeaderControllerException;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscardLeaderMessageControllerTest {

    DiscardLeaderMessageController discardLeaderMessageController;
    int gameId;
    ControllerActionsServerMulti ca;
    ControllerActionsServerSingle cas;

    @Test
    public void testDoActionMulti() throws ControllerException {
        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer mp = ca.getGame();
        Player player = mp.getPlayers().get(0);
        discardLeaderMessageController = new DiscardLeaderMessageController(new DiscardLeaderMessage(gameId, player.getPlayerId(), player.getBoard().getLeaderCards().get(0).getId()));
        discardLeaderMessageController.doAction(ca);
        assertTrue(player.getBoard().getLeaderCards().get(0).isDiscarded());
        assertEquals(1, player.getBoard().getFaithtrack().getPosition());

        discardLeaderMessageController = new DiscardLeaderMessageController(new DiscardLeaderMessage(gameId, player.getPlayerId(), player.getBoard().getLeaderCards().get(0).getId()));
        try {
            discardLeaderMessageController.doAction(ca);
            fail();
        }catch (AlreadyDiscardedLeaderControllerException ignore){}

        discardLeaderMessageController = new DiscardLeaderMessageController(new DiscardLeaderMessage(gameId, player.getPlayerId(), 0));
        try {
            discardLeaderMessageController.doAction(ca);
            fail();
        }catch (InvalidArgumentControllerException ignore){}
    }
}