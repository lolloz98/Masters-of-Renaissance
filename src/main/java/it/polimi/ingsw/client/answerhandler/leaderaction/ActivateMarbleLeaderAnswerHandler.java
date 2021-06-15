package it.polimi.ingsw.client.answerhandler.leaderaction;

import it.polimi.ingsw.client.answerhandler.AnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.leaderanswer.ActivateMarbleLeaderAnswer;

/**
 * Answer Handler that handles the activation of a Marble Leader
 */
public class ActivateMarbleLeaderAnswerHandler extends ActivateLeaderAnswerHandler {
    public ActivateMarbleLeaderAnswerHandler(ActivateMarbleLeaderAnswer answer) {
        super(answer);
    }

    @Override
    /**
     * the player receives the combinations of the resources (due to the marble leader) which can be flushed when
     * the message UseMarketMessage is sent.
     * All the useful updates are done in the super-class.
     */
    protected void handleLeaderAnswer(LocalGame<?> localGame) {
        //this method does nothing
    }
}
