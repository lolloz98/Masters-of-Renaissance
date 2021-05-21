package it.polimi.ingsw.server.controller.messagesctr.creation;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.ControllerActionsServerMulti;
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
    public Answer doAction(ControllerActionsBase<?> controllerActions) throws ControllerException {
        int playerId = ControllerManager.getInstance().joinGame((JoinGameMessage) getClientMessage());

        // we add all the ids of the connected players
        ArrayList<Integer> playerIds = new ArrayList<>();
        ArrayList<String> playerNames = new ArrayList<>();
        if (controllerActions instanceof ControllerActionsServerMulti) {
            for (Player i : ((ControllerActionsServerMulti) controllerActions).getNumberAndPlayers().getSecond()) {
                playerIds.add(i.getPlayerId());
                playerNames.add(i.getName());
            }
        } else {
            playerIds.add(playerId);
        }

        return new JoinGameAnswer(getClientMessage().getGameId(), playerId, playerIds, playerNames);
    }
}
