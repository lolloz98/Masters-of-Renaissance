package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalDepotLeader;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;
import it.polimi.ingsw.messages.requests.actions.FlushMarketResMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.UnexpectedControllerException;
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
     * hod that push the chosen resources in the depots
     *
     * @param controllerActions controller action of current game
     * @return FlushMarketResAnswer
     * @throws ControllerException
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Player player = getPlayerFromId(controllerActions);
        Board board = player.getBoard();
        FlushMarketResMessage clientMessage = (FlushMarketResMessage) getClientMessage();

        try {
            board.gainResources(clientMessage.getChosenCombination(), clientMessage.getToKeep(), controllerActions.getGame());
        } catch (InvalidResourcesToKeepByPlayerException | InvalidResourceQuantityToDepotException | DifferentResourceForDepotException e) {
            throw new ControllerException(e.getMessage());
        } catch (InvalidStepsException e) {
            logger.error("trying to give and invalid step number to the faith track while flushing the resources from market");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (EndAlreadyReachedException e) {
            logger.error("trying to move on the faith track while the end is already reached");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (InvalidTypeOfResourceToDepotException e) {
            logger.error("is given an invalid type of resource to the depot");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (FigureAlreadyDiscardedException | FigureAlreadyActivatedException e) {
            logger.error("the vatican figure of a player results already discarded/activated");
            throw new UnexpectedControllerException(e.getMessage());
        } catch (InvalidArgumentException e) {
            logger.error(e.getMessage());
            throw new UnexpectedControllerException(e.getMessage());
        }

        MarketTray market = controllerActions.getGame().getMarketTray();
        market.removeResources();

        ArrayList<LocalTrack> localTracks = controllerActions.getFaithTracks();
        TreeMap<Resource, Integer> resInNormalDeposit = board.getResInNormalDepots();
        ArrayList<LocalDepotLeader> localDepotLeaders = new ArrayList<>();
        for (DepotLeaderCard l : board.getDepotLeaders()) {
            LocalDepotLeader localDepotLeader = ConverterToLocalModel.convert(l);
            localDepotLeaders.add(localDepotLeader);
        }

        return new FlushMarketResAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localTracks, resInNormalDeposit, localDepotLeaders);
    }
}
