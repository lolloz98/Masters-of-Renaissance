package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.requests.*;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.exception.NoSuchControllerException;
import it.polimi.ingsw.server.controller.messagesctr.GameStatusMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.JoinGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.controller.messagesctr.preparation.RemoveLeaderPrepMessageController;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.game.SinglePlayer;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;

/**
 * In this class we ignore the ActionControllers, to test just the messages
 */
public final class MessageControllerTestHelper {

    public static ControllerActionsSingle getSingle(int gameId) throws NoSuchControllerException {
        return (ControllerActionsSingle) ControllerManager.getInstance().getControllerFromMap(gameId);
    }

    public static ControllerActionsMulti getMulti(int gameId) throws NoSuchControllerException {
        return (ControllerActionsMulti) ControllerManager.getInstance().getControllerFromMap(gameId);
    }
    /**
     * create a 4 player game
     *
     * @return gameId
     */
    public static int doActionCreateGameMulti() throws ControllerException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(4, "1"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        return answer.getGameId();
    }

    /**
     * @return gameId
     */
    public static int doActionCreateGameSingle() throws ControllerException {
        CreateGameMessageController createGameMessageController = new CreateGameMessageController(new CreateGameMessage(1, "1"));
        Answer answer = createGameMessageController.doAction(new AnswerListener(null));
        return answer.getGameId();
    }


    private static void doJoinGameMulti(int gameId, String playerName) throws ControllerException {
        JoinGameMessageController joinGameMessageController = new JoinGameMessageController(new JoinGameMessage(gameId, playerName));
        joinGameMessageController.doAction(ControllerManager.getInstance().getControllerFromMap(gameId));
    }

    /**
     * 3 players join the game
     *
     * @param gameId game to join
     */
    public static void doJoinGameMulti(int gameId) throws ControllerException {
        doJoinGameMulti(gameId, "2");
        doJoinGameMulti(gameId, "3");
        doJoinGameMulti(gameId, "4");
    }

    /**
     * create a game with 4 players
     *
     * @return gameId
     */
    public static int toPrepStateMulti() throws ControllerException {
        int gameId = doActionCreateGameMulti();
        doJoinGameMulti(gameId);
        return gameId;
    }

    private static void chooseInitRes(int gameId, Player player) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        while (player.getBoard().getInitialRes() != 0) {
            ChooseOneResPrepMessageController chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, player.getPlayerId(), Resource.GOLD));
            chooseOneResPrepMessageController.doAction(ca);
        }
    }

    /**
     * create a game with 4 players with init resources already picked by all players
     *
     * @return gameId
     */
    public static int toDecidedInitResMulti() throws ControllerException {
        int gameId = toPrepStateMulti();
        for (Player p : ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()) {
            chooseInitRes(gameId, p);
        }
        return gameId;
    }

    private static void removeLeaders(int gameId, Player player) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        RemoveLeaderPrepMessageController removeLeaderPrepMessageController = new RemoveLeaderPrepMessageController(new RemoveLeaderPrepMessage(gameId, player.getPlayerId(), new ArrayList<>() {{
            add(player.getBoard().getLeaderCards().get(0).getId());
            add(player.getBoard().getLeaderCards().get(1).getId());
        }}));
        removeLeaderPrepMessageController.doAction(ca);
    }

    /**
     * create a game with 4 players in ready state (preparation finished)
     *
     * @return gameId
     */
    public static int toReadyMulti() throws ControllerException {
        int gameId = toDecidedInitResMulti();
        for (Player p : ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()) {
            removeLeaders(gameId, p);
        }
        return gameId;
    }

    public static int toReadySingle() throws ControllerException {
        int gameId = doActionCreateGameSingle();
        removeLeaders(gameId, ((SinglePlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayer());
        return gameId;
    }

    public static GameStatusAnswer getGameStatus(int gameId, int playerId) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        GameStatusMessageController gameStatusMessageController = new GameStatusMessageController(new GameStatusMessage(gameId, playerId));
        return (GameStatusAnswer) gameStatusMessageController.doAction(ca);
    }
}
