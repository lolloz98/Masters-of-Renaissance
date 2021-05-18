package it.polimi.ingsw.client.answerhandler.endgame;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.endgameanswer.EndGameAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EndGameAnswerHandler extends AnswerHandler {

    private static final Logger logger = LogManager.getLogger(EndGameAnswerHandler.class);

    public EndGameAnswerHandler(EndGameAnswer answer) {
        super(answer);
    }

    /**
     * this method sets the state of the local game into over and updates the winners
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        EndGameAnswer serverAnswer=(EndGameAnswer) getAnswer();

        if(localGame instanceof LocalSingle){
            if(serverAnswer.getWinners()==null)
                ((LocalSingle)localGame).setMainPlayerWinner(false);
            else
                ((LocalSingle)localGame).setMainPlayerWinner(true);
        }

        if(localGame instanceof LocalMulti){
            ((LocalMulti)localGame).setWinners(serverAnswer.getWinners());
        }

        localGame.setState(LocalGameState.OVER);
        localGame.notifyObserver();
    }
}
