package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalDevelopCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalProductionLeader;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushProductionResAnswer;

import java.util.TreeMap;

/**
 * class that modify the local game with the parameters passed by the server in the answer, this class is used to update the local game after an FlushProductionResMessage
 */
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

        // flush leader developments
        for (LocalCard lc : localBoard.getLeaderCards()){
            if(lc instanceof LocalProductionLeader){
                LocalProductionLeader lpl = (LocalProductionLeader) lc;
                lpl.setResToFlush(new TreeMap<>());
            }
        }

        // update history
        if(localGame instanceof LocalMulti){
            LocalMulti localMulti = (LocalMulti) localGame;
            String actionDescription;
            if(serverAnswer.getPlayerId() == localMulti.getMainPlayerId()){
                actionDescription = "You used a development";
            } else {
                actionDescription = localMulti.getPlayerById(serverAnswer.getPlayerId()).getName() + " used a development";
            }
            localMulti.getLocalTurn().getHistoryObservable().getHistory().add(actionDescription);
        }

        localBoard.notifyObservers();
        localGame.getLocalTurn().getHistoryObservable().notifyObservers();
    }
}
