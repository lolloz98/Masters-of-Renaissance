package it.polimi.ingsw.server.controller.messagesctr.playing;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
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

    @Override
    public Answer doAction(ControllerActions<?> controllerActions) throws ControllerException {
        if (checkState(controllerActions)) {
            if (checkCurrentPlayer(controllerActions)) {
                return doActionNoChecks(controllerActions);
            } else throw new WrongStateControllerException("Wrong request! the game is not in the correct state");
        } else throw new WrongStateControllerException("Wrong request! the game is not in the correct state");
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
