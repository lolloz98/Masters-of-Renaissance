package it.polimi.ingsw.server.controller.messagesctr;

import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.messages.requests.ClientMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Parser;
import it.polimi.ingsw.server.controller.ControllerActions;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.InvalidActionControllerException;
import it.polimi.ingsw.server.controller.exception.WrongPlayerIdControllerException;
import it.polimi.ingsw.server.controller.exception.WrongStateControllerException;
import it.polimi.ingsw.server.controller.states.PrepareGameState;
import it.polimi.ingsw.server.model.exception.InvalidResourcesToKeepByPlayerException;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Board;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.WarehouseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ChooseOneResPrepMessageController extends ClientMessageController {
    private static final long serialVersionUID = 208L;

    private static final Logger logger = LogManager.getLogger(ChooseOneResPrepMessageController.class);

    public ChooseOneResPrepMessageController(ChooseOneResPrepMessage clientMessage) {
        super(clientMessage);
    }

    @Override
    public void doAction(ControllerActions<?> controllerActions) throws ControllerException{
        if(checkState(controllerActions)){
            Board board = getPlayerFromId(controllerActions).getBoard();
            int initRes = board.getInitialRes();

            if(initRes <= 0) throw new InvalidActionControllerException("not enough initial resources");
            try {
                board.gainResources(new TreeMap<>(){{
                    put(((ChooseOneResPrepMessage)getClientMessage()).getRes(), 1);
                }}, new TreeMap<>(){{
                    put(WarehouseType.NORMAL, new TreeMap<>(){{
                        put(((ChooseOneResPrepMessage)getClientMessage()).getRes(), 1);
                    }});
                }}, controllerActions.getGame());
            } catch (InvalidResourcesToKeepByPlayerException e) {
                logger.error("something unexpected happened in " + this.getClass() + " while putting initial resources in depot");
                throw new ControllerException("not possible to add init resources to the depots");
            }
        } else{
            throw new WrongStateControllerException();
        }
    }

    @Override
    protected boolean checkState(ControllerActions<?> controllerActions) {
        return controllerActions.getGameState() instanceof PrepareGameState;
    }
}
