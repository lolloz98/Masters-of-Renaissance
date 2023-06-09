package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.client.localmodel.LocalGameState;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class ChooseOneResPrepMessageControllerTest {

    ChooseOneResPrepMessageController chooseOneResPrepMessageController;

    @Test
    public void doActionOnNotYetCreatedGame() throws ControllerException {
        int gameId = MessageControllerTestHelper.doActionCreateGameMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, 0, Resource.GOLD));
        try {
            chooseOneResPrepMessageController.doAction(ca);
            fail();
        }catch(WrongStateControllerException ignore){}

        gameId = MessageControllerTestHelper.doActionCreateGameSingle();
        ControllerActionsServerSingle cas = (ControllerActionsServerSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, 0, Resource.GOLD));
        try {
            chooseOneResPrepMessageController.doAction(cas);
            fail();
        }catch(InvalidActionControllerException ignore){}
    }

    @Test
    public void doAction() throws ControllerException, InvalidArgumentException {
        Player p;
        int gameId = MessageControllerTestHelper.doToPrepStateMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer game = ca.getGame();
        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, game.getPlayers().get(0).getPlayerId(), Resource.GOLD));
        try {
            chooseOneResPrepMessageController.doAction(ca);
            fail();
        }catch(InvalidActionControllerException ignore){}

        p = game.getPlayers().get(1);
        assertEquals(1, p.getBoard().getInitialRes());
        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, p.getPlayerId(), Resource.GOLD));
        ChooseOneResPrepAnswer answer = (ChooseOneResPrepAnswer) chooseOneResPrepMessageController.doAction(ca);
        assertEquals(0, p.getBoard().getInitialRes());
        assertEquals(p.getBoard().getResInDepot(0), new TreeMap<>(){{
            put(Resource.GOLD, 1);
        }});
        assertEquals(answer.getState(), LocalGameState.PREP_LEADERS);
        assertEquals(answer.getRes(), Resource.GOLD);
    }

    @Test
    public void doActionInvalidRes() throws ControllerException {
        Player p;
        int gameId = MessageControllerTestHelper.doToPrepStateMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer game = ca.getGame();

        p = game.getPlayers().get(1);
        assertEquals(1, p.getBoard().getInitialRes());
        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, p.getPlayerId(), Resource.FAITH));
        try {
            chooseOneResPrepMessageController.doAction(ca);
            fail();
        }catch (InvalidArgumentControllerException ignore){}
        assertEquals(1, p.getBoard().getInitialRes());

        chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, p.getPlayerId(), Resource.ANYTHING));
        try {
            chooseOneResPrepMessageController.doAction(ca);
            fail();
        }catch (InvalidArgumentControllerException ignore){}
        assertEquals(1, p.getBoard().getInitialRes());
    }

    @Test
    public void doActionAllDecided() throws ControllerException {
        int gameId = MessageControllerTestHelper.toDecidedInitResMulti();
        ControllerActionsServerMulti ca = (ControllerActionsServerMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        MultiPlayer game = ca.getGame();
        for(Player p: game.getPlayers()){
            assertEquals(0, p.getBoard().getInitialRes());
        }
    }

    @AfterClass
    public static void cleanTmp(){
        MessageControllerTestHelper.cleanTmp();
    }
}