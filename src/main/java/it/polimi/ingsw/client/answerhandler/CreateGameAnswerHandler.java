package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Answer Handler that handles the creation of the game
 */
public class CreateGameAnswerHandler extends AnswerHandler {
    private static final Logger logger = LogManager.getLogger(CreateGameAnswerHandler.class);

    public CreateGameAnswerHandler(CreateGameAnswer answer) {
        super(answer);
    }

    /**
     * method that handles the creation of the local game setting the players.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            int id = getAnswer().getPlayerId();
            String name = ((CreateGameAnswer) getAnswer()).getName();
            localMulti.setMainPlayerId(id);
            localMulti.addLocalPlayer(new LocalPlayer(id, name, new LocalBoard()));
            localMulti.setGameId(getAnswer().getGameId());
        } else {//this method is called only on multiplayer.
            logger.error("create game answer received by singlePlayer game");
        }
        localGame.setState(LocalGameState.WAITING_PLAYERS);
        localGame.notifyObservers();
    }
}

