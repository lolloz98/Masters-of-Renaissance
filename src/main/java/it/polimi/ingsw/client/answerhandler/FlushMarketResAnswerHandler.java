package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalPlayer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.FlushMarketResAnswer;

import java.util.ArrayList;

public class FlushMarketResAnswerHandler extends AnswerHandler{

    public FlushMarketResAnswerHandler(FlushMarketResAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        FlushMarketResAnswer serverAnswer=(FlushMarketResAnswer) getAnswer();
        LocalBoard localBoard;

        //update the turn
        localGame.getLocalTurn().setMarketActivated(false);
        localGame.getLocalTurn().notifyObserver();

        //update the faith tracks
        ArrayList<LocalPlayer> localPlayers;
        localPlayers= localGame.getLocalPlayers();
        for(int i=0;i<localPlayers.size();i++){
            localBoard=localPlayers.get(i).getLocalBoard();
            localBoard.setLocalTrack(serverAnswer.getLocalTracks().get(i));
            localBoard.notifyObserver();
        }

        //update normal depots
        localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        localBoard.setResInNormalDepot(serverAnswer.getResInNormalDeposit());
        localBoard.notifyObserver();

        //update leader depots
        localBoard.updateLeaderDepots(serverAnswer.getLocalDepotLeaders());
    }
}
