package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JoinGameMessageControllerTest {

    @Test
    public void doAction() throws ControllerException {
        int gameId = MessageControllerTestHelper.doActionCreateGameMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);

        String name = "2";
        JoinGameMessageController joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId, name));
        joinGameMessageController.doAction(ca);
        assertNull(ca.getGame());
        assertEquals(2, ca.getNumberAndPlayers().getSecond().size());
        assertTrue(ca.getNumberAndPlayers().getSecond().stream().anyMatch(x -> x.getName().equals(name)));

        // cannot join a game with wrong id
        joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId + 1, name));
        try {
            joinGameMessageController.doAction(ca);
            fail();
        } catch (ControllerException ignore) {
        }

        assertNull(ca.getGame());
        assertEquals(2, ca.getNumberAndPlayers().getSecond().size());
        assertTrue(ca.getNumberAndPlayers().getSecond().stream().anyMatch(x -> x.getName().equals(name)));
    }

    @Test
    public void testDoActionOnFullGame() throws ControllerException {
        int gameId = MessageControllerTestHelper.doToPrepStateMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);

        JoinGameMessageController joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId, "invalid"));

        try {
            // cannot join already full game
            joinGameMessageController.doAction(ca);
            fail();
        } catch (ControllerException ignore) {
        }

        assertNotNull(ca.getGame());
        assertEquals(4, ca.getNumberAndPlayers().getSecond().size());
    }

    @Test
    public void testDoActionOnSingleGame() throws ControllerException {
        int gameId = MessageControllerTestHelper.doActionCreateGameSingle();
        ControllerActionsServerSingle ca = (ControllerActionsServerSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
        String name = "1";

        JoinGameMessageController joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId, name));

        try {
            // cannot join already full game
            joinGameMessageController.doAction(ca);
            fail();
        } catch (ControllerException ignore) {
        }

        assertNotNull(ca.getGame());
        assertEquals(name, ca.getGame().getPlayer().getName());
    }

    @AfterClass
    public static void cleanTmp(){
        MessageControllerTestHelper.cleanTmp();
    }
}