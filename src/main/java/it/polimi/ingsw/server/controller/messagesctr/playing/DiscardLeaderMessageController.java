package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.exception.InvalidArgumentException;
import it.polimi.ingsw.server.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscardLeaderMessageController extends PlayingMessageController {

    private static final Logger logger = LogManager.getLogger(DiscardLeaderMessageController.class);


    public DiscardLeaderMessageController(DiscardLeaderMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * discard the leaderCard from the player's board
     * @param controllerActions current controller action
     * @return DiscardLeaderCardAnswer to notify the changes
     * @throws ControllerException if the player doesn't own the card
     */
    @Override
    protected Answer doActionNoChecks(ControllerActions<?> controllerActions) throws ControllerException {
        LeaderCard<?> card;
        Player thisPlayer=getPlayerFromId(controllerActions);
        try {
            card=thisPlayer.getBoard().getLeaderCard(((LeaderMessage)getClientMessage()).getLeaderId());
        }catch(InvalidArgumentException e){
            throw new ControllerException("you don't own this leader");
        }

        try{
            thisPlayer.getBoard().discardLeaderCard(card);
        }catch(InvalidArgumentException e){
            logger.error("something went wrong in " + this.getClass() + "while discarding a leadercard");
            throw new ControllerException("unexpected error");
        }

        return new DiscardLeaderAnswer(getClientMessage().getGameId(),getClientMessage().getPlayerId(),card.getId());
    }
}
