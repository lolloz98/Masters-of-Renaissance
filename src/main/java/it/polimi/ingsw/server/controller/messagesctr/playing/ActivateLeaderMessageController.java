package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.client.localmodel.LocalDevelopmentGrid;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDepotLeaderAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDiscountLeaderAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateMarbleLeaderAnswer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateProductionLeaderAnswer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.leader.ActivateLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.server.model.cards.Color;
import it.polimi.ingsw.server.model.cards.DeckDevelop;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.*;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

public class ActivateLeaderMessageController extends PlayingMessageController {
    private static final long serialVersionUID = 208L;

    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public ActivateLeaderMessageController(ActivateLeaderMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Board board;
        board = getPlayerFromId(controllerActions).getBoard();
        LeaderCard<?> toActivate;
        try {
            toActivate = board.getLeaderCard(((LeaderMessage) getClientMessage()).getLeaderId());
        } catch (InvalidArgumentException e) {
            throw new ControllerException(e.getMessage());
        }

        try {
            toActivate.activate(controllerActions.getGame(), controllerActions.getGame().getPlayer(getClientMessage().getPlayerId()));
        } catch (RequirementNotSatisfiedException e) {
            throw new ControllerException("you don't have the requirements to activate this card, try again on the next turn");
        } catch (AlreadyActiveLeaderException e) {
            throw new ControllerException("you have already activated this card");
        } catch (ActivateDiscardedCardException e) {
            throw new ControllerException("you cannot activate a discarded card");
        } catch (ModelException e) {
            // todo check
            logger.warn("something unexpected happened in " + this.getClass() + " while activating a leader card");
            throw new ControllerException(e.getMessage());
        }

        if (toActivate instanceof DepotLeaderCard) {
            DepotLeaderCard card = (DepotLeaderCard) toActivate;
            LocalLeaderCard localCard = ConverterToLocalModel.convert(card);
            return new ActivateDepotLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard);
        }

        if (toActivate instanceof ProductionLeaderCard) {
            ProductionLeaderCard card = (ProductionLeaderCard) toActivate;
            LocalLeaderCard localCard = ConverterToLocalModel.convert(card);
            int whichLeaderProd = board.getProductionLeaders().indexOf(card) + 4; //it must be 4 or 5
            return new ActivateProductionLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard, whichLeaderProd);
        }

        if (toActivate instanceof DiscountLeaderCard) {

            TreeMap<Color, TreeMap<Integer, DeckDevelop>> decksDevelop;
            decksDevelop = controllerActions.getGame().getDecksDevelop();


            LocalDevelopmentGrid localGrid;
            localGrid = ConverterToLocalModel.convert(decksDevelop);
            DiscountLeaderCard card = (DiscountLeaderCard) toActivate;
            LocalLeaderCard localCard = ConverterToLocalModel.convert(card);
            return new ActivateDiscountLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard, localGrid);
        }
        if (toActivate instanceof MarbleLeaderCard) {
            MarbleLeaderCard card = (MarbleLeaderCard) toActivate;
            LocalLeaderCard localCard = ConverterToLocalModel.convert(card);
            return new ActivateMarbleLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard);
        }
        logger.error("toActivate is an unknown type of leader: " + toActivate.getClass());
        throw new ControllerException("unknown type of leaderCard");
    }

}
