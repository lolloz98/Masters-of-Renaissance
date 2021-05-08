package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;

public class JoinGameMessageController extends PreGameCreationMessageController {
    private static final long serialVersionUID = 203L;

    public JoinGameMessageController(JoinGameMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction() throws ControllerException {
        int playerId = ControllerManager.getInstance().joinGame((JoinGameMessage) getClientMessage());
        return new JoinGameAnswer(getClientMessage().getGameId(), playerId);
    }
}
