package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.leader.DiscardLeaderMessage;
import it.polimi.ingsw.messages.requests.leader.LeaderMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NotCurrentPlayerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.model.cards.leader.LeaderCard;
import it.polimi.ingsw.server.model.player.Player;

public class DiscardLeaderMessageController extends PlayingMessageController {


    public DiscardLeaderMessageController(DiscardLeaderMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        if(checkState(controllerActions)){
            if(checkCurrentPlayer(controllerActions)){
                LeaderCard<?> card;
                Player thisPlayer=getPlayerFromId(controllerActions);
                try {
                    card=thisPlayer.getBoard().getLeaderCard(((LeaderMessage)getClientMessage()).getLeaderId());
                }catch(IllegalArgumentException e){
                    throw new ControllerException("you don't own this leader");
                }

                try{
                    thisPlayer.getBoard().removeLeaderCard(card);
                }catch{

                }

            }
            else
                throw new NotCurrentPlayerException("you are not the current player! wait your turn");
        }
        else
            throw new WrongStateControllerException("Wrong request! the game is not in the correct state");
    }

}
