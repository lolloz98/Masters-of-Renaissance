package it.polimi.ingsw.client.answerhandler;

import it.polimi.ingsw.client.localmodel.LocalMulti;
import it.polimi.ingsw.messages.answers.JoinGameAnswer;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;

public class JoinGameAnswerHandlerTest extends TestCase {
    private LocalMulti multiPlayer;

    @Before
    public void setUp(){
        multiPlayer=new LocalMulti();
    }

    public void testHandleAnswer() {
        JoinGameAnswer serverAnswer=new JoinGameAnswer(2, 3,
                new ArrayList<>(){{
            add(2);
            add(3);
        }},
                new ArrayList<>(){{
            add("giulio");
            add("daniele");
        }});

    }
}