package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateGameMessageControllerTest {

    @Test
    public void doAction() throws ControllerException, InvalidArgumentException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(3, "lollo"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        ControllerActionsMulti action = (ControllerActionsMulti)ControllerManager.getInstance().getControllerFromMap(answer.getGameId());
        assertNotNull(action);
        assertNull(action.getGame());
        assertTrue(action.getNumberAndPlayers().getSecond().stream().anyMatch(x -> x.getPlayerId() == answer.getPlayerId()));
    }
}