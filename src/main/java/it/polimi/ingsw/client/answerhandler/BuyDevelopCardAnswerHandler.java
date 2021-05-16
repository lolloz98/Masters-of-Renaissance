package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalGame;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.mainactionsanswer.BuyDevelopCardAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuyDevelopCardAnswerHandler extends AnswerHandler{

    private static final Logger logger = LogManager.getLogger(BuyDevelopCardAnswer.class);

    public BuyDevelopCardAnswerHandler(BuyDevelopCardAnswer answer) {
        super(answer);
    }


    @Override
    public void handleAnswer(LocalGame<?> localGame) {

    }
}
