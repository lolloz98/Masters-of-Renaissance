package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.LeaderNotRemovedControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.controller.states.GamePlayState;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.exception.WrongColorDeckException;
import it.polimi.ingsw.server.model.exception.WrongLevelDeckException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
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
        ControllerActionsMulti ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer game = ca.getGame();
        assertEquals(GamePlayState.class, ca.getGameState().getClass());
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
        ControllerActionsSingle cas = (ControllerActionsSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
        SinglePlayer gameS = cas.getGame();
        assertEquals(GamePlayState.class, cas.getGameState().getClass());
        assertEquals(2, gameS.getPlayer().getBoard().getLeaderCards().size());
    }

    @Test
    public void testDoActionWrongState() throws ControllerException{
        int gameId = MessageControllerTestHelper.doActionCreateGameMulti();
        ControllerActionsMulti ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
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
        ControllerActionsMulti ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
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
        }catch(LeaderNotRemovedControllerException ignore){}

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