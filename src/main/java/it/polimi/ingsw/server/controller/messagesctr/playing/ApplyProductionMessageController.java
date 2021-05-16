package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.ApplyProductionAnswer;
import it.polimi.ingsw.messages.requests.actions.ApplyProductionMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.Production;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.game.Turn;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * message that handles the application of a production that owns the player
 */
public class ApplyProductionMessageController extends PlayingMessageController {
    private static final Logger logger = LogManager.getLogger(ApplyProductionMessageController.class);


    public ApplyProductionMessageController(ApplyProductionMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws WrongPlayerIdControllerException, InvalidActionControllerException, UnexpectedControllerException, InvalidArgumentControllerException {
        Player thisPlayer = getPlayerFromId(controllerActions);
        Board board = thisPlayer.getBoard();
        ApplyProductionMessage clientMessage = (ApplyProductionMessage) getClientMessage();

        Turn turn = controllerActions.getGame().getTurn();
        if(turn.cannotSetProductionActivated())
            throw new InvalidActionControllerException("Invalid action. At this time you cannot activate productions");

        try {
            board.activateProduction(clientMessage.getWhichProd(), clientMessage.getResToGive(), clientMessage.getResToGain(), controllerActions.getGame());
        } catch (InvalidResourceQuantityToDepotException | InvalidResourcesByPlayerException | InvalidProductionSlotChosenException | ProductionAlreadyActivatedException | NotEnoughResourcesException | ResourceNotDiscountableException | InvalidArgumentException e) {
            throw new InvalidArgumentControllerException(e.getMessage(), 0);
        }

        try {
            turn.setProductionsActivated(true);
        } catch (MainActionAlreadyOccurredException | MarketTrayNotEmptyException | ProductionsResourcesNotFlushedException e) {
            // Since we have already controlled if it could be set to false marketActivated, we are confident we are never coming here
            logger.error("setProductionsActivated threw an unexpected exception after checks. This might have corrupted the status of a game: " + e);
            throw new UnexpectedControllerException("Something unexpected happened. However, the production has been activated: " + e.getMessage());
        }

        //generating the parameters to construct the answer
        Production production;
        try {
            production = board.getProduction(clientMessage.getWhichProd());
        } catch (IllegalArgumentException e) {
            // Since we have already used the parameter whichProd, we are confident we are never coming here
            logger.error("getProduction threw an unexpected exception after checks. This did not corrupt the game, however, the players do not receive answers: " + e);
            throw new UnexpectedControllerException("Something unexpected happened (all the players should ask for the status of the game to correctly see it). The production has been activated.");
        }

        TreeMap<Resource, Integer> resToFlush = new TreeMap<>(production.getGainedResources());

        LocalDepotLeader localDepot;
        ArrayList<LocalDepotLeader> leaderDepots = new ArrayList<>();
        for (DepotLeaderCard leaderDepot : board.getDepotLeaders()) {
            localDepot = ConverterToLocalModel.convert(leaderDepot);
            leaderDepots.add(localDepot);
        }

        TreeMap<Resource, Integer> resInNormalDepots = board.getResInNormalDepots();

        return new ApplyProductionAnswer(clientMessage.getGameId(), clientMessage.getPlayerId(), resToFlush, resInNormalDepots, leaderDepots, clientMessage.getWhichProd());
    }
}
