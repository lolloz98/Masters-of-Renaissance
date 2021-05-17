package it.polimi.ingsw.client.answerhandler.mainaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;

import java.util.ArrayList;

public class FlushMarketResAnswerHandler extends AnswerHandler {

    public FlushMarketResAnswerHandler(FlushMarketResAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FlushMarketResAnswer serverAnswer=(FlushMarketResAnswer) getAnswer();
        LocalBoard localBoard;

        //update the turn
        localGame.getLocalTurn().setMarketActivated(false);

        //reset market
        localGame.getLocalMarket().resetCombinations();

        //update the faith tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        //update normal depots
        localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        localBoard.setResInNormalDepot(serverAnswer.getResInNormalDeposit());

        //update leader depots
        localBoard.updateLeaderDepots(serverAnswer.getLocalDepotLeaders());

        localGame.getLocalTurn().notifyObserver();
        localBoard.notifyObserver();
    }
}
