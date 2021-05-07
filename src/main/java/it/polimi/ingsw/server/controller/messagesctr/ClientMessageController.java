package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.WrongPlayerIdControllerException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ClientMessageController implements Serializable {
    private static final long serialVersionUID = 200L;

    private final ClientMessage clientMessage;

    public ClientMessageController(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    protected Player getPlayerFromId(ControllerActions<?> controllerActions) throws ControllerException{
        Game<?> game = controllerActions.getGame();
        Player player;
        int playerId = getClientMessage().getPlayerId();
        if(game instanceof MultiPlayer){
            List<Player> players = ((MultiPlayer)game).getPlayers().stream().filter(x -> x.getPlayerId() == playerId).collect(Collectors.toList());
            if(players.size() != 1) throw new WrongPlayerIdControllerException("number of players with specified id: " + players.size());
            player = players.get(0);
        }else{
            // Do we want to check the player id in this case? -> maybe not
            player = ((SinglePlayer)game).getPlayer();
        }
        return player;
    }

    public abstract void doAction(ControllerActions<?> controllerActions) throws ControllerException;

    protected abstract boolean checkState(ControllerActions<?> controllerActions);
}
