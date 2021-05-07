package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.utility.PairId;

public class CreateGameMessageController extends BeforeControllerActionsMessageController {
    private static final long serialVersionUID = 202L;

    public CreateGameMessageController(CreateGameMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction() throws ControllerException {
        PairId<Integer, Integer> id = ControllerManager.getInstance().reserveIdForNewGame((CreateGameMessage) getClientMessage());
        return new CreateGameAnswer(id.getFirst(), id.getSecond());
    }
}
