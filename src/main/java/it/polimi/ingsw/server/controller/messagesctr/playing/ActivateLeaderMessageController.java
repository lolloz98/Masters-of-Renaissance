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
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.ConverterToLocalModel;
import it.polimi.ingsw.enums.Color;
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
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        Board board;
        board = getPlayerFromId(controllerActions).getBoard();
        LeaderCard<?> toActivate;
        try {
            toActivate = board.getLeaderCard(((LeaderMessage) getClientMessage()).getLeaderId());
        } catch (InvalidArgumentException e) {
            throw new InvalidArgumentControllerException(e.getMessage());
        }

        try {
            toActivate.activate(controllerActions.getGame(), controllerActions.getGame().getPlayer(getClientMessage().getPlayerId()));
        } catch (RequirementNotSatisfiedException e) {
            throw new RequirementNotSatisfiedControllerException();
        } catch (AlreadyActiveLeaderException e) {
            throw new AlreadyActiveLeaderControllerException();
        } catch (ActivateDiscardedCardException e) {
            throw new AlreadyDiscardedLeaderControllerException();
        } catch (ModelException e) {
            logger.error("something unexpected happened in " + this.getClass() + " while activating a leader card");
            throw new UnexpectedControllerException(e.getMessage());
        }

        LocalLeaderCard localCard = ConverterToLocalModel.convert(toActivate);

        if (toActivate instanceof DepotLeaderCard) {
            return new ActivateDepotLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard);
        }

        if (toActivate instanceof ProductionLeaderCard) {
            return new ActivateProductionLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard);
        }

        if (toActivate instanceof DiscountLeaderCard) {
            LocalDevelopmentGrid localGrid = ConverterToLocalModel.convert(controllerActions.getGame().getDecksDevelop());
            return new ActivateDiscountLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard, localGrid);
        }
        if (toActivate instanceof MarbleLeaderCard) {
            return new ActivateMarbleLeaderAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(), localCard);
        }

        logger.error("toActivate is an unknown type of leader: " + toActivate.getClass());
        throw new UnexpectedControllerException("unknown type of leaderCard");
    }

}
