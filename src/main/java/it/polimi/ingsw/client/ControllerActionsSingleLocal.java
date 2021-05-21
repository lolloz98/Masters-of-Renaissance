package it.polimi.ingsw.client;

import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServerSingle;
import it.polimi.ingsw.server.controller.ControllerActionsSingleHelper;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.PreGameCreationMessageController;
import it.polimi.ingsw.server.model.exception.EmptyDeckException;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ControllerActionsSingleLocal extends ControllerActionsBase<SinglePlayer> {
    private static final Logger logger = LogManager.getLogger(ControllerActionsSingleLocal.class);

    /**
     * We are using this class not to replicate server code useful also in a local game
     */
    private final ControllerActionsSingleHelper controllerActionsSingleHelper;

    public ControllerActionsSingleLocal(SinglePlayer game, int gameId) throws EmptyDeckException {
        super(game, gameId);
        controllerActionsSingleHelper = new ControllerActionsSingleHelper();
        controllerActionsSingleHelper.constructor(this);
    }

    @Override
    public boolean checkToGamePlayState() {
        return controllerActionsSingleHelper.checkToGamePlayState(game);
    }

    @Override
    public void removeLeadersEffect() throws UnexpectedControllerException {
        controllerActionsSingleHelper.removeLeadersEffect();
    }

    @Override
    public void applyLeadersEffect() throws UnexpectedControllerException {
        controllerActionsSingleHelper.applyLeadersEffect();
    }

    @Override
    public ArrayList<LocalPlayer> getWinners() throws UnexpectedControllerException {
        return controllerActionsSingleHelper.getWinners(game);
    }

    @Override
    public void doAction(ClientMessageController clientMessage) throws ControllerException {

    }

    @Override
    public void doPreGameAction(PreGameCreationMessageController clientMessage, AnswerListener answerListener) throws ControllerException {

    }

    @Override
    public void doDiscardOrRemoveLeader(ClientMessageController parsedMessage) throws ControllerException {

    }

    @Override
    public void sendGameStatusToAll(int gameId, int playerIdLastRequest) throws UnexpectedControllerException {

    }

    @Override
    public void doGetStatusAction(GameStatusMessageController parsedMessage) throws ControllerException {

    }

    @Override
    public void destroyGame(String message, int playerId, boolean removePlayerIdListener) throws NoSuchControllerException {

    }
}
