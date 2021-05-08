package it.polimi.ingsw.server.controller.messagesctr.preparation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.DiscardLeaderPrepAnswer;
import it.polimi.ingsw.messages.requests.DiscardLeaderPrepMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.player.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class DiscardLeaderPrepMessageController extends ClientMessageController {
    private static final long serialVersionUID = 209L;

    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public DiscardLeaderPrepMessageController(DiscardLeaderPrepMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        if(checkState(controllerActions)){
            Board board = getPlayerFromId(controllerActions).getBoard();
            ArrayList<LeaderCard> toRemove=((DiscardLeaderPrepMessage)getClientMessage()).getLeadersToDiscard();

            if(toRemove.size()!=2) throw new InvalidActionControllerException("Wrong quantity of leader chosen: you should choose just two leaders");

            try {
                board.removeLeaderCards(toRemove);
            }catch (IllegalArgumentException e){
                logger.error("something unexpected happened in " + this.getClass() + " while removing leaders");
                throw new ControllerException("not possible to remove these leaders");
            }

            if(controllerActions.checkToGamePlayState())
                controllerActions.toGamePlayState();

             return new DiscardLeaderPrepAnswer(getClientMessage().getGameId(), getClientMessage().getPlayerId(),toRemove);

        }
        else
            throw new WrongStateControllerException();
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() instanceof PrepareGameState;
    }
}
