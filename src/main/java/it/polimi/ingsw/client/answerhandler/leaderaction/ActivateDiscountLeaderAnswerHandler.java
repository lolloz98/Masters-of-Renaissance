package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateDiscountLeaderAnswer;

/**
 * Answer Handler that handles the activation of a discount leader modifying the local game
 */
public class ActivateDiscountLeaderAnswerHandler extends ActivateLeaderAnswerHandler {

    public ActivateDiscountLeaderAnswerHandler(ActivateDiscountLeaderAnswer answer) {
        super(answer);
    }


    /**
     * method that updates the grid after an activation of a Discount Leader
     * @param localGame
     */
    @Override
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //update the grid
        localGame.setLocalDevelopmentGrid(((ActivateDiscountLeaderAnswer)getAnswer()).getLocalGrid());
        localGame.getLocalDevelopmentGrid().notifyObservers();
    }
}
