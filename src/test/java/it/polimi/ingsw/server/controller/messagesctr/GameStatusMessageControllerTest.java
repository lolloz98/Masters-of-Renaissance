package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalConcealedCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerActionsSingle;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.MessageControllerTestHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameStatusMessageControllerTest {
    GameStatusMessageController gameStatusMessageController;
    GameStatusAnswer gameStatusAnswer;
    int gameId;
    ControllerActionsMulti ca;
    ControllerActionsSingle cas;

    LocalMulti lm;
    LocalSingle ls;

    @Test
    public void doActionWrongState() throws ControllerException {
        gameId = MessageControllerTestHelper.doActionCreateGameMulti();
        ca = (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
        try {
            gameStatusAnswer = MessageControllerTestHelper.getGameStatus(gameId, ca.getNumberAndPlayers().getSecond().get(0).getPlayerId());
            fail();
        } catch (WrongStateControllerException ignore) {}
    }

    private void checkMulti() throws ControllerException {
        for(Player p: ca.getGame().getPlayers()) {
            lm = (LocalMulti) MessageControllerTestHelper.getGameStatus(gameId, p.getPlayerId()).getGame();
            List<LocalPlayer> lps = lm.getLocalPlayers();
            for(LocalPlayer lp: lps) {
                for (LocalCard lc : lp.getLocalBoard().getLeaderCards()) {
                    if (lp.getId() == p.getPlayerId()){
                        if(lc instanceof LocalConcealedCard) fail();
                    }else{
                        if(lc instanceof LocalLeaderCard) fail();
                    }
                }
            }
        }
    }

    @Test
    public void doAction() throws ControllerException {
        gameId = MessageControllerTestHelper.toPrepStateMulti();
        ca = MessageControllerTestHelper.getMulti(gameId);
        checkMulti();

        gameId = MessageControllerTestHelper.toReadyMulti();
        ca = MessageControllerTestHelper.getMulti(gameId);
        checkMulti();
    }

    @Test
    public void doActionSingle() throws ControllerException{
        gameId = MessageControllerTestHelper.doActionCreateGameSingle();
        cas = MessageControllerTestHelper.getSingle(gameId);
        ls = (LocalSingle) MessageControllerTestHelper.getGameStatus(gameId, cas.getGame().getPlayer().getPlayerId()).getGame();
        assertEquals(gameId, ls.getGameId());
    }
}