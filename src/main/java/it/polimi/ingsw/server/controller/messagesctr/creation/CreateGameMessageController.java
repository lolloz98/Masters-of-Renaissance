package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.utility.PairId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class CreateGameMessageController implements Serializable {
    private static final Logger logger = LogManager.getLogger(CreateGameMessageController.class);

    private static final long serialVersionUID = 202L;

    private final ClientMessage clientMessage;

    public CreateGameMessageController(CreateGameMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public Answer doAction(AnswerListener answerListener) throws ControllerException {
        PairId<Integer, Integer> id = ControllerManager.getInstance().reserveIdForNewGame((CreateGameMessage) getClientMessage(), answerListener);
        logger.debug("id " + id.getFirst() + " successfully reserved");
        if(((CreateGameMessage) getClientMessage()).getPlayersNumber() == 1){
            return AnswerFactory.createGameStatusAnswer(id.getFirst(), id.getSecond(), id.getSecond(), ControllerManager.getInstance().getControllerFromMap(id.getFirst()).getGame());
        }
        return new CreateGameAnswer(id.getFirst(), id.getSecond(), ((CreateGameMessage) getClientMessage()).getUserName());
    }
}
