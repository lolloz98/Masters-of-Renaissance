package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.*;
import it.polimi.ingsw.server.controller.messagesctr.ClientMessageController;
import it.polimi.ingsw.server.controller.State;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;

/**
 * every request that can come only from the current player must inherit from this class
 */

public abstract class PlayingMessageController extends ClientMessageController {

    public PlayingMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * @param controllerActions controllerActions of current game
     * @return answer related to this.clientMessage
     * @throws WrongStateControllerException if the state of the game is not the right one
     * @throws WrongPlayerIdControllerException if the player who sent the request is not the one currently playing
     * @throws InvalidActionControllerException if the action selected is invalid
     * @throws InvalidArgumentControllerException if in the client message there are some invalid arguments
     * @throws UnexpectedControllerException if something unexpected (which might lead to a corrupt game status) happens
     * @throws ControllerException depends on the concrete class
     */
    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        if (checkState(controllerActions)) {
            if (checkCurrentPlayer(controllerActions)) {
                return doActionNoChecks(controllerActions);
            } else throw new WrongPlayerIdControllerException("Wrong request! It is not your turn, you cannot perform this action");
        } else throw new WrongStateControllerException("Wrong request! The game is not in the correct state");
    }

    /**
     * method that checks if the player that has sent the request can do a currentPlayer action
     *
     * @param controllerActions controllerActions of current game
     * @return true if this.playerId is equal to current player id
     */
    protected boolean checkCurrentPlayer(ControllerActions<?> controllerActions) {
        Game<?> game = controllerActions.getGame();
        if (game instanceof SinglePlayer) {
            SinglePlayer singlePlayer = (SinglePlayer) game;
            return !singlePlayer.getTurn().isLorenzoPlaying();
        } else if (game instanceof MultiPlayer) {
            MultiPlayer multiPlayer = (MultiPlayer) game;
            return multiPlayer.getTurn().getCurrentPlayer().getPlayerId() == getClientMessage().getPlayerId();
        }
        return true;
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() == State.PLAY;
    }
}
