package it.polimi.ingsw.client;

import it.polimi.ingsw.client.answerhandler.CreateGameAnswerHandler;
import it.polimi.ingsw.client.answerhandler.GameStatusAnswerHandler;
import it.polimi.ingsw.client.answerhandler.JoinGameAnswerHandler;
import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.ParserException;
import it.polimi.ingsw.messages.answers.Answer;
import it.polimi.ingsw.messages.answers.CreateGameAnswer;
import it.polimi.ingsw.messages.answers.GameStatusAnswer;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import it.polimi.ingsw.server.model.exception.ModelException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserClientTest {

    Object object;
    Answer answer;

    public Object check(Answer answer) {
        try {
            Object object = ParserClient.parseAnswer(answer);
            assertEquals(answer.getClass().getSimpleName() + "Handler", object.getClass().getSimpleName());
            return object;
        } catch (ParserException e) {
            fail();
        }
        return null;
    }

    @Test
    public void parseTestCreateGameAnswer() throws ModelException {
        answer = new CreateGameAnswer(1, 0, "player");
        object = check(answer);
        assertEquals(answer, ((CreateGameAnswerHandler) object).getAnswer());
    }

    @Test
    public void parseTestGameStatusAnswerHandler() throws ModelException {
        answer = new GameStatusAnswer(1, 0,  0, new LocalMulti());
        object = check(answer);
        assertEquals(answer, ((GameStatusAnswerHandler) object).getAnswer());
    }

    @Test
    public void parseTestJoinGameAnswerHandler() throws ModelException {
        answer = new JoinGameAnswer(1, 1, new ArrayList<>(), new ArrayList<>());
        object = check(answer);
        assertEquals(answer, ((JoinGameAnswerHandler) object).getAnswer());
    }
}