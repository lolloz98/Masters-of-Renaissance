package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.server.controller.AnswerFactory;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidArgumentControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.State;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.enums.WarehouseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

public class ChooseOneResPrepMessageController extends ClientMessageController {
    private static final long serialVersionUID = 208L;

    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public ChooseOneResPrepMessageController(ChooseOneResPrepMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws ControllerException {
        Board board = getPlayerFromId(controllerActions).getBoard();
        int initRes = board.getInitialRes();

        if (initRes <= 0) throw new InvalidActionControllerException("Not enough initial resources");
        if (((ChooseOneResPrepMessage) getClientMessage()).getRes() == Resource.FAITH) throw new InvalidArgumentControllerException("The resource cannot be of type faith");
        try {
            board.gainResources(new TreeMap<>() {{
                put(((ChooseOneResPrepMessage) getClientMessage()).getRes(), 1);
            }}, new TreeMap<>() {{
                put(WarehouseType.NORMAL, new TreeMap<>() {{
                    put(((ChooseOneResPrepMessage) getClientMessage()).getRes(), 1);
                }});
            }}, controllerActions.getGame());

            // once we add initResource to the depot, we diminish the counter
            board.setInitialRes(initRes - 1);

            // logger.debug("Checking game state");
            if (controllerActions.checkToGamePlayState())
                controllerActions.toGamePlayState();

            return AnswerFactory.createChooseOneResPrepAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), ((ChooseOneResPrepMessage) getClientMessage()).getRes(), controllerActions.getGame());
        } catch (InvalidTypeOfResourceToDepotException | InvalidArgumentException | InvalidResourcesToKeepByPlayerException | InvalidResourceQuantityToDepotException | DifferentResourceForDepotException e) {
            throw new InvalidArgumentControllerException("Invalid argument: " + e.getMessage());
        }
    }

    @Override
    protected boolean checkState(ControllerActionsBase<?> controllerActions) {
        return controllerActions.getGameState() == State.PREPARATION;
    }
}
