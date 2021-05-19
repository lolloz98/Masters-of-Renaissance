package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.RemoveLeaderPrepMessage;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.LeaderNotRemovedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.State;
import it.polimi.ingsw.server.controller.messagesctr.playing.ConcealedLeaderMessageInterface;
import it.polimi.ingsw.server.controller.messagesctr.playing.PlayingMessageController;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RemoveLeaderPrepMessageController extends ClientMessageController implements ConcealedLeaderMessageInterface {
    private static final long serialVersionUID = 209L;

    private static final Logger logger = LogManager.getLogger(RemoveLeaderPrepMessageController.class);

    public RemoveLeaderPrepMessageController(RemoveLeaderPrepMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Board board = getPlayerFromId(controllerActions).getBoard();
        ArrayList<Integer> toRemove = ((RemoveLeaderPrepMessage) getClientMessage()).getLeadersToRemove();

        if (toRemove.size() != 2)
            throw new InvalidActionControllerException("Wrong quantity of leader chosen: you should choose just two leaders");

        try {
            board.removeLeaderCards(toRemove);
        } catch (InvalidArgumentException e) {
            throw new LeaderNotRemovedControllerException("Not possible to remove these leaders");
        }

        if (controllerActions.checkToGamePlayState())
            controllerActions.toGamePlayState();

        return AnswerFactory.createRemoveLeaderPrepAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), toRemove, controllerActions.getGame());

    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() == State.PREPARATION;
    }
}
