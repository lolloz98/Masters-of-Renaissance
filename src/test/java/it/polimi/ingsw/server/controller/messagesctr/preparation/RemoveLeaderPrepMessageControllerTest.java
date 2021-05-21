package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.controller.State;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RemoveLeaderPrepMessageControllerTest {
    RemoveLeaderPrepMessageController removeLeaderPrepMessageController;

    @Test
    public void testDoAction() throws ControllerException {
        int gameId = MessageControllerTestHelper.toReadyMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer game = ca.getGame();
        assertEquals(State.PLAY, ca.getGameState());
        for(Player p: game.getPlayers()){
            assertEquals(2, p.getBoard().getLeaderCards().size());
        }

        removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, ca.getNumberAndPlayers().getSecond().get(0).getPlayerId(), new ArrayList<>(){{
            add(ca.getNumberAndPlayers().getSecond().get(0).getBoard().getLeaderCards().get(0).getId());
            add(ca.getNumberAndPlayers().getSecond().get(0).getBoard().getLeaderCards().get(1).getId());
        }}));
        try {
            removeLeaderPrepMessageController.doAction(ca);
            fail();
        }catch(WrongStateControllerException ignore){}

        gameId = MessageControllerTestHelper.toReadySingle();
        ControllerActionsServerSingle cas = (ControllerActionsServerSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
        SinglePlayer gameS = cas.getGame();
        assertEquals(State.PLAY, cas.getGameState());
        assertEquals(2, gameS.getPlayer().getBoard().getLeaderCards().size());
    }

    @Test
    public void testDoActionWrongState() throws ControllerException{
        int gameId = MessageControllerTestHelper.doActionCreateGameMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, 0, new ArrayList<>(){{
            add(50);
            add(51);
        }}));
        try {
            removeLeaderPrepMessageController.doAction(ca);
            fail();
        }catch(WrongStateControllerException ignore){}
    }

    @Test
    public void testDoAction2() throws ControllerException{
        int gameId = MessageControllerTestHelper.toDecidedInitResMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, ca.getNumberAndPlayers().getSecond().get(0).getPlayerId(), new ArrayList<>(){{
            add(ca.getNumberAndPlayers().getSecond().get(0).getBoard().getLeaderCards().get(0).getId());
            add(ca.getNumberAndPlayers().getSecond().get(0).getBoard().getLeaderCards().get(1).getId());
        }}));
        removeLeaderPrepMessageController.doAction(ca);

        removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, ca.getNumberAndPlayers().getSecond().get(1).getPlayerId(), new ArrayList<>(){{
            add(ca.getNumberAndPlayers().getSecond().get(1).getBoard().getLeaderCards().get(2).getId());
            add(ca.getNumberAndPlayers().getSecond().get(1).getBoard().getLeaderCards().get(3).getId());
        }}));
        removeLeaderPrepMessageController.doAction(ca);

        // re-doing same request
        try {
            removeLeaderPrepMessageController.doAction(ca);
            fail();
        }catch(InvalidActionControllerException ignore){}

        // only one leader
        removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, ca.getNumberAndPlayers().getSecond().get(2).getPlayerId(), new ArrayList<>(){{
            add(ca.getNumberAndPlayers().getSecond().get(2).getBoard().getLeaderCards().get(2).getId());
        }}));
        try {
            removeLeaderPrepMessageController.doAction(ca);
            fail();
        }catch(InvalidActionControllerException ignore){}
    }

}