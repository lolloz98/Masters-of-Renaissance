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
 * Answer Handler that handles the flush of the resources from the productions modifying the local game.
 */
public class FlushProductionResAnswerHandler extends AnswerHandler {

    public FlushProductionResAnswerHandler(FlushProductionResAnswer answer) {
        super(answer);
    }

    /**
     * method that updates the local game after a flush productions resources request.
     *
     * @param localGame
     */
    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FlushProductionResAnswer serverAnswer = (FlushProductionResAnswer) getAnswer();
        LocalBoard localBoard;

        //update turn
        localGame.getLocalTurn().setProductionsActivated(false);

        //update faith tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        localBoard = localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();

        // update strongbox
        localBoard.setResInStrongBox(serverAnswer.getResInStrongbox());

        // update productions
        localBoard.flushFromProductions();

        // flush leader developments
        for (LocalCard lc : localBoard.getLeaderCards()) {
            if (lc instanceof LocalProductionLeader) {
                LocalProductionLeader lpl = (LocalProductionLeader) lc;
                lpl.setResToFlush(new TreeMap<>());
            }
        }

        // update history
        if (localGame instanceof LocalMulti) {
            LocalMulti localMulti = (LocalMulti) localGame;
            String actionDescription;
            if (serverAnswer.getPlayerId() == localMulti.getMainPlayerId()) {
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
