package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
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

        //update faith tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();

        // update strongbox
        localBoard.setResInStrongBox(serverAnswer.getResInStrongbox());

        // update productions
        localBoard.flushFromProductions();

        // update history
        if(localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            String actionDescription;
            if(serverAnswer.getPlayerId() == localMulti.getMainPlayerId()){
                actionDescription = "You used a development";
            } else {
                actionDescription = localMulti.getPlayerById(serverAnswer.getPlayerId()).getName() + " used a development";
            }
            localMulti.getLocalTurn().getHistory().add(actionDescription);
        }

        localBoard.notifyObservers();
    }
}
