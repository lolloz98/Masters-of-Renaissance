package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDiscountLeaderAnswer;

public class ActivateDiscountLeaderAnswerHandler extends ActivateLeaderAnswerHandler {

    public ActivateDiscountLeaderAnswerHandler(ActivateDiscountLeaderAnswer answer) {
        super(answer);
    }


    @Override
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //update the grid
        localGame.setLocalDevelopmentGrid(((ActivateDiscountLeaderAnswer)getAnswer()).getLocalGrid());
        localGame.getLocalDevelopmentGrid().notifyObservers();
    }
}
