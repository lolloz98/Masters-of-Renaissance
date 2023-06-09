package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnMultiAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Answer Handler that handles the end of a turn, in a multi-player, modifying the local game.
 */
public class FinishTurnMultiAnswerHandler extends AnswerHandler {

    private static final Logger logger = LogManager.getLogger(FinishTurnMultiAnswerHandler.class);

    public FinishTurnMultiAnswerHandler(FinishTurnMultiAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the local game after a finish turn request.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        if (localGame instanceof LocalMulti) {
            FinishTurnMultiAnswer serverAnswer = (FinishTurnMultiAnswer) getAnswer();
            LocalMulti localMulti = (LocalMulti) localGame;

            //update the grid
            localGame.setLocalDevelopmentGrid(serverAnswer.getLocalGrid());

            //update local turn with current player
            int currentPlayerId = serverAnswer.getNewCurrentPlayerId();
            LocalPlayer currPlayer = localMulti.getPlayerById(currentPlayerId);
            LocalTurnMulti localTurn = localMulti.getLocalTurn();
            localTurn.setCurrentPlayer(currPlayer);
            localTurn.setMainActionOccurred(false);


            localMulti.getLocalDevelopmentGrid().notifyObservers();
            localTurn.notifyObservers();
        } else
            logger.error("the answer is for a multi player game and " + logger.getName() + " has been sent to a single player");
    }
}
