package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;

/**
 * every request that can come only from the current player must inherit from this class
 */
public abstract class CurrentPlayerMessageController extends ClientMessageController{
    private static final long serialVersionUID = 206L;

    public CurrentPlayerMessageController(ClientMessage clientMessage) {
        super(clientMessage);
    }

    /**
     * method that checks if the player that has sent the request can do a currentPlayer action
     * @param controllerActions
     * @return true if this.playerId is equal to current layer id
     */
    protected boolean checkCurrentPlayer(ControllerActions controllerActions){
        Game game=controllerActions.getGame();
        if(game instanceof SinglePlayer){
            SinglePlayer singlePlayer=(SinglePlayer) game;
            if(singlePlayer.getTurn().isLorenzoPlaying())
                return false;
        }
        else if(game instanceof MultiPlayer){
            MultiPlayer multiPlayer=(MultiPlayer) game;
            if(multiPlayer.getTurn().getCurrentPlayer().getPlayerId()!=getClientMessage().getPlayerId())
                return false;
        }
        return true;
    }
}
