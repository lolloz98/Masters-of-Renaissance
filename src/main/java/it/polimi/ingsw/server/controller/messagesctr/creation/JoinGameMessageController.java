package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.ControllerActionsMulti;
import it.polimi.ingsw.server.controller.ControllerManager;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;

public class JoinGameMessageController extends PreGameCreationMessageController {
    private static final long serialVersionUID = 203L;

    public JoinGameMessageController(JoinGameMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        int playerId = ControllerManager.getInstance().joinGame((JoinGameMessage) getClientMessage());

        // we add all the ids of the connected players
        ArrayList<Integer> playerIds = new ArrayList<>();
        if(controllerActions instanceof ControllerActionsMulti){
            for(Player i: ((ControllerActionsMulti) controllerActions).getNumberAndPlayers().getSecond()){
                playerIds.add(i.getPlayerId());
            }
        }else{
            playerIds.add(playerId);
        }

        return new JoinGameAnswer(getClientMessage().getGameId(), playerId, playerIds);
    }
}
