package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FinishTurnSingleAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinishTurnSingleAnswerHandler extends AnswerHandler{

    private static final Logger logger = LogManager.getLogger(FinishTurnSingleAnswerHandler.class);


    public FinishTurnSingleAnswerHandler(FinishTurnSingleAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FinishTurnSingleAnswer serverAnswer=(FinishTurnSingleAnswer) getAnswer();

        if(localGame instanceof LocalSingle){
            LocalSingle localSingle=(LocalSingle) localGame;
            LocalBoard localBoard=localSingle.getMainPlayer().getLocalBoard();

            localGame.getLocalTurn().setMainActionOccurred(false);
            localGame.getLocalTurn().notifyObserver();

            //update the grid
            localSingle.setLocalDevelopmentGrid(serverAnswer.getLocalGrid());
            localSingle.getLocalDevelopmentGrid().notifyObserver();

            //update lorenzo's track
            localSingle.getLorenzoTrack().setFaithTrackScore(serverAnswer.getLocalLorenzoTrack().getFaithTrackScore());

            //update player's track
            localBoard.setLocalTrack(serverAnswer.getLocalPlayerTrack());

            localBoard.notifyObserver();

        } else
            logger.error("the answer is for a single player game and " + logger.getName() + " has been sent to a multiplayer player");

    }
}
