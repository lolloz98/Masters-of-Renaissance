package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.requests.ChooseOneResPrepMessage;
import it.polimi.ingsw.messages.requests.CreateGameMessage;
import it.polimi.ingsw.messages.requests.JoinGameMessage;
import it.polimi.ingsw.server.AnswerListener;
import it.polimi.ingsw.server.controller.exception.ControllerException;
import it.polimi.ingsw.server.controller.messagesctr.creation.CreateGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.creation.JoinGameMessageController;
import it.polimi.ingsw.server.controller.messagesctr.preparation.ChooseOneResPrepMessageController;
import it.polimi.ingsw.server.model.game.MultiPlayer;
import it.polimi.ingsw.server.model.game.Resource;
import it.polimi.ingsw.server.model.player.Player;

/**
 * In this class we ignore the ActionControllers, to test just the messages
 */
public final class MessageControllerTestHelper {
    /**
     * create a 4 player game
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
     * @param gameId game to join
     */
    public static void doJoinGameMulti(int gameId) throws ControllerException {
        doJoinGameMulti(gameId, "2");
        doJoinGameMulti(gameId, "3");
        doJoinGameMulti(gameId, "4");
    }

    /**
     * create a game with 4 players
     * @return gameId
     */
    public static int toPrepStateMulti() throws ControllerException {
        int gameId = doActionCreateGameMulti();
        doJoinGameMulti(gameId);
        return gameId;
    }

    // todo test
    private static void chooseInitRes(int gameId, Player player) throws ControllerException {
        ControllerActions<?> ca = ControllerManager.getInstance().getControllerFromMap(gameId);
        while(player.getBoard().getInitialRes() != 0){
            ChooseOneResPrepMessageController chooseOneResPrepMessageController = new ChooseOneResPrepMessageController(new ChooseOneResPrepMessage(gameId, player.getPlayerId(), Resource.GOLD));
            chooseOneResPrepMessageController.doActionNoChecks(ca);
        }
    }

    /**
     * create a game with 4 players with init resources already picked by all players
     * @return gameId
     */
    //todo test
    public static int toDecidedInitResMulti() throws ControllerException{
        int gameId = toPrepStateMulti();
        for(Player p: ((MultiPlayer) ControllerManager.getInstance().getControllerFromMap(gameId).getGame()).getPlayers()){
            chooseInitRes(gameId, p);
        }
        return gameId;
    }
}
