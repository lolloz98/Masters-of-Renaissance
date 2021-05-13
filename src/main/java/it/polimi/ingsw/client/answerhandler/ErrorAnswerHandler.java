package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.*;
import it.polimi.ingsw.messages.answers.ErrorAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorAnswerHandler extends AnswerHandler{
    private static final Logger logger = LogManager.getLogger(ErrorAnswerHandler.class);

    public ErrorAnswerHandler(ErrorAnswer answer) {
        super(answer);
    }

    @Override
    public void handleAnswer(LocalGame<?> localGame) {
        localGame.getError().setErrorMessage(((ErrorAnswer) getAnswer()).getMessage());
    }
}
