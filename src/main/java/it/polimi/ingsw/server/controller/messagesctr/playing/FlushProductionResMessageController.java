package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushProductionResAnswer;
import it.polimi.ingsw.messages.requests.actions.FlushProductionResMessage;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.Turn;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;


public class FlushProductionResMessageController extends PlayingMessageController {
    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public FlushProductionResMessageController(FlushProductionResMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws WrongPlayerIdControllerException, InvalidActionControllerException, InvalidArgumentControllerException, UnexpectedControllerException {
        Player thisPlayer = getPlayerFromId(controllerActions);
        Board board = thisPlayer.getBoard();
        Turn turn = controllerActions.getGame().getTurn();

        if(turn.cannotSetProductionActivated())
            throw new InvalidActionControllerException("At this time, you cannot perform this action");

        try {
            board.flushResFromProductions(controllerActions.getGame());
        } catch (ResourceNotDiscountableException | InvalidArgumentException | InvalidStepsException e) {
            throw new InvalidArgumentControllerException(e.getMessage(), 0);
        }

        TreeMap<Resource, Integer> resInStrongBox;
        resInStrongBox=board.getResourcesInStrongBox();

        try {
            turn.setProductionsActivated(false);
        } catch (MarketTrayNotEmptyException | ProductionsResourcesNotFlushedException | MainActionAlreadyOccurredException e) {
            // Since we already controlled if it could be set to false marketActivated, we are confident we are never coming here
            logger.error("setProductionsActivated threw an unexpected exception after checks. This might have corrupted the status of a game: " + e);
            throw new UnexpectedControllerException("Something unexpected happened. However, the resources have been flushed in the strongBox successfully.");
        }

        ArrayList<LocalTrack> localTracks = ConverterToLocalModel.getLocalFaithTracks(controllerActions.getGame());
        return new FlushProductionResAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), resInStrongBox, localTracks);
    }

}
