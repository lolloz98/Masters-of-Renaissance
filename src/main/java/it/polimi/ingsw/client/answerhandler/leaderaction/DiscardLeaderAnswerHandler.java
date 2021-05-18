package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.LocalSingle;
import it.polimi.ingsw.client.localmodel.LocalTrack;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.DiscardLeaderAnswer;

public class DiscardLeaderAnswerHandler extends AnswerHandler {
    public DiscardLeaderAnswerHandler(DiscardLeaderAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        DiscardLeaderAnswer serverAnswer=(DiscardLeaderAnswer) getAnswer();

        LocalBoard localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();

        //update the leadercard
        if(localGame.getMainPlayer().getId()==serverAnswer.getPlayerId()){
            for(LocalCard card: localGame.getMainPlayer().getLocalBoard().getLeaderCards()){
                if(card.getId()==serverAnswer.getLeader().getId())
                    ((LocalLeaderCard)card).setDiscarded(true);
            }
        } else{
            for(LocalCard card: localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard().getLeaderCards()){
                if(card.getId()==0)
                    ((LocalLeaderCard)card).setDiscarded(true);
            }
        }

        //update the tracks
        localGame.updatePlayerFaithTracks(serverAnswer.getLocalTracks());

        //update the lorenzo track
        if(localGame instanceof LocalSingle)
            ((LocalSingle)localGame).setLorenzoTrack(serverAnswer.getLorenzoTrack());

        localBoard.notifyObserver();
    }
}
