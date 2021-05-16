package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.ApplyProductionAnswer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnMultiAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinishTurnMultiAnswerHandler extends AnswerHandler{

    private static final Logger logger = LogManager.getLogger(FinishTurnMultiAnswer.class);

    public FinishTurnMultiAnswerHandler(FinishTurnMultiAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        if(localGame instanceof LocalMulti){
            FinishTurnMultiAnswer serverAnswer = (FinishTurnMultiAnswer) getAnswer();
            LocalMulti localMulti=(LocalMulti) localGame;

            //update the grid
            localGame.setLocalDevelopmentGrid(serverAnswer.getLocalGrid());

            //update local turn with current player
            int currentPlayerId=serverAnswer.getNewCurrentPlayerId();
            LocalPlayer currPlayer= localMulti.getPlayerById(currentPlayerId);
            LocalTurnMulti localTurn=localMulti.getLocalTurn();
            localTurn.setCurrentPlayer(currPlayer);
            localTurn.setMainActionOccurred(false);


            localMulti.getLocalDevelopmentGrid().notifyObserver();
            localTurn.notifyObserver();
        }
        else
            logger.error("the answer is for a multiplayer game and it has been sent to a single player");
    }
}
