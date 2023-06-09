package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActionsBase;
import it.polimi.ingsw.server.controller.ControllerActionsServer;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.WrongPlayerIdControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for the messages
 */
public abstract class ClientMessageController implements Serializable {
    private static final long serialVersionUID = 200L;

    private final ClientMessage clientMessage;

    public ClientMessageController(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    /**
     *
     * @param controllerActions of the current game
     * @return player who sent the message
     * @throws WrongPlayerIdControllerException the id in the message does not match with the id of a player in this game
     */
    protected Player getPlayerFromId(ControllerActionsBase<?> controllerActions) throws WrongPlayerIdControllerException {
        Game<?> game = controllerActions.getGame();
        Player player;
        int playerId = getClientMessage().getPlayerId();
        if (game instanceof MultiPlayer) {
            List<Player> players = ((MultiPlayer) game).getPlayers().stream().filter(x -> x.getPlayerId() == playerId).collect(Collectors.toList());
            if (players.size() != 1)
                throw new WrongPlayerIdControllerException("number of players with specified id: " + players.size());
            player = players.get(0);
        } else {
            // Do we want to check the player id in this case? -> maybe not
            player = ((SinglePlayer) game).getPlayer();
        }
        return player;
    }

    /**
     * After checking the status of the game it
     * @param controllerActions of the current game
     * @return answer related to this specific request
     */
    public Answer doAction(ControllerActionsBase<?> controllerActions) throws ControllerException {
        if (checkState(controllerActions)) {
            return doActionNoChecks(controllerActions);
        } else throw new WrongStateControllerException("Wrong request! the game is not in the correct state");
    }

    /**
     * do action without checking for the state and current player
     *
     * @param controllerActions controller action of current game
     * @return answer of this message
     * @throws ControllerException if something wrong with the message
     */
    protected abstract Answer doActionNoChecks(ControllerActionsBase<?> controllerActions) throws ControllerException;

    protected abstract boolean checkState(ControllerActionsBase<?> controllerActions);
}
