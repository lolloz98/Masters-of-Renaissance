package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.preparationanswer.ChooseOneResPrepAnswer;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.model.exception.InvalidResourcesToKeepByPlayerException;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.WarehouseType;
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
    public Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Board board = getPlayerFromId(controllerActions).getBoard();
        int initRes = board.getInitialRes();

        if (initRes <= 0) throw new InvalidActionControllerException("not enough initial resources");
        try {
            board.gainResources(new TreeMap<>() {{
                put(((ChooseOneResPrepMessage) getClientMessage()).getRes(), 1);
            }}, new TreeMap<>() {{
                put(WarehouseType.NORMAL, new TreeMap<>() {{
                    put(((ChooseOneResPrepMessage) getClientMessage()).getRes(), 1);
                }});
            }}, controllerActions.getGame());

            if (controllerActions.checkToGamePlayState())//if the preparation state is ended(all the players have discarded 2 leaders and have chosen the beginning resources)
                controllerActions.checkToGamePlayState();

            return new ChooseOneResPrepAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), ((ChooseOneResPrepMessage) getClientMessage()).getRes());

        } catch (InvalidResourcesToKeepByPlayerException e) {
            logger.error("something unexpected happened in " + this.getClass() + " while putting initial resources in depot");
            throw new ControllerException("not possible to add init resources to the depots");
        }
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() instanceof PrepareGameState;
    }
}
