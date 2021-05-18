package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalBoard;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.client.localmodel.localcards.LocalCard;
import it.polimi.ingsw.client.localmodel.localcards.LocalLeaderCard;
import it.polimi.ingsw.messages.answers.leaderanswer.LeaderAnswer;

import java.util.ArrayList;

public abstract class ActivateLeaderAnswerHandler extends AnswerHandler {
    public ActivateLeaderAnswerHandler(LeaderAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        LeaderAnswer serverAnswer=(LeaderAnswer) getAnswer();

        //update the leader card
        LocalBoard localBoard=localGame.getPlayerById(serverAnswer.getPlayerId()).getLocalBoard();
        ArrayList<LocalCard> leaderCards=localBoard.getLeaderCards();
        ArrayList<LocalCard> copyOfLeaderCards=new ArrayList<>(leaderCards);

        if(localGame.getMainPlayer().getId()== serverAnswer.getPlayerId()){
            //modifying the arraylist of leader cards in local board
            for(int i=0;i<copyOfLeaderCards.size();i++){
                if(copyOfLeaderCards.get(i).getId()==0) {
                    leaderCards.remove(i);
                    leaderCards.add(serverAnswer.getLeader());
                }
            }
        } else{
            for(LocalCard leader:leaderCards){
                if(leader.getId()==serverAnswer.getLeader().getId())
                    ((LocalLeaderCard)leader).setActive(true);
            }
        }

        handleLeaderAnswer(localGame);

        localBoard.notifyObserver();
    }

    protected abstract void handleLeaderAnswer(LocalGame<?> localGame);
}
