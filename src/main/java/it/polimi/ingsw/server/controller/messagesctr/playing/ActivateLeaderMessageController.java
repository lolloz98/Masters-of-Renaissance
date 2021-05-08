package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.leader.ActivateDepotLeaderAnswer;
import it.polimi.ingsw.messages.answers.leader.ActivateDiscountLeaderAnswer;
import it.polimi.ingsw.messages.answers.leader.ActivateMarbleLeaderAnswer;
import it.polimi.ingsw.messages.answers.leader.ActivateProductionLeaderAnswer;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.cards.leader.*;
import it.polimi.ingsw.server.model.exception.ActivateDiscardedCardException;
import it.polimi.ingsw.server.model.exception.AlreadyActiveLeaderException;
import it.polimi.ingsw.server.model.exception.RequirementNotSatisfiedException;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActivateLeaderMessageController extends PlayingMessageController {
    private static final long serialVersionUID = 208L;

    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public ActivateLeaderMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {

        if(checkState(controllerActions)){
            if(checkCurrentPlayer(controllerActions)){
                Board board;
                board = controllerActions.getGame().getPlayer(getClientMessage().getPlayerId()).getBoard();
                LeaderCard toActivate= board.getLeaderCard(((LeaderMessage)getClientMessage()).getLeaderId());

                try{
                    toActivate.activate(controllerActions.getGame(),controllerActions.getGame().getPlayer(getClientMessage().getPlayerId()));
                }catch (RequirementNotSatisfiedException e){
                    throw new ControllerException("you don't have the requirements to activate this card, try again on the next turn");
                }catch(AlreadyActiveLeaderException e){
                    throw new ControllerException("you have already activated this card");
                }catch(ActivateDiscardedCardException e){
                    throw new ControllerException("you cannot activate a discarded card");
                }catch(IllegalArgumentException e){
                    logger.debug("something unexpected happened in " + this.getClass() + " while activating a leader card");
                    throw new ControllerException("not possible to activate leader card");
                }

                if(toActivate instanceof DepotLeaderCard){
                    DepotLeaderCard card=(DepotLeaderCard) toActivate;
                    return new ActivateDepotLeaderAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),card.getId());
                }
                if(toActivate instanceof ProductionLeaderCard){
                    ProductionLeaderCard card=(ProductionLeaderCard) toActivate;
                    return new ActivateProductionLeaderAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),card.getId());
                }
                if(toActivate instanceof DiscountLeaderCard){
                    return new ActivateDiscountLeaderAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),controllerActions.getGame().getDecksDevelop());
                }
                if(toActivate instanceof MarbleLeaderCard){
                    return new ActivateMarbleLeaderAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),controllerActions.getGame().getMarketTray().getLeaderResources());
                }

            }
            else
                throw new NotCurrentPlayerException();
        }
        else
            throw new WrongStateControllerException();
        return null;
    }

}
