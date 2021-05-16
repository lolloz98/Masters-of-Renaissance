package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.leader.DepotLeaderCard;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.game.MarketTray;
import it.polimi.ingsw.enums.Resource;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeMap;


public class FlushMarketResMessageController extends PlayingMessageController {
    private static final Logger logger = LogManager.getLogger(FlushMarketResMessageController.class);

    public FlushMarketResMessageController(FlushMarketResMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * Flush a combination of the market in the depots
     *
     * @param controllerActions controller action of current game
     * @return FlushMarketResAnswer
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws InvalidActionControllerException, WrongPlayerIdControllerException, InvalidArgumentControllerException, UnexpectedControllerException {
        Player player = getPlayerFromId(controllerActions);
        Board board = player.getBoard();
        FlushMarketResMessage msg = (FlushMarketResMessage) getClientMessage();

        MarketTray marketTray = controllerActions.getGame().getMarketTray();

        if(controllerActions.getGame().getTurn().cannotSetMarketActivated(false) || marketTray.getResCombinations().size() == 0)
            throw new InvalidActionControllerException("At this time, this action is not valid: cannot flush the market resources in the depots.");

        if(!marketTray.checkResources(msg.getChosenCombination()))
            throw new InvalidArgumentControllerException("The combination of resources chosen is invalid. Please select a valid combination.");

        try {
            board.gainResources(msg.getChosenCombination(), msg.getToKeep(), controllerActions.getGame());
        } catch (InvalidResourcesToKeepByPlayerException | DifferentResourceForDepotException | InvalidResourceQuantityToDepotException | InvalidArgumentException | InvalidTypeOfResourceToDepotException e) {
            throw new InvalidArgumentControllerException("Invalid arguments: " + e.getMessage());
        }
        marketTray.removeResources();

        try {
            controllerActions.getGame().getTurn().setMarketActivated(false);
        } catch (ModelException e) {
            // Since we already controlled if it could be set to false marketActivated, we are confident we are never coming here
            logger.error("SetMarketActivate threw an unexpected exception after checks. This might have corrupted the status of a game: " + e);
            throw new UnexpectedControllerException("Something unexpected happened. However, the resources have been flushed in the depots.");
        }

        ArrayList<LocalTrack> localTracks = ConverterToLocalModel.getLocalFaithTracks(controllerActions.getGame());
        TreeMap<Resource, Integer> resInNormalDeposit = board.getResInNormalDepots();
        ArrayList<LocalDepotLeader> localDepotLeaders = new ArrayList<>();
        for (DepotLeaderCard l : board.getDepotLeaders()) {
            LocalDepotLeader localDepotLeader = ConverterToLocalModel.convert(l);
            localDepotLeaders.add(localDepotLeader);
        }

        return new FlushMarketResAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localTracks, resInNormalDeposit, localDepotLeaders);
    }
}
