package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateGameMessageControllerTest {

    @Test
    public void doAction() throws ControllerException, InvalidArgumentException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(3, "lollo"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        ControllerActionsServerMulti action = (ControllerActionsServerMulti)ControllerManager.getInstance().getControllerFromMap(answer.getGameId());
        assertNotNull(action);
        assertNull(action.getGame());
        assertTrue(action.getNumberAndPlayers().getSecond().stream().anyMatch(x -> x.getPlayerId() == answer.getPlayerId()));
    }
}