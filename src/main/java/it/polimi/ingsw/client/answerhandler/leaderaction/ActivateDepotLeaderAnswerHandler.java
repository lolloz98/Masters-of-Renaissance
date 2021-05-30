package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDepotLeaderAnswer;

//todo
public class ActivateDepotLeaderAnswerHandler extends ActivateLeaderAnswerHandler {

    public ActivateDepotLeaderAnswerHandler(ActivateDepotLeaderAnswer answer) {
        super(answer);
    }


    /**
     * the leader depot is already updated in the method of ActivateLeaderAnswerHandler(super-class)
     * @param localGame
     */
    @Override
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //this method does nothing
    }
}
