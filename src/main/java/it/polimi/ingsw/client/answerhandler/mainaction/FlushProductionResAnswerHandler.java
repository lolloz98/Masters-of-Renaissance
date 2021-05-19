package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushProductionResAnswer;

public class FlushProductionResAnswerHandler extends AnswerHandler {

    public FlushProductionResAnswerHandler(FlushProductionResAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FlushProductionResAnswer serverAnswer=(FlushProductionResAnswer) getAnswer();
        LocalBoard localBoard;

        //update turn
        localGame.getLocalTurn().setProductionsActivated(false);
        localGame.getLocalTurn().notifyObserver();

        //update faith tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();

        // update strongbox
        localBoard.setResInStrongBox(serverAnswer.getResInStrongbox());

        // update productions
        localBoard.flushFromProductions();

        localBoard.notifyObserver();

    }
}
